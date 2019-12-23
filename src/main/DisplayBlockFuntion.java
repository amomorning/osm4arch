package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.csvreader.CsvReader;

import osm.GeoMath;
import processing.core.PApplet;
import utils.Aoi;
import utils.ColorHelper;
import utils.Container;
import utils.Tools;
import wblut.geom.WB_Polygon;

public class DisplayBlockFuntion extends PApplet {
	Tools tools;
	public static final int LEN_OF_CAMERA = 5000;

	List<WB_Polygon> plgs;
	int[][] color;

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

		if (plgs != null)
			tools.render.drawPolygonEdges(plgs);

		tools.drawCP5();
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
		}
		if (key == 'b' || key == 'B') {
			getPolygonFuntionFromCSV("./data/blue.csv");
		}
		if (key == 'i' || key == 'I') {
			getPolygonFuntionFromCSV("./data/brown.csv");
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
					plgs.add(Tools.toWB_Polygon(aoi.getPly()));
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}