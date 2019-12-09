package main;

import processing.core.PApplet;
import utils.Aoi;
import utils.Container;

public class Main {

	public static void main(String[] args) {
		Container.initAll();
		PApplet.main("main.DisplayAll");
		PApplet.main("main.DisplayRoad");
//		PApplet.main("main.DisplayBlock");


//		Container.initOsm();
//		for(Aoi aoi : Container.aois) {
//			if(aoi.isBuilding) aoi.getPly();
//		}

//		Container.initGmaps();
//		PApplet.main("main.DisplayGrid");
	}

}
