package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.csvreader.CsvReader;

import Guo_Cam.Vec_Guo;
import evaluate.FunctionAnalysis;
import osm.GeoMath;
import processing.core.PApplet;
import utils.ColorHelper;
import utils.Container;
import utils.Tools;
import wblut.geom.WB_AABB;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;

public class DisplayStreetBlock extends PApplet{
	Tools tools;
	public static final int LEN_OF_CAMERA = 11000;
	
	List<WB_Point> pts;
	List<WB_Polygon> polygons;
	List<int[]> co;
	List<vPoint> vpt;
	int[] arr;
	double[] sco;
	
	double[] area;
	int[][] c;
	
	int cnt;
	
	
	GeoMath geoMath = new GeoMath(Container.MAP_LAT_LNG);
	WB_AABB rect = null;
	public void settings() {
		size(1200, 1000, P3D);
	}
	
	public void setup() {
		tools = new Tools(this, LEN_OF_CAMERA);
		tools.cam.top();
		tools.cam.getCamera().setPosition(new Vec_Guo(-293.33333333333337, -513.3333333333331, 11000.0));
		tools.cam.getCamera().setLookAt(new Vec_Guo(-293.33333333333337, -513.3333333333331, 0));

		readBlock();
		readEvaluetedPointsFromCSV("./data/evaluate_block_img.csv");
		setBlockColor();
	}	
	
	public void draw() {
		background(ColorHelper.BACKGROUNDBLUE);
//		tools.cam.drawSystem(LEN_OF_CAMERA);

		stroke(255, 0, 0);
		
//		drawVPoint();
		drawBlock();
		
		stroke(0);

		tools.drawCP5();
	}
	
	public void keyPressed() {
		if(key == 's' || key == 'S') {
			System.out.println(tools.cam.getCamera().getPosition());
			System.out.println(tools.cam.getCamera().getLookAt());
			saveFrame("./img/block_" + ("score") + ".png" );
		}

	}
	
	public void readEvaluetedPointsFromCSV(String filename) {
		try {
			CsvReader reader = new CsvReader(filename);
			GeoMath geoMath = new GeoMath(Container.MAP_LAT_LNG);

			pts = new ArrayList<>();
			co = new ArrayList<>();
			vpt = new ArrayList<>();
			
			arr = new int[polygons.size()];
			sco = new double[polygons.size()];
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
				
				int id = (int) Double.parseDouble(reader.get("polyid"));
				

				arr[id] ++;
				sco[id] += pt.score;

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
	
	public void drawVPoint() {
		int[] b = ColorHelper.hexToRGB(ColorHelper.BACKGROUNDBLUE);
		tools.app.stroke(b[0], b[1], b[2]);
		for(int i = 0; i < vpt.size(); ++ i) {
			tools.drawPoint(vpt.get(i).getWB_Point(), 10, co.get(i));
		}
	}
	

	public void drawBlock() {
		if (polygons == null)
			return;
		int[] b = ColorHelper.hexToRGB(ColorHelper.BACKGROUNDBLUE);
		tools.app.stroke(b[0], b[1], b[2]);
		for (int i = 0; i < polygons.size(); ++i) {

			tools.app.fill(c[i][0], c[i][1], c[i][2]);
			if (polygons.get(i).getSignedArea() > 100)
				continue;
			tools.render.drawPolygonEdges(polygons.get(i));
		}

	}

	public void readBlock() {
		polygons = FunctionAnalysis.getInstance().getMapPolygonOffline();
		System.out.println("Total polygons: " + polygons.size());
		area = new double[polygons.size()];
		for(int i = 0; i < polygons.size(); ++ i) {
			area[i] = Math.abs(polygons.get(i).getSignedArea());
		}
	}
	
	public void setBlockColor() {
		
		double step = 1.0/ 10;
		int[][] col = ColorHelper.createGradientHue(10, ColorHelper.RED, ColorHelper.BLUE);
		
		c = new int[polygons.size()][3];

	 	for(int i = 0; i < polygons.size(); ++ i) {
	 		double s = sco[i] / arr[i];
	 		c[i] = col[(int) (s/step)];
	 	}
	}

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
