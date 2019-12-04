package main;

import processing.core.PApplet;
import utils.Tools;

public class DisplayTemplate extends PApplet {
	Tools tools;
	public static final int LEN_OF_CAMERA = 5000;

	public void settings() {
		size(1200, 1000, P3D);
	}
	
	public void setup() {
		tools = new Tools(this, LEN_OF_CAMERA);
		
		
		initGUI();
	}	
	
	public void draw() {
		background(255);
		tools.cam.drawSystem(LEN_OF_CAMERA);

		tools.drawCP5();
	}
	
	public void initGUI() {
		
	}

}
