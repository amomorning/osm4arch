package evaluate;

import osm.GeoMath;
import processing.core.PApplet;
import utils.ColorHelper;
import utils.Container;
import utils.Tools;

public class FunctionAnalysis {
	private int totNum;
	private int[] n;
	private int[][] cnt, color;
	private double[] min, max;
	private double step = 150;
	
	public void gridCount(double[][] points) {

		min = GeoMath.latLngToXY(Container.SW_LAT_LNG);
		max = GeoMath.latLngToXY(Container.NE_LAT_LNG);

		n = new int[2];
		for (int i = 0; i < 2; ++i) {
			n[i] = (int) ((max[i] - min[i]) / step) + 1;
		}

		int maxNum = 0;
		cnt = new int[n[0]][n[1]];
		for (int i = 0; i < n[0]; ++i) {
			for (int j = 0; j < n[1]; ++j) {
				cnt[i][j] = 0;
			}
		}
		for (int i = 0; i < points.length; ++i) {
			int[] pos = new int[2];
			for (int j = 0; j < 2; ++j) {
				pos[j] = (int) (((points[i][j]) - min[j]) / step);
			}
			if (pos[0] < 0 || pos[0] >= n[0] || pos[1] < 0 || pos[1] >= n[1])
				continue;
			cnt[pos[0]][pos[1]]++;

			maxNum = Math.max(maxNum, cnt[pos[0]][pos[1]]);
		}

		totNum = (int) (Math.log1p(maxNum) / Math.log(Tools.RATIO));
		color = ColorHelper.createGradientHue(totNum, ColorHelper.RED, ColorHelper.BLUE);
	}

	public void drawGridCount(PApplet app) {
		for (int i = 0; i < n[0]; ++i) {
			for (int j = 0; j < n[1]; ++j) {
				float x = (float) (i * step + min[0]);
				float y = (float) (j * step + min[1]);
				app.noStroke();
				if (cnt[i][j] == 0) {
					app.fill(255);
				} else {
					int[] c = color[totNum - ((int) (Math.log1p(cnt[i][j]) / Math.log(Tools.RATIO)))];
					app.fill(c[0], c[1], c[2]);
				}
				app.rect(x, y, (float) step, (float) step);
			}
		}
	}

}
