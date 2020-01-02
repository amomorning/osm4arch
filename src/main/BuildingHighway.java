package main;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import readDXF.DXFImport;
import utils.Tools;
import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;

public class BuildingHighway extends PApplet {
	public static final int LEN_OF_CAMERA = 5000;
	Tools tools;
	
	List<WB_PolyLine> highways;
	List<WB_PolyLine> buildings;

	public void settings() {
		size(1200, 1000, P3D);
	}
	
	public void setup() {
		tools = new Tools(this, LEN_OF_CAMERA);
	
		
		highways = getDXFPolyLine("./data/highway_all.dxf");
		buildings = getDXFPolyLine("./data/building_all.dxf");
	}	
	
	public void draw() {
		background(255);
		tools.cam.drawSystem(LEN_OF_CAMERA);
		
		if(highways != null) 
			tools.render.drawPolylineEdges(highways);
		if(buildings != null) 
			tools.render.drawPolylineEdges(buildings);

		tools.drawCP5();
	}
	
	public List<WB_PolyLine> getDXFPolyLine(String filename) {
		double[][][] polys = DXFImport.polylines_layer(filename, "brokenLine");
		List<WB_PolyLine> polylines = new ArrayList<>();
		

		for (int i = 0; i < polys.length; ++i) {
			WB_Point[] pts = new WB_Point[polys[i].length];
			for (int j = 0; j < polys[i].length; ++j) {
				pts[j] = new WB_Point(polys[i][j]);
			}
			polylines.add(new WB_PolyLine(pts));
		}
		

		return polylines;	
	}

}
