package main;

import java.io.IOException;
import java.util.List;

import evaluate.StreetAnalysis;
import processing.core.PApplet;
import utils.Tools;
import wblut.geom.WB_Point;

public class DisplayTemplate extends PApplet {
	Tools tools;
	public static final int LEN_OF_CAMERA = 5000;
	
	List<WB_Point> pts;

	public void settings() {
		size(1200, 1000, P3D);
	}
	
	public void setup() {
		tools = new Tools(this, LEN_OF_CAMERA);
	
		try {
			pts = StreetAnalysis.writeSamplePoint("./data/points.csv");
			System.out.println("Number of points:" + pts.size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		initGUI();
	}	
	
	public void draw() {
		background(255);
		tools.cam.drawSystem(LEN_OF_CAMERA);

		stroke(255, 0, 0);
		if(pts != null) tools.render.drawPoint(pts, 3);
		tools.drawCP5();
	}
	
	public void initGUI() {
		
	}
	
	public void keyPressed() {
	}

}
