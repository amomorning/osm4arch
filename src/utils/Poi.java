package utils;

import java.util.HashMap;
import java.util.Map;

import wblut.geom.WB_Point;

public class Poi {
    WB_Point position;
    Map<String, String> tags;
    
    public Poi(WB_Point pts) {
        position = pts.copy();
        tags = new HashMap<>();
    }
    
    public void addTag(String key, String value) {
        tags.put(key, value);
    }
}
