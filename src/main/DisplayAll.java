package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import controlP5.Button;
import controlP5.ControlP5;
import controlP5.DropdownList;
import evaluate.FunctionAnalysis;
import evaluate.ShapeAnalysis;
import gmaps.GmapsTypeDetail;
import gmaps.GmapsTypeDetail.Types;
import osm.GeoMath;
import processing.core.PApplet;
import utils.Aoi;
import utils.Container;
import utils.ExportDXF;
import utils.Gpoi;
import utils.Tools;
import wblut.geom.WB_AABB;
import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;
import wblut.geom.WB_Polygon;

public class DisplayAll extends PApplet {
	Tools tools;
	public static final int LEN_OF_CAMERA = 5000;

	List<WB_Polygon> polygon;
	List<WB_PolyLine> ply;
	List<WB_Point> pts;


	DropdownList aoiKey, aoiValue, poiType;
	Button keyButton, valueButton, typeButton;
	
	FunctionAnalysis functionAnalysis;
	ShapeAnalysis shapeAnalysis;
	GeoMath geoMath = new GeoMath(Container.MAP_LAT_LNG);
	WB_AABB rect = null;

	public void settings() {
		size(1800, 1000, P3D);
	}

	public void setup() {
		functionAnalysis = new FunctionAnalysis();
		shapeAnalysis = new ShapeAnalysis();

		tools = new Tools(this, LEN_OF_CAMERA);

		rect = new WB_AABB(geoMath.latLngToXY(Container.SW_LAT_LNG), geoMath.latLngToXY(Container.NE_LAT_LNG));
		System.out.println(rect);

		
		initGUI();
	}

	public void draw() {
		background(0);
		tools.cam.drawSystem(LEN_OF_CAMERA);
		stroke(255, 0, 0);

		stroke(255);

		if (ply != null) {
			tools.render.drawPolylineEdges(ply);
		}

		stroke(255, 0, 0);
		noFill();
		tools.render.drawAABB(rect);

		stroke(0, 255, 0);
		if (pts != null) {
			tools.render.drawPoint(pts, 2);
//			functionAnalysis.drawGridCount(tools);
		}
		


//		if (polygon != null) shapeAnalysis.drawShapeIndex(tools);

		tools.drawCP5();
	}

	public void initGUI() {

		aoiKey = tools.cp5.addDropdownList("tagKey").setPosition(100, 100).setSize(240, 240).setItemHeight(30)
				.setBarHeight(30).setColorBackground(color(255, 128)).setColorActive(color(0))
				.setColorForeground(color(255, 100, 0));

		aoiKey.getCaptionLabel().toUpperCase(true);
		aoiKey.getCaptionLabel().setColor(0xffff0000);
		aoiKey.getCaptionLabel().getStyle().marginTop = 3;
		aoiKey.getValueLabel().getStyle().marginTop = 3;

		aoiValue = tools.cp5.addDropdownList("tagValue").setPosition(400, 100).setSize(240, 240).setItemHeight(30)
				.setBarHeight(30).setColorBackground(color(255, 128)).setColorActive(color(0))
				.setColorForeground(color(255, 100, 0));

		aoiValue.getCaptionLabel().toUpperCase(true);
		aoiValue.getCaptionLabel().setColor(0xffff0000);
		aoiValue.getCaptionLabel().getStyle().marginTop = 3;
		aoiValue.getValueLabel().getStyle().marginTop = 3;

		poiType = tools.cp5.addDropdownList("poiType").setPosition(700, 100).setSize(240, 240).setItemHeight(30)
				.setBarHeight(30).setColorBackground(color(255, 128)).setColorActive(color(0))
				.setColorForeground(color(255, 100, 0));

		poiType.getCaptionLabel().toUpperCase(true);
		poiType.getCaptionLabel().setColor(0xffff0000);
		poiType.getCaptionLabel().getStyle().marginTop = 3;
		poiType.getValueLabel().getStyle().marginTop = 3;

		int cnt = 0;
//		l.continuousUpdateEvents();
//		r.continuousUpdateEvents();
		String[] keys = Container.tagList.keySet().toArray(new String[Container.tagList.keySet().size()]);
		Arrays.sort(keys);

		for (String key : keys) {
			aoiKey.addItem(key, cnt++).setLabel(key).setColorBackground(color(0, 0, 255));
		}

		keyButton = tools.cp5.addButton("keyControl").setPosition(100, 80);
		valueButton = tools.cp5.addButton("valueControl").setPosition(400, 80);
		typeButton = tools.cp5.addButton("typeControl").setPosition(700, 80);

		for (Types type : GmapsTypeDetail.Types.values()) {
			String str = type.toString();
//			System.out.println(str);
			poiType.addItem(str, cnt++).setLabel(str).setColorBackground(color(0, 0, 255));
		}

	}

