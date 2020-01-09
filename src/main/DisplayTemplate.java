package main;

import java.io.IOException;
import java.util.List;

import evaluate.StreetAnalysis;
import osm.GeoMath;
import processing.core.PApplet;
import utils.Container;
import utils.Tools;
import wblut.geom.WB_AABB;
import wblut.geom.WB_Point;
import wblut.geom.WB_Segment;

public class DisplayTemplate extends PApplet {
	Tools tools;
	public static final int LEN_OF_CAMERA = 50;
	
	List<WB_Point> pts;
	WB_Segment seg;
	GeoMath geoMath = new GeoMath(Container.MAP_LAT_LNG);
	WB_AABB rect = null;
	public void settings() {
		size(1200, 1000, P3D);
	}
	
	public void setup() {
		tools = new Tools(this, LEN_OF_CAMERA);
	
		WB_Point p0 = new WB_Point(0, 0);
		WB_Point p1 = new WB_Point(100, 200);
		seg = new WB_Segment(p0, p1);

		pts = StreetAnalysis.getInstance().distDivideLine(p0, p1);

	}	
	
	public void draw() {
		background(255);
		tools.cam.drawSystem(LEN_OF_CAMERA);

		stroke(255, 0, 0);
		if(pts != null) tools.render.drawPoint(pts, 3);
		
		stroke(0);
		tools.render.drawSegment(seg);

		tools.drawCP5();
	}
	
	public void initGUI() {
		
	}
	
	public void keyPressed() {
	}

}
