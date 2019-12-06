package main;

import java.util.ArrayList;
import java.util.List;

import osm.OsmTypeDetail;
import osm.OsmTypeDetail.Road;
import processing.core.PApplet;
import utils.Aoi;
import utils.ColorHelper;
import utils.Container;
import utils.Tools;
import wblut.geom.WB_PolyLine;

public class DisplayRoad extends PApplet {

	Tools tools;
	public static final int LEN_OF_CAMERA = 5000;

	List<WB_PolyLine> plys;
	List<int[]> color;

	public void settings() {
		size(1600, 1000, P3D);
	}

	public void setup() {
		tools = new Tools(this, LEN_OF_CAMERA);

		plys = new ArrayList<>();
		color = new ArrayList<>();
		for (Aoi aoi : Container.aois) {
			if (!aoi.getTags().containsKey("highway"))
				continue;

			String key = aoi.getTags().get("highway");
			if (OsmTypeDetail.roadMap.containsKey(key)) {
				Road road = OsmTypeDetail.roadMap.get(key);
				plys.add(aoi.getPly());
				color.add(OsmTypeDetail.roadColor.get(road));
			} else {
//				aoi.printTag();
				plys.add(aoi.getPly());
				color.add(new int[] { 255, 255, 255 });
			}
		}
		tools.cam.top();
	}

	public void draw() {
		background(ColorHelper.BACKGROUNDBLUE);
//		tools.cam.drawSystem(LEN_OF_CAMERA);


		drawRoads();

		drawLabels();

		tools.drawCP5();
	}

	private void drawRoads() {
		for(int i = 0; i < plys.size(); ++ i) {
			noFill();
			int c[] = color.get(i);
			stroke(c[0], c[1], c[2]);
			if(c[0] == 255 && c[1] == 255 && c[2] == 255) strokeWeight(1);
			else if(c[1] == 102 && c[2] == 102) strokeWeight(4);
			else if(c[1] == 255) strokeWeight(2);
			else strokeWeight(1);
			tools.render.drawPolylineEdges(plys.get(i));
		}
	}

	private void drawLabels() {
		tools.cam.begin2d();
		int cnt = 0;
		noStroke();
		for (Road road : Road.values()) {
			int x = 100;
			int y = height - 100 - 50 * cnt;
			int[] c = OsmTypeDetail.roadColor.get(road);
			fill(c[0], c[1], c[2]);
			rect(x, y, 30, 30);

			String[] strs = road.getString();
			String str = strs[0];
			for(int i = 1; i < strs.length; ++ i) {
				str += ", " + strs[i];
			}
			fill(255);
			textSize(17);
			text(str, x + 50, y + 20);
			text(str, x + 50, y + 20);
			cnt++;
		}
		tools.cam.begin3d();
	}

	public void keyPressed() {
		if(key == 'p' || key == 'P') {
			tools.cam.perspective();
		}
		if(key == 't' || key == 'T') {
			tools.cam.top();
		}
	}

}