	public void keyControl() {
		aoiValue.clear();
		String str = aoiKey.getLabel();
		System.out.println(str);
		int cnt = 0;
		for (String value : Container.tagList.get(str)) {
			System.out.println(value);
			aoiValue.addItem(value, cnt++).setLabel(value).setColorBackground(color(0, 0, 255));
		}
		aoiValue.addItem("all", cnt++).setLabel("all").setColorBackground(color(0, 0, 255));
		aoiValue.update();
	}

	public void valueControl() {
		String key = aoiKey.getLabel();
		String value = aoiValue.getLabel();

//		System.out.println(key + " " + value);
		ply = new ArrayList<>();
		polygon = new ArrayList<>();
		for (Aoi aoi : Container.aois) {
			for (String aoiKey : aoi.getTags().keySet()) {
				if (aoiKey.equals(key) && value.equals("all")) {
					if(aoi.isClosed) polygon.add(Tools.toWB_Polygon(aoi.getPly()));
					ply.add(aoi.getPly());
					aoi.printTag();
					continue;
				}
				if (aoiKey.equals(key) && aoi.getTags().get(aoiKey).equals(value)) {
					if(aoi.isClosed) polygon.add(Tools.toWB_Polygon(aoi.getPly()));
					ply.add(aoi.getPly());
					aoi.printTag();
				}
			}
		}
	
		System.out.println(ply.get(0).getPoint(0));
		System.out.println(ply.get(0).getPoint(1));
//		shapeAnalysis.shapeIndex(polygon, 8);
	}

	public void typeControl() {
		pts = new ArrayList<>();
		String label = poiType.getLabel();
//		System.out.println(label);
		for (Gpoi g : Container.gpois) {
//			System.out.println(g.getType() + " " + label + " " + g.getType().equalsIgnoreCase(label));
			String type = g.getType();

			if (type.equals(label)) {
				double[] pos = geoMath.latLngToXY(g.getLat(), g.getLng());
				pts.add(new WB_Point(pos));
			}
		}

//		functionAnalysis.gridCount(Tools.toPoint3D(pts));
	}

	public void keyPressed() {
		if (key == 'a' || key == 'A') {
			ply = new ArrayList<>();
			for (Aoi aoi : Container.aois) {
				ply.add(aoi.getPly());
			}
		}


		if (key == 's' || key == 'S') {
			String filePath = "./data/" + aoiKey.getLabel() + "_" + aoiValue.getLabel() + ".dxf";
			ExportDXF dxf = new ExportDXF();
			for(WB_PolyLine pl : ply) {
				dxf.add(pl, ExportDXF.BROKEN);
			}
			dxf.save(filePath);
		}

		if (key == 'g' || key == 'G') {
			pts = new ArrayList<>();
			for (Gpoi g : Container.gpois) {
				double[] pos = geoMath.latLngToXY(g.getLat(), g.getLng());

				pts.add(new WB_Point(pos));
			}

		}
		
		if (key == 'i' || key == 'I') {
			polygon = new ArrayList<>();
			for(Aoi aoi : Container.aois) {
				if(aoi.isClosed && aoi.isBuilding) {
					polygon.add(Tools.toWB_Polygon(aoi.getPly()));
				}
			}
		}

	}
}