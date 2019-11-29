package utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.collections.api.tuple.Pair;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Multimap;

import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;

public class Container {
//    public static final String OSM_FILENAME = "./data/part_prato.osm.pbf";
	public static final double[] MAP_LAT_LNG = new double[] {43.8783119, 11.08387555};
    public static final double[] NE_LAT_LNG = new double[] {43.9252112, 11.1474862};
    public static final double[] SW_LAT_LNG = new double[] {43.8314126, 11.0202649};
    public static final long[] TIME_MIN_MAX = new long[] { 1215035100000L, 1572163411000L };
    
    
//    public static final String OSM_FILENAME = "./data/centro-latest.osm.pbf";
    public static final String OSM_FILENAME = "./data/planet_10.92,43.81_11.2627,43.9551.osm.pbf";
//    public static final double[] BASE = new double[] { 11.0913501, 43.88255};
    public static final int UTMZONE = 32;
    public static final char UTMLETTER = 'T';

    public static BiMap<Long, Integer> wayid;
    public static BiMap<Long, Integer> nodeid;


    public static List<WB_Point> points;
    public static List<WB_PolyLine> plys;
    
    
    public static List<Gpoi> gpois;
    public static List<Poi> pois;
    public static List<Aoi> aois;
    
    public static Multimap<String, String> tagList;
    

    public static void initAll() {
         points = new ArrayList<>();
         nodeid = HashBiMap.create();       
         wayid = HashBiMap.create();

         plys = new ArrayList<>();
         pois = new ArrayList<>();
         aois = new ArrayList<>();
         gpois = new ArrayList<>();
         
         tagList = ArrayListMultimap.create();
    }
    
}
