package main;

import java.util.ArrayList;
import java.util.List;

import controlP5.Button;
import controlP5.DropdownList;
import evaluate.FunctionAnalysis;
import evaluate.ShapeAnalysis;
import gmaps.GmapsTypeDetail;
import gmaps.GmapsTypeDetail.Types;
import osm.GeoMath;
import processing.core.PApplet;
import utils.ColorHelper;
import utils.Container;
import utils.Gpoi;
import utils.Tools;
import wblut.geom.WB_AABB;
import wblut.geom.WB_Point;

public class DisplayGrid extends PApplet {
	Tools tools;
	public static final int LEN_OF_CAMERA = 5000;

	FunctionAnalysis functionAnalysis;
	ShapeAnalysis shapeAnalysis;
	private WB_AABB rect;

	private DropdownList  poiType;
	private Button typeButton;
	private List<WB_Point> pts;
	
	public void settings() {
		size(1200, 1000, P3D);
	}

	public void setup() {
		GeoMath geoMath = new GeoMath(Container.MAP_LAT_LNG);
		tools = new Tools(this, LEN_OF_CAMERA);

		functionAnalysis = new FunctionAnalysis();
		rect = new WB_AABB(geoMath.latLngToXY(Container.SW_LAT_LNG), geoMath.latLngToXY(Container.NE_LAT_LNG));

		pts = new ArrayList<>();
		for (Gpoi g : Container.gpois) {
			double[] pos = geoMath.latLngToXY(g.getLat(), g.getLng());
			pts.add(new WB_Point(pos));
		}

		functionAnalysis.gridCount(Tools.toPoint3D(pts));
		
		initGUI();
	}
	
	public void draw() {
		background(255);
		tools.cam.drawSystem(LEN_OF_CAMERA);
		stroke(255, 0, 0);

		stroke(255, 0, 0);
		noFill();
		tools.render.drawAABB(rect);
		
		noStroke();
		functionAnalysis.drawGridCount(tools);
	
		tools.drawCP5();
	}

	public void initGUI() {

		poiType = tools.cp5.addDropdownList("poiType").setPosition(100, 100).setSize(240, 240).setItemHeight(30)
				.setBarHeight(30).setColorBackground(color(255, 128)).setColorActive(color(0))
				.setColorForeground(color(255, 100, 0));

		poiType.getCaptionLabel().toUpperCase(true);
		poiType.getCaptionLabel().setColor(0xffff0000);
		poiType.getCaptionLabel().getStyle().marginTop = 3;
		poiType.getValueLabel().getStyle().marginTop = 3;
		
		typeButton = tools.cp5.addButton("typeControl").setPosition(100, 80);
		
		int c[][] = ColorHelper.createGradientHue(GmapsTypeDetail.Types.values().length + 1,
				ColorHelper.RED, ColorHelper.BLUE);
		int cnt = 0;
		for (Types type : GmapsTypeDetail.Types.values()) {
			String str = type.toString();
			poiType.addItem(str, cnt).setLabel(str).setColorBackground(color(c[cnt][0], c[cnt][1], c[cnt][2]));
			cnt ++ ;
		} 
		poiType.addItem("all", cnt ++).setLabel("all");
		

	}
	
	public void typeControl() {
		
		pts = new ArrayList<>();
		String label = poiType.getLabel();
		for (Gpoi g : Container.gpois) {
			String type = g.getType();
			if (type.equals(label) || label.equals("all")) {
				double[] pos = new GeoMath(Container.MAP_LAT_LNG).latLngToXY(g.getLat(), g.getLng());
				pts.add(new WB_Point(pos));
			}
		}

		functionAnalysis.gridCount(Tools.toPoint3D(pts));
	}
}
