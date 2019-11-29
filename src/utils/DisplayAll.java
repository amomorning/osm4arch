package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import controlP5.ControlEvent;
import controlP5.DropdownList;
import controlP5.ListBox;
import crosby.binary.osmosis.OsmosisReader;
import gmaps.GmapsDb;
import osm.GeoMath;
import osm.PbfReader;
import processing.core.PApplet;
import wblut.geom.WB_AABB;
import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;

public class DisplayAll extends PApplet {
	Tools tools;
	public static final int LEN_OF_CAMERA = 5000;

	List<WB_PolyLine> ply;
	List<WB_Point> pts;

	WB_AABB rect = null;
	ListBox l;
	ListBox r;

	public static void main(String[] args) {
		Container.initAll();
		GmapsDb db = new GmapsDb();
		db.collectData();

		try {
			InputStream inputStream = new FileInputStream(Container.OSM_FILENAME);
			OsmosisReader reader = new OsmosisReader(inputStream);
			reader.setSink(new PbfReader());
			reader.run();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		PApplet.main("utils.DisplayAll");

	}

	public void settings() {
		size(1800, 1000, P3D);
	}

	public void setup() {
		tools = new Tools(this, LEN_OF_CAMERA);

		ply = new ArrayList<>();
		rect = new WB_AABB(GeoMath.latLngToXY(Container.SW_LAT_LNG), GeoMath.latLngToXY(Container.NE_LAT_LNG));
		System.out.println(rect);

		l = tools.cp5.addListBox("tagKey").setPosition(100, 100).setSize(240, 240).setItemHeight(30).setBarHeight(30)
				.setColorBackground(color(255, 128)).setColorActive(color(0)).setColorForeground(color(255, 100, 0));

		r = tools.cp5.addListBox("tagValue").setPosition(100, 400).setSize(240, 240).setItemHeight(30).setBarHeight(30)
				.setColorBackground(color(255, 128)).setColorActive(color(0)).setColorForeground(color(255, 100, 0));

		int cnt = 0;
		l.setLabelVisible(true);
		for (String key : Container.tagList.keySet()) {
			l.addItem(key, cnt++).setLabel(key).setColorBackground(color(0, 0, 255));
		}
	}

	public void draw() {
		background(0);
		tools.cam.openLight();
		tools.cam.drawSystem(LEN_OF_CAMERA);
		stroke(255, 0, 0);
//        for (Poi poi : Container.pois) {
//            tools.render.drawPoint(poi.position);
//        }
		noStroke();
		stroke(0, 0, 255);
		tools.render.drawPolylineEdges(ply);

		stroke(255);
		noFill();
		tools.render.drawAABB(rect);

		stroke(0, 255, 0);
		if (pts != null) {
			tools.render.drawPoint(pts);
		}

		tools.drawCP5();
	}


	public void tagKey() {
		String str = l.getInfo();
		System.out.println(str);
	}

	public void keyPressed() {
		if (key == 'a' || key == 'A') {
			ply = new ArrayList<>();
			for (Aoi aoi : Container.aois) {
				ply.add(aoi.ply);
			}
		}
		if (key == 'p' || key == 'P') {
			ply = new ArrayList<>();
			for (Aoi aoi : Container.aois) {
				if (aoi.isPiazza()) {
					ply.add(aoi.ply);
					aoi.printTag();
				}
			}
		}
		if (key == 'b' || key == 'B') {
			ply = new ArrayList<>();
			for (Aoi aoi : Container.aois) {
				if (aoi.isBuilding()) {
					ply.add(aoi.ply);
					aoi.printTag();
				}
			}
		}
		if (key == 'f' || key == 'F') {
			ply = new ArrayList<>();
			for (Aoi aoi : Container.aois) {
				if (aoi.isForest()) {
					ply.add(aoi.ply);
					aoi.printTag();
				}
			}
		}
		if (key == 'i' || key == 'I') {
			ply = new ArrayList<>();
			for (Aoi aoi : Container.aois) {
				if (aoi.isIndustrial()) {
					ply.add(aoi.ply);
					aoi.printTag();
				}
			}
		}
		if (key == 'r' || key == 'R') {
			ply = new ArrayList<>();
			for (Aoi aoi : Container.aois) {
				if (aoi.isResidential()) {
					ply.add(aoi.ply);
					aoi.printTag();
				}
			}
		}

		if (key == 's' || key == 'S') {
			Tools.saveWB_Polyline(ply.toArray(new WB_PolyLine[ply.size()]), "./data/test.3dm");
		}

		if (key == 'g' || key == 'G') {
			pts = new ArrayList<>();
			for (Gpoi g : Container.gpois) {
				double[] pos = GeoMath.latLngToXY(g.getLat(), g.getLng());
				if (g.getType() == "null")
					continue;
				pts.add(new WB_Point(pos));
			}
		}

		if (key == 'h' || key == 'H') {
			ply = new ArrayList<>();
			for (Aoi aoi : Container.aois) {
				if (aoi.isHighway()) {
					ply.add(aoi.ply);
					aoi.printTag();
				}
			}
		}
	}
}