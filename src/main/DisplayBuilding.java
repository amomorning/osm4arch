package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.csvreader.CsvWriter;
import com.google.common.cache.Weigher;

import evaluate.ShapeAnalysis;
import osm.GeoMath;
import processing.core.PApplet;
import utils.Aoi;
import utils.ColorHelper;
import utils.Container;
import utils.Tools;
import wblut.geom.WB_AABB;
import wblut.geom.WB_Coord;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Segment;

public class DisplayBuilding extends PApplet {

	Tools tools;
	public static final int LEN_OF_CAMERA = 11000;
	WB_Point[] pts;

	List<WB_Polygon> buildings;

	GeoMath geoMath = new GeoMath(Container.MAP_LAT_LNG);
	WB_AABB rect = null;
	ShapeAnalysis sa = new ShapeAnalysis();

	public void settings() {
		size(1000, 1000, P3D);
	}

	public void setup() {
		tools = new Tools(this, LEN_OF_CAMERA);
		tools.cam.top();
		rect = new WB_AABB(geoMath.latLngToXY(Container.SW_LAT_LNG), geoMath.latLngToXY(Container.NE_LAT_LNG));

		buildings = new ArrayList<WB_Polygon>();
		for (Aoi aoi : Container.aois) {
			if (aoi.isBuilding)
				buildings.add(Tools.toWB_Polygon(aoi.getPly()));
		}

		sa.shapeIndex(buildings, 8);

		double[][] v = { { 2048.69181597, 3692.17729632 }, { 3058.59400386, -1697.1331551 } };
		pts = new WB_Point[2];
		pts[0] = new WB_Point(v[0]);
		pts[1] = new WB_Point(v[1]);

		print(pts[0] + " " + pts[1]);
	}

	public void draw() {
		background(ColorHelper.BACKGROUNDBLUE);
//		tools.cam.drawSystem(LEN_OF_CAMERA);
//		int[] b = ColorHelper.hexToRGB(ColorHelper.BACKGROUNDBLUE);
//		int[] c = ColorHelper.colorLighter(ColorHelper.BACKGROUNDBLUE, 2);
//		fill(c[0], c[1], c[2]);
//		stroke(b[0], b[1], b[2]);
//		tools.render.drawPolygonEdges(buildings);
//
//		strokeWeight(3);
//		int[][] col = ColorHelper.createGradientHue(3, ColorHelper.RED, ColorHelper.BLUE);
//		for (int i = 0; i < 2; ++i) {
//			stroke(col[i][0], col[i][1], col[i][2]);
//			tools.render.drawSegment(WB_Point.ZERO(), pts[i]);
//		}

		sa.drawShapeIndex(tools);

		stroke(255, 0, 0);
		noFill();
		strokeWeight(1);
		tools.render.drawAABB(rect);
		tools.drawCP5();
	}

	public void keyPressed() {
		if (key == 's' || key == 'S') {
			CsvWriter csv = new CsvWriter("./data/building_vector.csv");
			for (WB_Polygon b : buildings) {
				for (int i = 0; i < b.getNumberSegments(); i += 2) {
					WB_Segment seg = b.getSegment(i);
//					if(seg.getLength() < 50) continue;
					WB_Coord coord = WB_Point.sub(seg.getEndpoint(), seg.getOrigin());
					System.out.println("Seg = " + seg.getLength());
					System.out.println("Coord = " + coord.toString() + " Len = " + WB_Point.getLength(coord));
					System.out.println("--------------------");
					String[] record = { Double.toString(coord.xd()), Double.toString(coord.yd()) };
					try {
						csv.writeRecord(record);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			csv.close();
		}
		
		if(key == 'f' || key == 'F') {
			saveFrame("./img/BuildingAxis.png");
		}
	}

}
