package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import evaluate.FunctionAnalysis;
import osm.OsmTypeDetail;
import processing.core.PApplet;
import utils.Aoi;
import utils.Container;
import utils.Tools;
import wblut.geom.WB_PolyLine;
import wblut.geom.WB_Polygon;

public class DisplayBlock extends PApplet {

	List<WB_Polygon> polygon;
	List<WB_PolyLine> plys;
	Tools tools;
	public static final int LEN_OF_CAMERA = 5000;

	Random rand = new Random(233);
	int[] c;

	public void settings() {
		size(1200, 1000, P3D);
		c = new int[11000];
		for (int i = 0; i < 11000; ++i) {
			c[i] = rand.nextInt(255);
		}
	}

	public void setup() {
		FunctionAnalysis fa = new FunctionAnalysis();
		tools = new Tools(this, LEN_OF_CAMERA);

		plys = new ArrayList<>();
		for (Aoi aoi : Container.aois) {
			if (!aoi.isHighway)
				continue;
			String key = aoi.getTags().get("highway");
			if (OsmTypeDetail.roadMap.containsKey(key) && OsmTypeDetail.roadMap.get(key) != OsmTypeDetail.Road.R1
					&& OsmTypeDetail.roadMap.get(key) != OsmTypeDetail.Road.S3)
				plys.add(aoi.getPly());
		}

		polygon = fa.getMapPolygon(plys);

		System.out.println("Total generated polygon number: " + polygon.size());

		initGUI();
		colorMode(HSB);
	}

	public void draw() {
		background(255);
		tools.cam.drawSystem(LEN_OF_CAMERA);

		stroke(255);
//		System.out.println(polygon.size());
		for (int i = 0; i < polygon.size(); ++i) {
			fill(c[i], 100, 200);
//			System.out.println("number = " + polygon.get(i).getNumberOfPoints() );
			if(polygon.get(i).getSignedArea() > 100) continue;
			tools.render.drawPolygonEdges(polygon.get(i));
		}

		stroke(0);
		tools.render.drawPolylineEdges(plys);
		tools.drawCP5();
	}

	public void initGUI() {

	}

	public void keyPressed() {
	}

}
