package utils;

import java.util.ArrayList;
import java.util.List;


import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import javafx.util.Pair;
import wblut.geom.WB_Point;

public class Container {
    public static final String OSM_FILENAME = "./data/part_prato.osm.pbf";


    public static List<WB_Point> points;
    public static BiMap<Long, Integer> nodeid;
    
    public static List<Pair<Integer, Integer>> edges;
    
    
    public static void initAll() {
         points = new ArrayList<>();
         nodeid = HashBiMap.create();       
         edges = new ArrayList<>();
    }
}
