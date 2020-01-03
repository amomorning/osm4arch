package main;

import processing.core.PApplet;
import utils.Container;

public class Main {

	public static void main(String[] args) {
		
//		PApplet.main("main.BuildingHighway");
		
		Container.OSM_FILENAME = "./data/prato.pbf";
		Container.initAll();
//		PApplet.main("main.DisplayAll");
//		PApplet.main("main.DisplayRoad");
//		PApplet.main("main.DisplayBlock");
//		PApplet.main("main.DisplayBlockFuntion");
//		PApplet.main("main.DisplayPoint");
		PApplet.main("main.DisplayGmapsPoint");

//		PApplet.main("main.DisplayTemplate");

//		Container.initGmaps();
//		PApplet.main("main.DisplayGrid");

	}
	
	public void test() {
		System.out.println(System.getProperty("user.dir"));
	}

}
