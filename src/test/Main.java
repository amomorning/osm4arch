package test;

import osm.GeoMath;
import utils.Container;

public class Main {

	public static void main(String[] args) {
		Container.initOsm();
		
		GeoMath geoMath = new GeoMath(Container.MAP_LAT_LNG);
		
		double[] a = Container.SW_LAT_LNG;
		double[] b = Container.NE_LAT_LNG;
		double dis = GeoMath.haversineDistance(a[0], a[1], a[0], b[1]);
		System.out.println(a[0] + " " + a[1] + " " + a[0] + " " + b[1]);
		System.out.println(dis);
		

		double[] bl = geoMath.latLngToXY(Container.SW_LAT_LNG);
		double[] tr = geoMath.latLngToXY(Container.NE_LAT_LNG);
		System.out.println(tr[0] - bl[0]);
		System.out.println(tr[1] - bl[1]);
		
		System.out.println("Map ratio = " + Container.MAPRATIO);
		System.out.println(a[0] + " " + a[1]);
		
		double[] ll = geoMath.xyToLatLng(bl[0], bl[1]);
		System.out.println(ll[0] + " " + ll[1]);
		
	}
}
