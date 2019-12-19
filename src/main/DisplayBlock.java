package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import controlP5.Button;
import controlP5.DropdownList;
import evaluate.FunctionAnalysis;
import evaluate.StreetAnalysis;
import gmaps.GmapsTypeDetail;
import gmaps.GmapsTypeDetail.Types;
import osm.GeoMath;
import osm.OsmTypeDetail;
import processing.core.PApplet;
import utils.Aoi;
import utils.ColorHelper;
import utils.Container;
import utils.ExportDXF;
import utils.Gpoi;
import utils.Poi;
import utils.Tools;
import wblut.geom.WB_AABB;
import wblut.geom.WB_GeometryOp;
import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;
import wblut.geom.WB_Polygon;

public class DisplayBlock extends PApplet {

	List<WB_Point> pts;
	List<WB_Polygon> polygons;
	List<WB_PolyLine> plys;
	Tools tools;
	public static final int LEN_OF_CAMERA = 5000;

	Random rand = new Random(233);

	int[] arr;
	int[][] c;
	int[][] a;

	String[] header;

	public DropdownList poiType;
	public Button typeButton;

	public void settings() {
		size(1200, 1000, P3D);
		c = new int[11000][3];
		for (int i = 0; i < 11000; ++i) {
			c[i] = new int[] { rand.nextInt(255), 100, 200 };
		}
	}

	public void setup() {
		tools = new Tools(this, LEN_OF_CAMERA);
		seivePolyline();
//		calcBlock();
		readBlock();

		initGUI();
	}

	public void draw() {
		background(255);
		tools.cam.drawSystem(LEN_OF_CAMERA);

		if (pts != null) {
			stroke(ColorHelper.RED);
			fill(ColorHelper.RED);
			tools.render.drawPoint(pts, 4);
		}

		drawBlock();
		stroke(0);

		if (arr != null)
			drawDigit(arr);

		tools.drawCP5();
	}

	public void initGUI() {

		poiType = tools.cp5.addDropdownList("poiType").setPosition(100, 100).setSize(240, 240).setItemHeight(30)
				.setBarHeight(30).setColorBackground(color(255, 128)).setColorActive(color(0))
				.setColorForeground(color(255, 100, 0));

		poiType.getCaptionLabel().toUpperCase(true);
		poiType.getCaptionLabel().setColor(0xffff0000);
		poiType.getCaptionLabel().getStyle().marginTop = 3;
		poiType.getValueLabel().getStyle().marginTop = 3;

		typeButton = tools.cp5.addButton("typeControl").setPosition(100, 80);

		int c[][] = ColorHelper.createGradientHue(GmapsTypeDetail.Types.values().length + 1, ColorHelper.RED,
				ColorHelper.BLUE);
		int cnt = 0;
		for (Types type : GmapsTypeDetail.Types.values()) {
			String str = type.toString();

			poiType.addItem(str, cnt).setLabel(str).setColorBackground(color(c[cnt][0], c[cnt][1], c[cnt][2]));
			cnt++;
		}
		poiType.addItem("all", cnt++).setLabel("all");

	}

	public void typeControl() {
		String label = poiType.getLabel();

		int tmp = 0;
		for (int i = 0; i < header.length; ++i)
			if (header[i].equals(label)) {
				tmp = i;
			}

		arr = a[tmp];
		setColor(arr);
	}

	public void seivePolyline() {

		plys = new ArrayList<>();
		for (Aoi aoi : Container.aois) {
			if (!aoi.isHighway)
				continue;
			if (!aoi.getTags().containsKey("junction") && aoi.isClosed)
				continue;

			String key = aoi.getTags().get("highway");

			if (key.indexOf("link") > -1)
				continue;
			if (OsmTypeDetail.roadMap.containsKey(key) && OsmTypeDetail.roadMap.get(key) != OsmTypeDetail.Road.R1
					&& OsmTypeDetail.roadMap.get(key) != OsmTypeDetail.Road.S3)
				plys.add(aoi.getPly());
		}
	}

