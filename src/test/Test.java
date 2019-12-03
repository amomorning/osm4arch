package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import processing.core.PApplet;
import utils.Tools;
import wblut.geom.WB_GeometryFactory;
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

	WB_GeometryFactory gf=new WB_GeometryFactory();
	List<WB_Segment> segs;
	public static void main(String[] args) {

		PApplet.main("test.Test");
	}

	public void settings() {
		size(700, 400, P3D);
	}

	public void setup() {
		tools = new Tools(this, 20);

		WB_Point[] pts = new WB_Point[5];
		
		Random rand = new Random(2333);
		
		for(int i = 0; i < 5; ++ i) {
			pts[5-i-1] = gf.createPointFromPolar(10.0 + rand.nextInt(10), i * Math.PI*2.0/5.0);
		}
		
		polygon= new WB_Polygon(pts);

		WB_Point pt = polygon.getCenter();

		// Transform to center
		WB_Transform3D T = new WB_Transform3D().addTranslate(-1, polygon.getCenter());
		polygon.applySelf(T);
		
		System.out.println("Transfrom from " + pt + " to " + polygon.getCenter());
		
		points = new ArrayList<>();
		for(int i = 0; i < polygon.getNumberOfPoints(); ++ i) {
			points.add(polygon.getPoint(i));
		}
		
		segs = new ArrayList<>();
		
//		double r = 20;
//		int num = 15;
//		for(int i = 0; i < num; ++ i) {
//			double phi = Math.PI * 2 / num * i;
//			segs.add(new WB_Segment(WB_Vector.ZERO(), gf.createPointFromPolar(r, phi)));
//		}
		
		
		for(int i = 0; i < points.size(); ++ i) {
			int j = (i+1)%points.size();

			System.out.println(i + ", " + j);
			System.out.println(calcAngleFromX2D(points.get(i)));
			System.out.println(calcAngleFromX2D(points.get(j)));
			
			double a = Math.PI * 2 / 15;
			segs.addAll(getRangedSegment(20, a,calcAngleFromX2D(points.get(i)) , calcAngleFromX2D(points.get(j))));
			
			System.out.println("");
		}
	}
	
	public double calcAngleFromX2D(WB_Point pt) {
		double angle = pt.getAngle(WB_Vector.X());
		if(pt.yd() < 0) return Math.PI*2 - angle;
		else return angle;
	}
	
	public List<WB_Segment>  getRangedSegment(double r, double a, double b, double c) {
		if(c < b) { double t = b; b = c; c = t; }
		if(c > Math.PI*3.0/2.0 && b < Math.PI/2) {
			c -= Math.PI * 2;
			double t = b; b = c; c = t;
		}
		
		int t1 = (int) Math.floor(b/a) + 1;
		int t2 = (int) Math.floor(c/a) + 1;
		List<WB_Segment> segs = new ArrayList<>();
		for(int i = t1; i < t2; ++ i) {
			double phi = i*a;
			segs.add(new WB_Segment(WB_Vector.ZERO(), gf.createPointFromPolar(r, phi)));
		}
		System.out.println(segs.size());
		return segs;
	}
	
	public void draw() {

		background(255);
		tools.cam.drawSystem(20);

		stroke(0);
		fill(200);
		tools.render.drawPolygonEdges(polygon);

		for(int i = 0; i < points.size(); ++ i) {
			stroke(255, 0, 0);
			fill(255, 0, 0);
			WB_Point pt = points.get(i);
			tools.render.drawPoint(points.get(i));
			tools.printOnScreen3D("" + (i+1), 12, pt.xd(), pt.yd(), pt.zd());
		}
		
		tools.render.drawSegment(segs);
	}

}
