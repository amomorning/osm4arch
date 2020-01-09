package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gmaps.GmapsTypeDetail;
import gmaps.GmapsTypeDetail.Types;
import main.DisplayPoint.vPoint;
import osm.GeoMath;
import processing.core.PApplet;
import utils.Aoi;
import utils.ColorHelper;
import utils.Container;
import utils.Gpoi;
import utils.Tools;
import wblut.geom.WB_AABB;
import wblut.geom.WB_Circle;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;

public class DisplayGmapsPoint extends PApplet {
	Tools tools;
	public static final int LEN_OF_CAMERA = 11000;

	List<WB_Point> pts;
	GeoMath geoMath;
	Types[] types;
	int[][] co;
	List<WB_Polygon> building;

	Map<String, Integer> map;
	String msg = "";
	WB_AABB rect = null;

	public void settings() {
		size(1200, 1000, P3D);

		geoMath = new GeoMath(Container.MAP_LAT_LNG);
		types = GmapsTypeDetail.Types.values();
		System.out.println(types.length);
		co = ColorHelper.createGradientHue(types.length, ColorHelper.RED, ColorHelper.BLUE);

		map = new HashMap<>();
		for (int i = 0; i < co.length; ++i) {
			map.put(types[i].toString(), i);
		}

		building = new ArrayList<>();
		for (Aoi aoi : Container.aois) {
			if (aoi.isBuilding) {
				building.add(Tools.toWB_Polygon(aoi.getPly()));
			}
		}
		rect = new WB_AABB(geoMath.latLngToXY(Container.SW_LAT_LNG), geoMath.latLngToXY(Container.NE_LAT_LNG));

	}

	public void setup() {
		tools = new Tools(this, LEN_OF_CAMERA);
		tools.cam.top();
	}

	public void draw() {
		background(ColorHelper.BACKGROUNDBLUE);
//		tools.cam.drawSystem(LEN_OF_CAMERA);
//		drawTabels();
//		tools.drawCircle(this, 135);
		tools.printOnScreen(msg, 16, 100, 30);
		if (building != null) {
			int[] c = ColorHelper.colorLighter(ColorHelper.BACKGROUNDBLUE, 0.6);
			fill(c[0], c[1], c[2]);
			noStroke();
			tools.render.drawPolygonEdges(building);
		}

		stroke(255, 0, 0);
		for (Gpoi p : Container.gpois) {
			double[] xy = geoMath.latLngToXY(p.getLat(), p.getLng());

			if (p.getType().indexOf("null") > -1)
				continue;

			int id = map.get(p.getType());
			int[] c = co[id];
			WB_Circle cc = new WB_Circle(xy[0], xy[1], 5);
			fill(c[0], c[1], c[2]);
			noStroke();
			tools.render.drawCircle(cc);
		}
		if (pts != null)
			tools.render.drawPoint(pts, 3);

		stroke(0);
		tools.drawCP5();

		stroke(255, 0, 0);
		noFill();
		tools.render.drawAABB(rect);


	}

	public void initGUI() {

	}

	public void keyPressed() {
		if (key == 's' || key == 'S') {
			saveFrame("./img/GmapsPoint.png");
		}
		if (key == 'c' || key == 'C') {
		}
	}

	public void mouseClicked() {
		double[] tmp = tools.cam.getCoordinateFromScreenOnXYPlaneDouble(mouseX, mouseY);
		WB_Point pos = new WB_Point(tmp);
		double min = Double.MAX_VALUE;
		Gpoi pt = null;
		for (Gpoi p : Container.gpois) {
			if (p.getType().equals("null"))
				continue;
			WB_Point wp = new WB_Point(geoMath.latLngToXY(p.getLat(), p.getLng()));
			double dis = WB_Point.getDistance(wp, pos);
			if (dis < min) {
				pt = p;
				min = dis;
			}
		}
		if (pt == null) {
			msg = "Point not found.";
		} else {

			msg = pt.getPlaceid() + "\n" + pt.getLat() + ", " + pt.getLng() + "\n" + pt.getName() + "\n"
					+ pt.getRating() + " (" + pt.getUserRatingsTotal() + ")\n" + pt.getType() + ": "
					+ pt.getTypeDetail();
			System.out.println(msg);
		}

	}

	public void drawTabels() {
		for (int i = 0; i < co.length; ++i) {
			int x = 100;
			int y = tools.app.height - 100 - i * 50;
			tools.cam.begin2d();
			tools.app.noStroke();
			tools.app.fill(co[i][0], co[i][1], co[i][2]);
			tools.app.rect(x, y, 30, 30);

			tools.app.fill(255);
			tools.app.textSize(17);
			tools.app.text("" + types[i], x + 50, y + 20);
			tools.cam.begin3d();
		}
	}
}
