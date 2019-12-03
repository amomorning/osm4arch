package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import processing.core.PApplet;
import utils.Tools;
import wblut.geom.WB_GeometryFactory;
import wblut.geom.WB_GeometryOp;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Segment;
import wblut.geom.WB_Transform3D;
import wblut.geom.WB_Vector;
import wblut.processing.WB_Render;

public class Test extends PApplet {

	Tools tools;
	int num = 10;
	WB_Render render;
	WB_Polygon polygon;
	List<WB_Point> points;
	int[][] c;

	WB_GeometryFactory gf = new WB_GeometryFactory();
	List<WB_Segment> segments;

	public static void main(String[] args) {

		PApplet.main("test.Test");
	}

	public void settings() {
		size(700, 400, P3D);
	}

	public void setup() {
		tools = new Tools(this, 20);

		int N = 20;
		WB_Point[] pts = new WB_Point[N];

		Random rand = new Random(2333);

		for (int i = 0; i < N; ++i) {
			pts[N - i - 1] = gf.createPointFromPolar(10.0 + rand.nextInt(20), i * Math.PI * 2.0 / N);
		}

		polygon = new WB_Polygon(pts);

		WB_Point pt = polygon.getCenter();

		// Transform to center
		WB_Transform3D T = new WB_Transform3D().addTranslate(-1, polygon.getCenter());
		polygon.applySelf(T);

		System.out.println("Transfrom from " + pt + " to " + polygon.getCenter());

		points = new ArrayList<>();
		for (int i = 0; i < polygon.getNumberOfPoints(); ++i) {
			points.add(polygon.getPoint(i));
		}

		segments = new ArrayList<>();
		List<WB_Segment> segs = new ArrayList<>();

		double r = 0;
		for(int i = 0; i < polygon.getNumberOfPoints(); ++ i) {
			WB_Point p = polygon.getPoint(i);
			r = Math.max(r, p.getLength());
			System.out.println("r = " + r);
		}
		int num = 40;
		for (int i = 0; i < num; ++i) {
			double phi = Math.PI * 2 / num * i;
			segs.add(new WB_Segment(WB_Vector.ZERO(), gf.createPointFromPolar(r, phi)));
		}

		if (WB_GeometryOp.contains2D(pt, polygon) == false) {
			System.err.println("Polygon center is not in polygon.");
		}

		for (int i = 0; i < num; ++i) {
			WB_Point tmp = null;
			WB_Segment s1 = segs.get(i);
			for(int j = 0; j < polygon.getNumberOfPoints(); ++ j) {
				int k = (j+1)%polygon.getNumberOfPoints();
				WB_Segment s2 = new WB_Segment(polygon.getPoint(j), polygon.getPoint(k));
				WB_Point p = (WB_Point)WB_GeometryOp.getIntersection2D(s1, s2).getObject();
				if(p == null) {System.out.println("null"); continue;}
				if(tmp != null) {
					System.err.println("Rays intersect conflictly");
				}
				tmp = p.copy();
			}
			if(tmp != null) {
				segments.add(new WB_Segment(WB_Vector.ZERO(), tmp));
			}
		}

//		for(int i = 0; i < points.size(); ++ i) {
//			int j = (i+1)%points.size();
//
//			System.out.println(i + ", " + j);
//			System.out.println(calcAngleFromX2D(points.get(i)));
//			System.out.println(calcAngleFromX2D(points.get(j)));
//			
//			double a = Math.PI * 2 / 15;
//			segs.addAll(getRangedSegment(20, a,calcAngleFromX2D(points.get(i)) , calcAngleFromX2D(points.get(j))));
//			
//			System.out.println("");
//		}
	}



	public void draw() {

		background(0);
		tools.cam.drawSystem(20);

		stroke(0);
		fill(100);
		tools.render.drawPolygonEdges(polygon);

		for (int i = 0; i < points.size(); ++i) {
			stroke(255, 0, 0);
			fill(255, 0, 0);
			WB_Point pt = points.get(i);
			tools.render.drawPoint(points.get(i));
			tools.printOnScreen3D("" + (i + 1), 12, pt.xd(), pt.yd(), pt.zd());
		}

		stroke(0, 255, 255);
		tools.render.drawSegment(segments);
	}

}
