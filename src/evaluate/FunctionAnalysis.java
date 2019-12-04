package evaluate;

import java.util.ArrayList;
import java.util.List;

import osm.GeoMath;
import processing.core.PApplet;
import utils.ColorHelper;
import utils.Container;
import utils.Gpoi;
import utils.Tools;
import wblut.geom.WB_GeometryOp;
import wblut.geom.WB_Point;

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

	public void drawGridCount(Tools tools) {
		for (int i = 0; i < n[0]; ++i) {
			for (int j = 0; j < n[1]; ++j) {
				float x = (float) (i * step + min[0]);
				float y = (float) (j * step + min[1]);
				tools.app.noStroke();
				if (cnt[i][j] == 0) {
					tools.app.fill(255);
				} else {
					int[] c = color[totNum - (int)(Math.log1p(cnt[i][j]) / Math.log(Tools.RATIO))];
					tools.app.fill(c[0], c[1], c[2]);
				}
				tools.app.rect(x, y, (float) step, (float) step);
			}
		}
		
		for(int i = 0; i < color.length; ++ i) {
			int x = 100;
			int y = tools.app.height - 100 - i*50;
			tools.cam.begin2d();
			tools.app.noStroke();
			tools.app.fill(color[i][0], color[i][1], color[i][2]);
			tools.app.rect(x, y, 30, 30);
			
			tools.app.fill(0);
			tools.app.textSize(17);
			
			int l = (int) Math.ceil(Math.exp((Math.log(Tools.RATIO) * (totNum-i)))) - 1;
			int r = (int) Math.ceil(Math.exp((Math.log(Tools.RATIO) * (totNum-i+1)))) - 1;
			tools.app.text("POI Num Range [" + l + ", " + r + ") ", x + 50, y+20);
			tools.cam.begin3d();
		}
	}
	
	public double[][] getPointsNeighbor(double[] pos, double[][] points, double r) {
		WB_Point p = new WB_Point(pos);
		List<double[]> innerPoint = new ArrayList<>();
		for(int i = 0; i < points.length; ++ i) {
			WB_Point q = new WB_Point(points[i]);
			double dis = WB_GeometryOp.getDistance3D(p, q); 
			if(dis < r) {
				innerPoint.add(points[i]);
			}
		}
		return innerPoint.toArray(new double[innerPoint.size()][]);
	}
	
	public Gpoi[] getGpoisNeighbor(Gpoi pos, double r) {
		List<Gpoi> innerGpoi = new ArrayList<>();

		WB_Point p = new WB_Point(GeoMath.latLngToXY(pos.getLat(), pos.getLng()));
		for(Gpoi gpoi : Container.gpois) {
			WB_Point q = new WB_Point(GeoMath.latLngToXY(gpoi.getLat(), gpoi.getLng()));
			double dis = WB_GeometryOp.getDistance3D(p, q); 
			if(dis < r) {
				innerGpoi.add(gpoi);
			}
		}
		return innerGpoi.toArray(new Gpoi[innerGpoi.size()]);
	}
}