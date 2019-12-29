package test;

import java.util.ArrayList;
import java.util.List;

import wblut.geom.WB_Point;

public class Main {

	public static void main(String[] args) {
		
		List<WB_Point> pts = new ArrayList<>();

		double d = 12; 
		
		System.out.println(pts.size() + " " + d);
	}
	
	public static double changeD(List<WB_Point> pts, double d) {
		pts.add(new WB_Point());
		return d/2;

	}

}