	public void calcUniformPoint() {
		pts = new ArrayList<>();
		try {
			pts = StreetAnalysis.getInstance().writeSamplePoint("./data/points(Uniform).csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void calcBlock() {

		polygons = FunctionAnalysis.getInstance().getMapPolygon(plys);
		System.out.println("Total polygons: " + polygons.size());

	}

	public void drawBlock() {
		if (polygons == null)
			return;
		stroke(255);
		for (int i = 0; i < polygons.size(); ++i) {

			int[] co = ColorHelper.hsvToRGB(c[i][0], c[i][1], c[i][2]);
			fill(co[0], co[1], co[2]);
			if (polygons.get(i).getSignedArea() > 100)
				continue;
			tools.render.drawPolygonEdges(polygons.get(i));
		}

	}

	public void readBlock() {
		polygons = FunctionAnalysis.getInstance().getMapPolygonOffline();
		System.out.println("Total polygons: " + polygons.size());
	}

	public void drawDigit(int[] arr) {
		fill(0);
		stroke(0);
		for (int i = 0; i < arr.length; ++i) {
			WB_Point pt = polygons.get(i).getCenter();
			tools.printOnScreen3D("" + arr[i], 10, pt.xd(), pt.yd(), pt.zd());
		}
	}

	public void keyPressed() {

		if (key == 's' || key == 'S') {
			ExportDXF dxf = new ExportDXF();
			for (WB_Polygon plg : polygons) {
				dxf.add(plg, ExportDXF.BROKEN);
			}
			dxf.save("./data/siteblock.dxf");
			System.out.println("Finish export.");

		}

		if (key == 'p' || key == 'P') {
			arr = a[0];
			setColor(arr);
		}

		if (key == 'b' || key == 'B') {
			arr = a[2];
			setColor(arr);
		}

		if (key == 'g' || key == 'G') {
			String filename = "./data/block_gmaps_infomation.csv";
			CsvWriter csvWriter = new CsvWriter(filename, ',', Charset.forName("UTF-8"));

			setHeader();
			for (int i = 0; i < header.length; ++i) {
				System.out.println(header[i]);
			}
			try {
				csvWriter.writeRecord(header);
				calcBlockGmaps(csvWriter);
			} catch (IOException e) {
				e.printStackTrace();
			}

			csvWriter.close();
		}

		if (key == 'y' || key == 'Y') {

			arr = a[1];
			setColor(arr);
		}

		if (key == 'c' || key == 'C') {
			arr = null;
			pts = null;
		}

		if (key == 's' || key == 'S') {
			calcUniformPoint();
		}

		if (key == 'r' || key == 'R') {
			String filename = "./data/block_gmaps_infomation.csv";
			readCSV(filename);
		}
	}

	public void setHeader() {
		header = new String[GmapsTypeDetail.Types.values().length + 4];
		int cnt = 0;
		header[cnt++] = "poi";
		header[cnt++] = "yellow";
		header[cnt++] = "brown";
		header[cnt++] = "green";

		for (Types type : GmapsTypeDetail.Types.values()) {
			header[cnt++] = type.toString();
		}

	}

	public void readCSV(String filename) {
		try {
			CsvReader csvReader = new CsvReader(filename);
			csvReader.readHeaders();
			int tot = 0;
			while (csvReader.readRecord())
				tot++;

			csvReader = new CsvReader(filename);
			csvReader.readHeaders();
			int n = csvReader.getHeaderCount();

			a = new int[n][tot];

			int k = 0;
			while (csvReader.readRecord()) {
				for (int i = 0; i < csvReader.getHeaderCount(); ++i) {
					a[i][k] = Integer.parseInt(csvReader.get(i));
				}
				k++;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void calcBlockGmaps(CsvWriter csvWriter) throws IOException {
		GeoMath geo = new GeoMath(Container.MAP_LAT_LNG);

		pts = new ArrayList<>();

		for (Gpoi gp : Container.gpois) {
			WB_Point pt = new WB_Point(geo.latLngToXY(gp.getLat(), gp.getLng()));
			pts.add(pt);
		}

		for (int i = 0; i < polygons.size(); i++) {
			int poi = 0, yellow = 0, brown = 0, green = 0;

			Map<String, AtomicInteger> map = new HashMap<>();
			for (Types type : GmapsTypeDetail.Types.values()) {
				map.put(type.toString(), new AtomicInteger(0));
			}

			WB_Polygon ply = polygons.get(i);

			WB_AABB aabb = ply.getAABB();
			for (Gpoi gp : Container.gpois) {
				WB_Point pt = new WB_Point(geo.latLngToXY(gp.getLat(), gp.getLng()));
				if (!aabb.contains(pt))
					continue;

				double d = WB_GeometryOp.getDistance3D(pt, WB_GeometryOp.getClosestPoint3D(pt, ply));
				if (d > 1)
					continue;

				poi++;

				if (gp.isChinese())
					yellow++;
				if (gp.isGreen())
					green++;
				if (gp.isBrown())
					brown++;

				String type = gp.getType();
				if (type.indexOf("null") < 0) {
					AtomicInteger count = map.get(type);
					count.incrementAndGet();
					System.out.println("Count = " + count.get());
				}
			}
			System.out.println("ID #" + i + ": " + poi);

			for (Poi op : Container.pois) {
				WB_Point pt = op.getPosition();
				if (!aabb.contains(pt))
					continue;

				double d = WB_GeometryOp.getDistance3D(pt, WB_GeometryOp.getClosestPoint3D(pt, ply));
				if (d > 1)
					continue;

				if (op.isGreen()) {
					green++;
				}
				if (op.isBrown()) {
					brown++;
				}
			}

			// Save data
			String[] content = new String[header.length];
			content[0] = "" + poi;
			content[1] = "" + yellow;
			content[2] = "" + brown;
			content[3] = "" + green;
			int cnt = 4;
			for (Types type : GmapsTypeDetail.Types.values()) {
				AtomicInteger count = map.get(type.toString());
				content[cnt++] = "" + count.get();
			}
			csvWriter.writeRecord(content);
		}

		System.out.println("Finished.");

	}

	public void setColor(int[] arr) {
		int max = 0;
		for (int i = 0; i < arr.length; ++i)
			max = Math.max(max, arr[i]);
		int num = (int) (Math.log1p(max) / Math.log(Tools.RATIO));
		int[][] co = ColorHelper.createGradientHue(num, ColorHelper.RED, ColorHelper.BLUE);

		for (int i = 0; i < arr.length; ++i) {
			if (arr[i] == 0)
				c[i] = new int[] { 255, 255, 255 };
			else
				c[i] = co[num - (int) (Math.log1p(arr[i]) / Math.log(Tools.RATIO))];
		}
	}

}
