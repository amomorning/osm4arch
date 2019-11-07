package osm;

import utils.Container;

public class GeoMath {
    
 public static final double _180_PI = 180d / Math.PI;
    
    public static final double _360_PI = 360d / Math.PI;
    
    public static final double PI_360 = Math.PI / 360d;
    
    public static final double PI_180 = Math.PI / 180d;
    
    public static final double PI_4 = Math.PI / 4d;
    
    public static final double PI_2 = Math.PI / 2d;
    
    public static final double MAX_LAT = Math.toDegrees(Math.atan(Math.sinh(Math.PI))); 

    public static final int EARTH_RADIUS = (6378137 + 6356752) / 2;
    
    /**
     * Calculates a projected coordinate for a given longitude value. 
     * 
     * @see {@link http://en.wikipedia.org/wiki/Mercator_projection#Mathematics_of_the_projection}
     * 
     * @param lat the latitude as double value
     * 
     * @return the mercator-projected x-coordinate.
     * 
     */
    public static double lonToMercator(double lon) {
        return EARTH_RADIUS * lon * PI_180;
    }
    
    public static double mercatorToLon(double x) {
        return x / EARTH_RADIUS / PI_180;
    }

    /**
     * Calculates a projected coordinate for a given latitude value. When lat is bigger than MAX_LAT, it will be clamped
     * to MAX_LAT.
     * 
     * @see {@link http://en.wikipedia.org/wiki/Mercator_projection#Mathematics_of_the_projection}
     * 
     * @param lat the latitude as double value
     * 
     * @return the mercator-projected y-coordinate.
     * 
     */
    public static double latToMercator(double lat) {
        lat = Math.min(MAX_LAT, lat);
        lat = Math.max(-MAX_LAT, lat);
        return EARTH_RADIUS * Math.log(Math.tan(lat * PI_360 + PI_4));
    }
    
    public static double mercatorToLat(double y) {
        y = y / EARTH_RADIUS;
        return _180_PI * (2d * Math.atan(Math.exp(y)) - PI_2);
    }
    
    
    public static double[] lonLatToXY(double lon, double lat) {
        double x0 = lonToMercator(Container.MAP_BASE[0]);
        double y0 = latToMercator(Container.MAP_BASE[1]);
        double x1 = lonToMercator(lon);
        double y1 = latToMercator(lat);
        return new double[] {x1-x0, y1-y0};
    }
    
    public static double[] xyToLonLat(double x, double y) {
        x += lonToMercator(Container.MAP_BASE[0]);
        y += latToMercator(Container.MAP_BASE[1]);
        
        return new double[] {mercatorToLon(x), mercatorToLat(y)};
    }
    
    public static double mercatorDistance(double lon1, double lat1, double lon2, double lat2) {
        double y1 = latToMercator(lat1);
        double x1 = lonToMercator(lon1);
        double y2 = latToMercator(lat2);
        double x2 = lonToMercator(lon2);
        return Math.sqrt((x1-x2) * (x1-x2) + (y1-y2) * (y1-y2));
    }
    

    /**
     * Caculate the haversine distance between two points
     * 
     * @param lon1
     * 
     * @param lat1
     * 
     * @param lon2
     * 
     * @param lat2
     * 
     * @return distance between the two point in meters
     */
    public static double haversineDistance(double lon1, double lat1, double lon2, double lat2) {
        
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +  Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2); 
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
        return EARTH_RADIUS * c;
    }

}
