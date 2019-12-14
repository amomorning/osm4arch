package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dxfExporter.Constants;
import dxfExporter.DXFData;
import dxfExporter.DXFExport;
import dxfExporter.DXFLayer;
import dxfExporter.DXFPoint;
import evaluate.FunctionAnalysis;
import evaluate.StreetAnalysis;
import osm.OsmTypeDetail;
import processing.core.PApplet;
import readDXF.DXFImport;
import utils.Aoi;
import utils.ColorHelper;
import utils.Container;
import utils.ExportDXF;
import utils.Tools;
import wblut.geom.WB_Coord;
import wblut.geom.WB_CoordCollection;
import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;
import wblut.geom.WB_Polygon;

public class DisplayBlock extends PApplet {

	List<WB_Point> pts;
	List<WB_Polygon> polygons;
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
		tools = new Tools(this, LEN_OF_CAMERA);
		seivePolyline();
//		calcBlock();
		readBlock();
//		calcUniformPoint();

		initGUI();
	}

	public void draw() {
		background(255);
		tools.cam.drawSystem(LEN_OF_CAMERA);

		float[] c = ColorHelper.hexToHSV(ColorHelper.RED);
		stroke(c[0], c[1], c[2]);
		fill(c[0], c[1], c[2]);
		if (pts != null)
			tools.render.drawPoint(pts, 4);

		drawBlock();
		stroke(0);
		tools.render.drawPolylineEdges(plys);
		tools.drawCP5();
	}

	public void initGUI() {

	}

	public void seivePolyline() {

		plys = new ArrayList<>();
		for (Aoi aoi : Container.aois) {
			if (!aoi.isHighway) continue;
			if(!aoi.getTags().containsKey("junction") && aoi.isClosed) continue;

			String key = aoi.getTags().get("highway");

			if (key.indexOf("link") > -1) continue;
			if (OsmTypeDetail.roadMap.containsKey(key) && OsmTypeDetail.roadMap.get(key) != OsmTypeDetail.Road.R1
					&& OsmTypeDetail.roadMap.get(key) != OsmTypeDetail.Road.S3)
				plys.add(aoi.getPly());
		}
	}

	public void calcUniformPoint() {
		try {
			pts = StreetAnalysis.getInstance().writeSamplePoint("./data/points(Uniform).csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void calcBlock() {

		polygons = FunctionAnalysis.getInstance().getMapPolygon(plys);
		colorMode(HSB);
		System.out.println("Total polygons: " + polygons.size());

	}

	public void drawBlock() {
		if (polygons == null)
			return;
		stroke(255);
		for (int i = 0; i < polygons.size(); ++i) {
			fill(c[i], 100, 200);
			if (polygons.get(i).getSignedArea() > 100)
				continue;
			tools.render.drawPolygonEdges(polygons.get(i));
		}

	}

	public void readBlock() {
		polygons = FunctionAnalysis.getInstance().getMapPolygonOffline();
		System.out.println("Total polygons: " + polygons.size());
		colorMode(HSB);
	}

//	@SuppressWarnings("unchecked")
	public void keyPressed() {
		if (key == 's' || key == 'S') {
			ExportDXF dxf = new ExportDXF();
			for (WB_Polygon plg : polygons) {
				dxf.add(plg, ExportDXF.BROKEN);
			}
			dxf.save("./data/siteblock.dxf");
			System.out.println("Finish export.");

		}
	}

}
