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
    private WB_PolyLine ply;
    private Map<String, String> tags;
    public boolean isClosed = false;
    public boolean isBuilding = false;
    public boolean isHighway = false;

    
    public Aoi(WB_PolyLine polyline) {
        setPly(polyline);
        setTags(new HashMap<>());
    }
    
    public Aoi(WB_PolyLine pl, boolean b) { 
        isClosed = b;
        setPly(pl);
        setTags(new HashMap<>());
    }
    
    //TODO: UNCHECKED
    public Aoi(WB_Polygon out, WB_Polygon inner) {
        setPly((new WB_GeometryFactory()).createPolygonWithHole(
                out.getPoints().toArray(), inner.getPoints().toArray()));
    }
    
    public Aoi(WB_Point...pts) {
       setPly((new WB_GeometryFactory()).createSimplePolygon(pts));
    }

    public void addTag(String key, String value) {
        getTags().put(key, value);
    }
    
    
    public void setDate(Date t) {
        timestamp = t;
    }
    
    public void draw(Tools tool) {
    	tool.app.noFill();
    }

    public void printTag() {
        for(String key : getTags().keySet()) {
//            if(key == "building") continue;
            System.out.println("AOI key = " + key + ", value = " + getTags().get(key) );
        }
        System.out.println("---------------------------------------------");
    }


	public WB_PolyLine getPly() {
		return ply;
	}

	public void setPly(WB_PolyLine ply) {
		this.ply = ply;
	}

	public Map<String, String> getTags() {
		return tags;
	}

	public void setTags(Map<String, String> tags) {
		this.tags = tags;
	}

}
