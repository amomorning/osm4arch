package main;

import processing.core.PApplet;
import utils.Aoi;
import utils.Container;

public class Main {

	public static void main(String[] args) {
		Container.OSM_FILENAME = "./data/prato.pbf";
		Container.initAll();
//		PApplet.main("main.DisplayAll");
//		PApplet.main("main.DisplayRoad");
		PApplet.main("main.DisplayBlock");


//		Container.initGmaps();
//		PApplet.main("main.DisplayGrid");
	}

}
