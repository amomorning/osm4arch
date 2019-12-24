package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.csvreader.CsvReader;

import Guo_Cam.Vec_Guo;
import osm.GeoMath;
import processing.core.PApplet;
import utils.Aoi;
import utils.ColorHelper;
import utils.Container;
import utils.Tools;
import wblut.geom.WB_Circle;
import wblut.geom.WB_PolyLine;
import wblut.geom.WB_Polygon;

public class DisplayBlockFuntion extends PApplet {
	Tools tools;
	public static final int LEN_OF_CAMERA = 5000;

	List<WB_PolyLine> plys;
	List<WB_Polygon> plgs;
	int[] fillcolor, linecolor;
	private double circleR = 10;

	public void settings() {
		size(1200, 1000, P3D);
	}

	public void setup() {
		tools = new Tools(this, LEN_OF_CAMERA);

		initGUI();
	}

	public void draw() {
		background(255);
		tools.cam.drawSystem(LEN_OF_CAMERA);

		stroke(255, 0, 0);

		if (plgs != null) {
			fill(fillcolor[0], fillcolor[1], fillcolor[2]);
			stroke(linecolor[0], linecolor[1], linecolor[2]);
			tools.render.drawPolygonEdges(plgs);
		}
		
		if(plys != null) { 
			noFill();
			stroke(linecolor[0], linecolor[1], linecolor[2]);
			tools.render.drawPolylineEdges(plys);
		}

		tools.drawCP5();
		tools.drawCircle(this, circleR);

	}

	public int[][] getColor(int num, int[] a) {
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < a.length; ++i) {
			if (a[i] == 0)
				continue;
			min = Math.min(a[i], min);
			max = Math.min(a[i], max);
		}

		int step = (max - min + num - 1) / num + 1;

		int[][] c = ColorHelper.createGradientHue(num, ColorHelper.RED, ColorHelper.BLUE);

		int[][] ret = new int[a.length][3];
		for (int i = 0; i < a.length; ++i) {
			if (a[i] == 0)
				continue;
			int tmp = (a[i] - min) / step;
			ret[i] = c[tmp];
		}
		return ret;
	}

	public void initGUI() {

	}

	public void keyPressed() {

		if (key == 'g' || key == 'G') {
			getPolygonFuntionFromCSV("./data/green.csv");
			fillcolor = ColorHelper.hsvToRGB(80.0f/360*255, 255*0.15f, 255*0.95f);
			linecolor = ColorHelper.hsvToRGB(80.0f/360*255, 255*0.25f, 255*0.45f);
		}
		if (key == 'b' || key == 'B') {
			getPolygonFuntionFromCSV("./data/blue.csv");
			fillcolor = ColorHelper.hsvToRGB(180.0f/360*255, 255*0.15f, 255*0.95f);
			linecolor = ColorHelper.hsvToRGB(180.0f/360*255, 255*0.25f, 255*0.45f);
		}
		if (key == 'i' || key == 'I') {
			getPolygonFuntionFromCSV("./data/brown.csv");
			fillcolor = ColorHelper.hsvToRGB(10.0f/360*255, 255*0.15f, 255*0.55f);
			linecolor = ColorHelper.hsvToRGB(10.0f/360*255, 255*0.25f, 255*0.25f);
		}
		
		if(key == ']') {
			circleR += 10;
			System.out.println("Now circle radius is " + circleR + "m.");
		}

		if(key == '[') {
			circleR -= 10;
			System.out.println("Now circle radius is " + circleR + "m.");
		}
	}

	public void getPolygonFuntionFromCSV(String filename) {
		try {
			CsvReader reader = new CsvReader(filename);

			List<String> keys = new ArrayList<>();
			List<String> values = new ArrayList<>();
			while (reader.readRecord()) {
				keys.add(reader.get(0));
				values.add(reader.get(1));
			}

			plgs = new ArrayList<>();
			plys = new ArrayList<>();
			for (Aoi aoi : Container.aois) {
//				if (!aoi.isClosed)
//					continue;

				Map<String, String> mp = aoi.getTags();
				boolean flag = false;

				for (int i = 0; i < keys.size(); ++i) {
					if (!mp.containsKey(keys.get(i)))
						continue;
					if (mp.get(keys.get(i)).equals(values.get(i))) {
						flag = true;
					}
					if(values.get(i).equals("all")) {
						flag = true;
					}
				}

				if (flag == true) {
					if(aoi.isClosed) plgs.add(Tools.toWB_Polygon(aoi.getPly()));
					else plys.add(aoi.getPly());
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Finish read.");
	}

}