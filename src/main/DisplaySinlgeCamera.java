package main;

import java.util.ArrayList;
import java.util.List;

import Guo_Cam.Vec_Guo;
import evaluate.FunctionAnalysis;
import gmaps.GmapsTypeDetail;
import osm.GeoMath;
import processing.core.PApplet;
import utils.Aoi;
import utils.Container;
import utils.Gpoi;
import utils.Tools;
import wblut.geom.WB_AABB;
import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Segment;

public class DisplaySinlgeCamera extends PApplet {

	Tools tools;
	public static final int LEN_OF_CAMERA = 10000;
	
	List<WB_Polygon> polygons;
	List<WB_PolyLine> polylines;
	List<WB_Point> points;
	WB_Segment seg;
	GeoMath geoMath = new GeoMath(Container.MAP_LAT_LNG);
	WB_AABB rect = null;
	String tag;

	public void settings() {
		size(3200, 1800, P3D);
	}
	
	public void setup() {
		tools = new Tools(this, LEN_OF_CAMERA);
		tools.cam.iso();
		tools.cam.getCamera().setPosition(new Vec_Guo(-4255.766012592976, -2653.3777829515316, 2222.490489699794));
		tools.cam.getCamera().setLookAt(new Vec_Guo(-1211.8879175115399, 390.50031212989904, -821.3876053816383));
		
		rect = new WB_AABB(geoMath.latLngToXY(Container.SW_LAT_LNG), geoMath.latLngToXY(Container.NE_LAT_LNG));


	}	
	
	public void draw() {
		background(255);
		stroke(0);
		fill(0);

		if(polylines != null)
			tools.render.drawPolylineEdges(polylines);


		if(polygons != null) { 
			stroke(255);
			tools.render.drawPolygonEdges(polygons);
		}

		if(points != null)
			tools.render.drawPoint(points, 3);
		tools.drawCP5();
	}
	
	
	public void keyPressed() {
		if(key == 'f' || key == 'F') {
			System.out.println(tools.cam.getCamera().getPosition());
			System.out.println(tools.cam.getCamera().getLookAt());
			
			saveFrame("./img/P_" + tag + ".png");
		}
		
		if(key == 'a' || key == 'A') {
			initAll();
			for(Aoi aoi : Container.aois) {
				polylines.add(aoi.getPly());
			}
			tag = "all";
		}
		
		if(key == 'p' || key == 'P') {
			initAll();
			for(Gpoi g : Container.gpois) {
				double[] pos = geoMath.latLngToXY(g.getLat(), g.getLng());
				points.add(new WB_Point(pos));
			}
			tag = "allpt";
		}
		
		if(key == 'b' || key == 'B') {
			initAll();
			for(Aoi aoi : Container.aois) {
				if(aoi.isBuilding)
					polygons.add(Tools.toWB_Polygon(aoi.getPly()));
			}
			
			tag = "building";
		}
		
		if(key == 'h' || key == 'H') {
			initAll();
			
			for(Aoi aoi: Container.aois) {
				if(aoi.isHighway)
					polylines.add(aoi.getPly());
			}
			tag = "highway";
		}
		
		if(key == 'k' || key =='K') {
			initAll();
			polygons = FunctionAnalysis.getInstance().getMapPolygonOffline();
			tag = "block";
		}
		
		
	}

	public void initAll() {
		polygons = new ArrayList<>();
		polylines = new ArrayList<>();
		points = new ArrayList<>();
	}

}
