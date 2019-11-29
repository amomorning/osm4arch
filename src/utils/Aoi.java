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
    Date timestamp;
    WB_PolyLine ply;
    Map<String, String> tags;
    public boolean isClosed = false;

    
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
    
    
    public void setDate(Date t) {
        timestamp = t;
    }
    

    public void printTag() {
        for(String key : tags.keySet()) {
//            if(key == "building") continue;
            System.out.println("AOI key = " + key + ", value = " + tags.get(key) );
        }
        System.out.println("---------------------------------------------");
    }

    public boolean isBuilding() {
        for(String key : tags.keySet()) {
            if(key.equals("building"))
                return true;
        }
        return false;
    }
    
    public boolean isResidential() {
        for(String key : tags.keySet()) {
            if(key.equals("landuse") && tags.get(key).equals("residential"))
                return true;
        }
        return false;
    }
        
    public boolean isIndustrial() {
        for(String key : tags.keySet()) {
            if(key.equals("landuse") && tags.get(key).equals("industrial")) return true;
        }
        return false;
    }
    
    public boolean isForest() {
        for(String key : tags.keySet()) {
            if(key.equals("landuse") && tags.get(key).equals("forest"))
                return true;
        }
        return false;
    }
    
    public boolean isPiazza() {
        for(String key : tags.keySet()) {
            if(key.equals("addr:street") && tags.get(key).indexOf("Piazza") > 0) {
                return true;
            }
        }
        return false;
    }

	public boolean isHighway() {
        for(String key : tags.keySet()) {
            if(key.equals("highway"))
                return true;
        }
        return false;
	}
}
