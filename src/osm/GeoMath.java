package osm;

import java.util.Locale;

import utils.Container;
import utils.Tools;
import wblut.geom.WB_Coord;

public class GeoMath {

	private static final double EQUATORIAL_RADIUS = 6378137.0;
	private static final double POLAR_RADIUS = 6356752.3;

	private static double EARTH_RADIUS = (EQUATORIAL_RADIUS + POLAR_RADIUS) / 2.;
	private static double[] CENTER = { 0., 0. };

	/**
	 * @Function:GeoMath
	 * @Description:TODO
	 * @param lat
	 * @param lng
	 */
	public GeoMath(double lat, double lng) {
		setCenter(lat, lng);
	}

	/**
	 * @Function:GeoMath
	 * @Description:TODO
	 * @param latLng
	 */
	public GeoMath(double[] latLng) {
		setCenter(latLng[0], latLng[1]);
	}

	/**
	 * @Function: setCenter
	 * @Description: TODO
	 * @param lat
	 * @param lng
	 *
	 * @return: void
	 */
	public void setCenter(double lat, double lng) {
		CENTER[0] = lat;
		CENTER[1] = lng;
		calcEarthRadius(lat);
	}

	/**
	 * @Function: calcEarthRadius
	 * @Description: calculate Geocentric radius at geodetic latitude
	 *               https://en.wikipedia.org/wiki/Earth_radius
	 * @param lat
	 *
	 * @return: void
	 */
	public static void calcEarthRadius(double lat) {
		lat = Math.toRadians(lat);

		double a = EQUATORIAL_RADIUS;
		double b = POLAR_RADIUS;

		double ta = a * Math.cos(lat);
		double tb = b * Math.sin(lat);

		EARTH_RADIUS = Math.sqrt((ta * a * ta * a + tb * b * tb * b) / (ta * ta + tb * tb));
	}

	/**
	 * @Function: latLngToXY
	 * @Description: TODO
	 * @param lat
	 * @param lng
	 * @return
	 *
	 * @return: double[]
	 */
	public double[] latLngToXY(double lat, double lng) {
		double d = haversineDistance(lat, lng, CENTER[0], CENTER[1]);
		double y = Math.toRadians(lat - CENTER[0]);
		double xp = Math.toRadians(lng - CENTER[1]);

		if (Container.MAPRATIO == 0) {
			double yp = Math.log(Math.tan(Math.PI / 4 + Math.toRadians(lat) / 2))
					- Math.log(Math.tan(Math.PI / 4 + Math.toRadians(CENTER[0]) / 2));
			Container.MAPRATIO = y / yp;
		}

		double x = Container.MAPRATIO * xp;

		return new double[] { EARTH_RADIUS * x, EARTH_RADIUS * y };
	}

	public double[] latLngToXY(double[] latLng) {
		return latLngToXY(latLng[0], latLng[1]);
	}

	/**
	 * @Function: xyToLatLng
	 * @Description: TODO
	 * @param x
	 * @param y
	 * @return
	 *
	 * @return: double[]
	 */
	public double[] xyToLatLng(double x, double y) {
		double lat = Math.toDegrees(y / EARTH_RADIUS + Tools.EPS) + CENTER[0];
		x /= Container.MAPRATIO;
		double lng = Math.toDegrees(x / EARTH_RADIUS + Tools.EPS) + CENTER[1];

		return new double[] { lat, lng };
	}

	/**
	 * Caculate the haversine distance between two points
	 * 
	 * @param lon1
	 * @param lat1
	 * @param lon2
	 * @param lat2
	 * 
	 * @return distance between the two point in meters
	 */
	public static double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
		calcEarthRadius((lat1 + lat2) / 2);

		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);

		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return EARTH_RADIUS * c;
	}

}
