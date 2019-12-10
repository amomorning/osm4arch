package main;

import java.util.List;

import evaluate.FunctionAnalysis;
import osm.GeoMath;
import processing.core.PApplet;
import utils.ColorHelper;
import utils.Container;
import utils.Gpoi;
import utils.Tools;
import wblut.geom.WB_GeometryOp;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;

public class DisplayBlockFuntion extends PApplet {
	Tools tools;
	public static final int LEN_OF_CAMERA = 5000;

	List<WB_Point> pts;
	List<WB_Polygon> plgs;
	int[] cnt;
	int[][] color;

	public void settings() {
		size(1200, 1000, P3D);
	}

	public void setup() {
		tools = new Tools(this, LEN_OF_CAMERA);
		GeoMath geoMath = new GeoMath(Container.MAP_LAT_LNG);

//		int n = Container.gpois
		plgs = FunctionAnalysis.getInstance().getMapPolygonOffline();
		cnt = new int[plgs.size()];

		int ttt = 0;
		for(int i = 0; i < plgs.size(); ++ i) cnt[i] = 0;
		for (Gpoi gpoi : Container.gpois) {

			WB_Point pt = new WB_Point(geoMath.latLngToXY(gpoi.getLat(), gpoi.getLng()));
			for (int i = 0; i < plgs.size(); ++i) {
				WB_Polygon plg = plgs.get(i);
				if (WB_GeometryOp.contains2D(pt, plg)) {
					cnt[i]++;
					break;
				}
			}
			
			System.out.println("Finished #" + (ttt++) + "/" + Container.gpois.size());
		}
		
		color = getColor(8, cnt);

		initGUI();
	}

	public void draw() {
		background(255);
		tools.cam.drawSystem(LEN_OF_CAMERA);

		stroke(255, 0, 0);
		if (pts != null)
			tools.render.drawPoint(pts, 3);
		
		for(int i = 0; i < cnt.length; ++ i) {
			if(cnt[i] > 0) {
				fill(color[i][0], color[i][1], color[i][2]);
				tools.render.drawPolygonEdges(plgs.get(i));
			}
		}
		tools.drawCP5();
	}

	public int[][] getColor(int num, int[] a) {
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < a.length; ++i) {
			if(a[i] == 0) continue;
			min = Math.min(a[i], min);
			max = Math.min(a[i], max);
		}

		int step = (max - min + num - 1) / num + 1;

		int[][] c = ColorHelper.createGradientHue(num, ColorHelper.RED, ColorHelper.BLUE);

		int[][] ret = new int[a.length][3];
		for (int i = 0; i < a.length; ++i) {
			if(a[i] == 0) continue;
			int tmp = (a[i] - min) / step;
			ret[i] = c[tmp];
		}
		return ret;
	}

	public void initGUI() {

	}

	public void keyPressed() {
	}

}