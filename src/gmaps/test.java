package gmaps;

import osm.GeoMath;
import utils.Container;

public class test {
            

    public static void main(String[] args) throws Exception {
        double[] xy = GeoMath.latLngToXY(Container.MAP_LAT_LNG);
        double[] latLng = GeoMath.xyToLatLng(xy[0], xy[1]);
        System.out.println(latLng[0] + " " + latLng[1]);
    }



}
