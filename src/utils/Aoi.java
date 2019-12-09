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
    private Date timestamp;
    private long wayid;
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
    

    public void addTag(String key, String value) {
        getTags().put(key, value);
    }
    
    
    public void setDate(Date t) {
        setTimestamp(t);
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

	public long getWayid() {
		return wayid;
	}

	public void setWayid(long wayid) {
		this.wayid = wayid;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
