package evaluate;

import java.util.ArrayList;
import java.util.List;

import utils.ColorHelper;
import utils.Tools;
import wblut.geom.WB_AABB;
import wblut.geom.WB_AABB2D;
import wblut.geom.WB_Coord;
import wblut.geom.WB_GeometryFactory;
import wblut.geom.WB_GeometryOp;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Segment;
import wblut.geom.WB_Transform3D;

public class ShapeAnalysis {

	private WB_Polygon[] polygons;
	private int[][] color;

	/**
	 * @Function: shapeIndexBoyeeClark
	 * @Description: 1964, safe version which can get all n indices,
	 *
	 * @return: double: 0 for wrong
	 */
	public double shapeIndexBoyeeClark(WB_Polygon polygon, int n) {
		List<WB_Segment> segs = new ArrayList<>();
		List<WB_Segment> segments = new ArrayList<>();

		WB_GeometryFactory gf = new WB_GeometryFactory();
		WB_Point pt = polygon.getCenter();

		// Transform to center
		WB_Transform3D T = new WB_Transform3D().addTranslate(-1, polygon.getCenter());
		polygon.applySelf(T);

		System.out.println(pt);
		System.out.println(polygon.getCenter());

		// get max r
		double r = 0;
		for (int i = 0; i < polygon.getNumberOfPoints(); ++i) {
			WB_Point p = polygon.getPoint(i);
			r = Math.max(r, p.getLength());
		}

		for (int i = 0; i < n; ++i) {
			double phi = Math.PI * 2 / n * i;
			segs.add(new WB_Segment(WB_Point.ZERO(), gf.createPointFromPolar(r, phi)));
		}

		if (WB_GeometryOp.contains2D(WB_Point.ZERO(), polygon) == false) {
			polygon = WB_GeometryOp.trimConvexPolygon2D(polygon, 150);
			System.err.println("Polygon center is not in polygon.");
//			return 0;
		}

		for (int i = 0; i < n; ++i) {
			WB_Point tmp = null;
			WB_Segment s1 = segs.get(i);
			for (int j = 0; j < polygon.getNumberOfPoints(); ++j) {
				int k = (j + 1) % polygon.getNumberOfPoints();
				WB_Segment s2 = new WB_Segment(polygon.getPoint(j), polygon.getPoint(k));
				WB_Point p = (WB_Point) WB_GeometryOp.getIntersection2D(s1, s2).getObject();
				if (p == null) {
					continue;
				}
				if (tmp != null) {
					System.err.println("Rays intersect conflictly");
					if (tmp.getLength() < p.getLength())
						tmp = p.copy();
//					return 0;
				} else {
					tmp = p.copy();
				}
			}
			if (tmp == null) {
				System.err.println("Polygon is broken.");
			} else {
			segments.add(new WB_Segment(WB_Point.ZERO(), tmp));
			}
		}
		double tot = 0;
		for(int i = 0; i < segments.size(); ++ i) {
			tot += segments.get(i).getLength();
		}

		double shapeIndex = 0;
		for (int i = 0; i < segments.size(); ++i) {
			double ri = segments.get(i).getLength();
			shapeIndex += Math.abs(ri / tot * 100.0 - 100 / n);
		}
		return shapeIndex;
	}

	public void shapeIndex(List<WB_Polygon> plys, int num) {
		color = new int[plys.size()][3];
		polygons = new WB_Polygon[plys.size()];

		double[] index = new double[plys.size()];

		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;

		for (int i = 0; i < plys.size(); ++i) {
			polygons[i] = new WB_Polygon(plys.get(i));
			index[i] = shapeIndexBoyeeClark(plys.get(i), 20);
			System.out.println("index = " + index[i]);

			if (index[i] > 0.0) {
				min = Math.min(min, index[i]);
				max = Math.max(max, index[i]);
			}
		}

		int[][] c = ColorHelper.createGradientHue(num, ColorHelper.RED, ColorHelper.BLUE);
		double div = (max - min) / (num - 1);

		for (int i = 0; i < plys.size(); ++i) {
			if (index[i] > 0.0) {
				double tmp = index[i] - min;
				color[i] = c[num-1-(int) Math.floor(tmp / div)];
			} else {

				color[i] = new int[] {0, 0, 0};
//				color[i] = ColorHelper.hexToRGB(ColorHelper.PINK);
			}
		}

	}

	public void drawShapeIndex(Tools tools) {

		for (int i = 0; i < color.length; ++i) {
			tools.app.fill(color[i][0], color[i][1], color[i][2]);
			tools.app.stroke(0);
			tools.render.drawPolygonEdges(polygons[i]);
		}
	}

	public double fastShapeIndexBoyeeClark(WB_Polygon polygon, int n) {
		List<WB_Segment> segs = new ArrayList<>();
		List<WB_Point> points = new ArrayList<>();
		WB_Point pt = polygon.getCenter();

		// Transform to center
		WB_Transform3D T = new WB_Transform3D().addTranslate(-1, polygon.getCenter());
		polygon.applySelf(T);

		// get max r
		double r = 0;
		for (int i = 0; i < polygon.getNumberOfPoints(); ++i) {
			WB_Point p = polygon.getPoint(i);
			r = Math.max(r, p.getLength());
		}

		for (int i = 0; i < polygon.getNumberOfPoints(); ++i) {
			points.add(polygon.getPoint(i));
		}

		for (int i = 0; i < points.size(); ++i) {
			int j = (i + 1) % points.size();
			WB_Segment seg = new WB_Segment(points.get(i), points.get(j));
			double a = Math.PI * 2 / 15;
			segs.addAll(getRangedSegment(r, a, seg));

		}

		double tot = 0;
		for (int i = 0; i < segs.size(); ++i) {
			tot += segs.get(i).getLength();
		}

		double shapeIndex = 0;
		for (int i = 0; i < segs.size(); ++i) {
			double ri = segs.get(i).getLength();
			shapeIndex += Math.abs(ri / tot * 100.0 - 100.0 / segs.size());
		}
		return shapeIndex;
	}

	private double calcAngleFromX2D(WB_Coord pt) {
		double angle = WB_Point.getAngle(pt, WB_Point.X());
		if (pt.yd() < 0)
			return Math.PI * 2 - angle;
		else
			return angle;
	}

	private List<WB_Segment> getRangedSegment(double r, double a, WB_Segment s1) {
		WB_GeometryFactory gf = new WB_GeometryFactory();

		double b = calcAngleFromX2D(s1.getOrigin());
		double c = calcAngleFromX2D(s1.getEndpoint());

		if (c < b) {
			double t = b;
			b = c;
			c = t;
		}
		if (c > Math.PI * 3.0 / 2.0 && b < Math.PI / 2) {
			c -= Math.PI * 2;
			double t = b;
			b = c;
			c = t;
		}

		int t1 = (int) Math.floor(b / a) + 1;
		int t2 = (int) Math.floor(c / a) + 1;
		List<WB_Segment> segs = new ArrayList<>();
		for (int i = t1; i < t2; ++i) {
			double phi = i * a;

			WB_Segment s2 = new WB_Segment(WB_Point.ZERO(), gf.createPointFromPolar(r, phi));
			WB_Point p = (WB_Point) WB_GeometryOp.getIntersection2D(s1, s2).getObject();
			segs.add(new WB_Segment(WB_Point.ZERO(), p));

		}
		System.out.println(segs.size());
		return segs;
	}
}
