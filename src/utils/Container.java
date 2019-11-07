package utils;

import java.util.ArrayList;
import java.util.List;


import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import javafx.util.Pair;
import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;

public class Container {
    public static final String OSM_FILENAME = "./data/part_prato.osm.pbf";
    public static final double[] MAP_BASE = new double[] { 11.093150000000001, 43.8827 };
    public static final long[] TIME_MIN_MAX = new long[] { 1215035100000L, 1572163411000L };
    
    
//    public static final String OSM_FILENAME = "./data/centro-latest.osm.pbf";
//    public static final String OSM_FILENAME = "./data/planet_10.92,43.81_11.2627,43.9551.osm.pbf";
//    public static final double[] BASE = new double[] { 11.0913501, 43.88255};

    public static BiMap<Long, Integer> wayid;
    public static BiMap<Long, Integer> nodeid;
    public static List<Pair<Integer, Integer>> edges;


    public static List<WB_Point> points;
    public static List<WB_PolyLine> plys;
    
    
    public static List<Poi> pois;
    public static List<Aoi> aois;
    
    
    public static void initAll() {
         points = new ArrayList<>();
         nodeid = HashBiMap.create();       
         wayid = HashBiMap.create();
         edges = new ArrayList<>();
         plys = new ArrayList<>();
         pois = new ArrayList<>();
         aois = new ArrayList<>();
    }
}
