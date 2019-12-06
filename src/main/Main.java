package main;

import processing.core.PApplet;
import utils.Container;

public class Main {

	public static void main(String[] args) {
		Container.initAll();
		PApplet.main("main.DisplayAll");
		PApplet.main("main.DisplayController");
//		PApplet.main("main.DisplayRoad");
		
		
//		Container.initGmaps();
//		PApplet.main("main.DisplayGrid");
	}

}
