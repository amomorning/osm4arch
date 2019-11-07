package utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import processing.core.PApplet;
import wblut.geom.WB_GeometryFactory;
import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;
import wblut.geom.WB_Polygon;

public class Aoi {
    Date date;
    WB_PolyLine ply;
    Map<String, String> tags;
    public boolean isClosed;
    
    public Aoi(WB_PolyLine polyline) {
        ply = polyline;
        tags = new HashMap<>();
    }
    
    public Aoi(WB_PolyLine pl, boolean b) { 
        isClosed = b;
        ply = pl;
        tags = new HashMap<>();
    }
    
    //TODO: UNCHECKED
    public Aoi(WB_Polygon out, WB_Polygon inner) {
        ply = (new WB_GeometryFactory()).createPolygonWithHole(
                out.getPoints().toArray(), inner.getPoints().toArray());
    }
    
    public Aoi(WB_Point...pts) {
       ply = (new WB_GeometryFactory()).createSimplePolygon(pts);
    }
    
    public void addTag(String key, String value) {
        tags.put(key, value);
    }
    
    public void setDate(Date timestamp) {
        date = timestamp;
    }
    
    public void draw(PApplet app) {
        
    }
}
