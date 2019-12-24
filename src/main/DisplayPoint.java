package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.csvreader.CsvReader;

import evaluate.StreetAnalysis;
import osm.GeoMath;
import osm.OsmTypeDetail;
import processing.core.PApplet;
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
	public static final int LEN_OF_CAMERA = 5000;

	public void settings() {
		size(1200, 1000, P3D);
	}

	public void setup() {
		tools = new Tools(this, LEN_OF_CAMERA);

		seivePolyline();

	}

	public void draw() {
		background(ColorHelper.BACKGROUNDBLUE);
		tools.cam.drawSystem(LEN_OF_CAMERA);
		if (building != null) {
			int[] c = ColorHelper.colorLighter(ColorHelper.BACKGROUNDBLUE, 0.6);
			fill(c[0], c[1], c[2]);
			noStroke();
			tools.render.drawPolygonEdges(building);
		}
		if (pts != null) {
			for (int i = 0; i < pts.size(); ++i) {
				tools.drawPoint(pts.get(i), 10, co.get(i));
			}
		}

		stroke(0);
		tools.drawCP5();
	}

	public void initGUI() {

	}

	public void keyPressed() {
		if (key == 'r' || key == 'R') {
			readEvaluetedPointsFromCSV("./data/evaluate_sample_img.csv");
		}
		if (key == 'b' || key == 'B') {
			building = new ArrayList<>();
			for (Aoi aoi : Container.aois) {
				if (aoi.isBuilding) {
					building.add(Tools.toWB_Polygon(aoi.getPly()));
				}
			}
		}

	}

	public void readEvaluetedPointsFromCSV(String filename) {
		try {
			CsvReader reader = new CsvReader(filename);
			GeoMath geoMath = new GeoMath(Container.MAP_LAT_LNG);

			pts = new ArrayList<>();
			co = new ArrayList<>();
			vpt = new ArrayList<>();

			int num = 10;

			double step = 1.0 / num;
			int[][] c = ColorHelper.createGradientHue(10, ColorHelper.RED, ColorHelper.BLUE);

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

				pts.add(pt.toWB_Point());
				vpt.add(pt);
				co.add(c[(int) (pt.score / step)]);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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

	public void mouseClicked() {
		if(vpt == null) return;
		double[] tmp = tools.cam.getCoordinateFromScreenOnXYPlaneDouble(mouseX, mouseY);
		WB_Point pos = new WB_Point(tmp);
		double min = Double.MAX_VALUE;
		vPoint pt = null;
		for(vPoint p : vpt) {
			double dis = WB_Point.getDistance(p, pos);
			if(dis < min) {
				pt = p;
				min = dis;
			}
		}
		if(pt == null) {System.out.println("Point not find."); }
		else {
			System.out.println("img name :" +  pt.imgname);
		}
	}
	
	

	class vPoint extends WB_Point {
		public String imgname;
		public double beautiful, boring, depressing, lively, safety, wealthy, score;

		public vPoint(double x, double y) {
			super(x, y);
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

		public WB_Point toWB_Point() {
			return new WB_Point(this);
		}

	}

}
