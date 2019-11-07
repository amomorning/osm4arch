package osm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.openstreetmap.osmosis.core.container.v0_6.BoundContainer;
import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.container.v0_6.NodeContainer;
import org.openstreetmap.osmosis.core.container.v0_6.RelationContainer;
import org.openstreetmap.osmosis.core.container.v0_6.WayContainer;
import org.openstreetmap.osmosis.core.domain.common.TimestampContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.Bound;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import org.openstreetmap.osmosis.core.domain.v0_6.WayNode;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;

import crosby.binary.osmosis.OsmosisReader;
import javafx.util.Pair;
import utils.Container;
import utils.DisplayAll;
import wblut.geom.WB_Point;


public class PbfReader implements Sink {
    private int WayCount;
    private int NodeCount;
    private int RelationCount;
	
	@Override
	public void initialize(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		WayCount = 0;
		NodeCount = 0;
		RelationCount = 0;
	    System.out.println("this is initialize.");
		
	}

	@Override
	public void complete() {
		// TODO Auto-generated method stub
	    System.out.println("this is complete.");
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void process(EntityContainer entityContainer) {
		// TODO Auto-generated method stub
        if (entityContainer instanceof NodeContainer) {
            // Nothing to do here
        	Node myNode = ((NodeContainer)entityContainer).getEntity();
//        	System.out.println("node = " + myNode.getId() + "lat = " + myNode.getLatitude() + " lon = " + myNode.getLongitude()) ;
        	Container.nodeid.put(myNode.getId(), NodeCount++);
        
        	Container.points.add(new WB_Point(GeoMath.lonLatToXY(myNode.getLongitude(), myNode.getLatitude())));
        } else if (entityContainer instanceof WayContainer) {

        	Container.edges.add(new Pair<>(1, 1));
//            Way myWay = ((WayContainer) entityContainer).getEntity();
//            boolean building = false;
//            for (Tag myTag : myWay.getTags()) {
//                if ("building".equalsIgnoreCase(myTag.getKey())) {
//                	if("house".equalsIgnoreCase(myTag.getValue())) {
//                		cnt ++;
//                     System.out.println("It's a apartment: " + myWay.getId());
//                    
//                	List<WayNode> nodes = myWay.getWayNodes();
//                	for(WayNode node:nodes) {
//                		System.out.println("nodeid = " + node.getNodeId());
////                		System.out.println("lat = " + node.getLatitude() + " lon = " + node.getLongitude());
//                	}
//                	}
//                	building = true;
//                }
//            }
//            if(building) {
//            	
//            }
        } else if (entityContainer instanceof RelationContainer) {
            // Nothing to do here
        } else if(entityContainer instanceof BoundContainer) {
            Bound myBound = ((BoundContainer)entityContainer).getEntity();
            TimestampContainer tc = myBound.getTimestampContainer();
            Date data = tc.getTimestamp();
            System.out.println("data = " + data);
            
            double yy = GeoMath.latToMercator(myBound.getLeft());
            double left = GeoMath.mercatorToLat(yy);

            double x = (myBound.getBottom() + myBound.getTop())/2.0;
            double y = (myBound.getLeft() + myBound.getRight())/2.0;
            System.out.println(x + ", " + y);

            
            double[] bl = GeoMath.lonLatToXY(myBound.getBottom(), myBound.getLeft());
            double[] tr = GeoMath.lonLatToXY(myBound.getTop(), myBound.getRight());
            System.out.println(bl[0] + " " + bl[1]+ "\n" + tr[0] + " " + tr[1]);
        }
        else {
            System.out.println("Unknown Entity!");
        }
	}

}
