package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.csvreader.CsvReader;

import Guo_Cam.Vec_Guo;
import evaluate.StreetAnalysis;
import osm.GeoMath;
import osm.OsmTypeDetail;
import processing.core.PApplet;
import processing.core.PImage;
import processing.opengl.PGraphics3D;
import utils.Aoi;
import utils.ColorHelper;
import utils.Container;
import utils.Tools;
import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;
import wblut.geom.WB_Polygon;

public class DisplayPoint extends PApplet {

	List<vPoint> vpt;
	List<WB_Point> pts;
	List<WB_PolyLine> plys;
	List<WB_Polygon> building;
	List<int[]> co;
	Tools tools;
	PImage img, emg;
	public static final int LEN_OF_CAMERA = 5200;

	public void settings() {
		size(3200, 1800, P3D);
	}

	public void setup() {

		tools = new Tools(this, LEN_OF_CAMERA);
		tools.cam.top();
		tools.cam.getCamera().setPosition(new Vec_Guo(-1800, -220, LEN_OF_CAMERA));
		tools.cam.getCamera().setLookAt(new Vec_Guo(-1800, -220, 0));
		seivePolyline();

		readEvaluetedPointsFromCSV("./data/evaluate_sample_img.csv");
		building = new ArrayList<>();
		for (Aoi aoi : Container.aois) {
			if (aoi.isBuilding) {
				building.add(Tools.toWB_Polygon(aoi.getPly()));
			}
		}

	}

	public void draw() {
		background(ColorHelper.BACKGROUNDBLUE);
//		tools.cam.drawSystem(LEN_OF_CAMERA);
		if (building != null) {
			int[] c = ColorHelper.colorLighter(ColorHelper.BACKGROUNDBLUE, 0.6);
			fill(c[0], c[1], c[2]);
			noStroke();
			tools.render.drawPolygonEdges(building);
		}
		if (pts != null) {
			for (int i = 0; i < pts.size(); ++i) {
				int r = 10;
				if(i < 5) r = 50;
				if(i >= pts.size() - 5) r = 50;
				tools.drawPoint(pts.get(i), r, co.get(i));
			}
		}

		if (img != null) {
			tools.cam.begin2d();
			tools.app.image(img, 250, 300, 300, 300);
			tools.app.image(emg, 550, 300, 400, 300);
			tools.cam.begin3d();
		}

		for (int i = 0; i < 5; ++i) {
			displayImage(vpt.get(i).getImgname(), 30, 1800 - 200 * i - 150 - 30);
			displayImage(vpt.get(vpt.size() - i - 1).getImgname(), 2620, 200 * i + 30);
		}

		stroke(0);
		tools.drawCP5();
	}

	public void displayImage(String imgname, float x, float y) {
		tools.cam.begin2d();
		PImage img = loadImage("E:\\evaluate_img\\" + imgname + ".jpg");
		PImage emg = loadImage("E:\\evaluate_img\\" + imgname + ".png");
		tools.app.image(img, x, y, 150, 150);
		tools.app.image(emg, x + 150, y, 200, 150);
		tools.cam.begin3d();
	}

	public void initGUI() {

	}

	public void keyPressed() {
		if (key == 'r' || key == 'R') {
		}
		if (key == 'b' || key == 'B') {

		}
		if (key == 's' || key == 'S') {

			System.out.println(tools.cam.getCamera().getPosition());
			System.out.println(tools.cam.getCamera().getLookAt());
			saveFrame("./img/PointImage.png");
		}

	}

	public void readEvaluetedPointsFromCSV(String filename) {
		try {
			CsvReader reader = new CsvReader(filename);
			GeoMath geoMath = new GeoMath(Container.MAP_LAT_LNG);

			pts = new ArrayList<>();
			co = new ArrayList<>();
			vpt = new ArrayList<>();

			reader.readHeaders();
			while (reader.readRecord()) {
				double lat = Double.parseDouble(reader.get("lat"));
				double lng = Double.parseDouble(reader.get("lng"));
				double[] xy = geoMath.latLngToXY(lat, lng);

				vPoint pt = new vPoint(xy[0], xy[1]);
				pt.imgname = reader.get("img_id");
				pt.beautiful = Double.parseDouble(reader.get("beautiful"));
				pt.boring = Double.parseDouble(reader.get("boring"));
				pt.depressing = Double.parseDouble(reader.get("depressing"));
				pt.lively = Double.parseDouble(reader.get("lively"));
				pt.safety = Double.parseDouble(reader.get("safety"));
				pt.wealthy = Double.parseDouble(reader.get("wealthy"));
				pt.score = Double.parseDouble(reader.get("score"));

				vpt.add(pt);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		int num = 10;

		double step = 1.0 / num;
		int[][] c = ColorHelper.createGradientHue(10, ColorHelper.RED, ColorHelper.BLUE);

		Collections.sort(vpt);
		for (vPoint pt : vpt) {
			pts.add(pt.getWB_Point());
			co.add(c[(int) (pt.score / step)]);
		}
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

	public void loadImg(String name) {
		img = loadImage(name);
	}

//	public void mouseClicked() {
//		if (img != null) {
//			img = null;
//			return;
//		}
//		if (vpt == null)
//			return;
//		double[] tmp = tools.cam.getCoordinateFromScreenOnXYPlaneDouble(mouseX, mouseY);
//		WB_Point pos = new WB_Point(tmp);
//		double min = Double.MAX_VALUE;
//		vPoint pt = null;
//		for (vPoint p : vpt) {
//			double dis = WB_Point.getDistance(p.getWB_Point(), pos);
//			if (dis < min) {
//				pt = p;
//				min = dis;
//			}
//		}
//		if (pt == null) {
//			System.out.println("Point not find.");
//		} else {
//			img = loadImage("E:\\evaluate_img\\" + pt.imgname + ".jpg");
//			emg = loadImage("E:\\evaluate_img\\" + pt.imgname + ".png");
//		}
//	}

	class vPoint implements Comparable<vPoint> {
		public String imgname;
		public double x, y, z;
		public double beautiful, boring, depressing, lively, safety, wealthy, score;

		public vPoint(double x, double y) {
			this.x = x;
			this.y = y;
		}

		public WB_Point getWB_Point() {
			return new WB_Point(x, y);
		}

		public String getImgname() {
			return imgname;
		}

		public void setImgname(String imgname) {
			this.imgname = imgname;
		}

		public double getBeautiful() {
			return beautiful;
		}

		public void setBeautiful(double beautiful) {
			this.beautiful = beautiful;
		}

		public double getBoring() {
			return boring;
		}

		public void setBoring(double boring) {
			this.boring = boring;
		}

		public double getDepressing() {
			return depressing;
		}

		public void setDepressing(double depressing) {
			this.depressing = depressing;
		}

		public double getLively() {
			return lively;
		}

		public void setLively(double lively) {
			this.lively = lively;
		}

		public double getSafety() {
			return safety;
		}

		public void setSafety(double safety) {
			this.safety = safety;
		}

		public double getWealthy() {
			return wealthy;
		}

		public void setWealthy(double wealthy) {
			this.wealthy = wealthy;
		}

		public double getScore() {
			return score;
		}

		public void setScore(double score) {
			this.score = score;
		}

		@Override
		public int compareTo(vPoint o) {
			if (this.score > o.score)
				return 1;
			return -1;
		}

	}

}
