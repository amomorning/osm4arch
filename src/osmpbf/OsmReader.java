package osmpbf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.container.v0_6.NodeContainer;
import org.openstreetmap.osmosis.core.container.v0_6.RelationContainer;
import org.openstreetmap.osmosis.core.container.v0_6.WayContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import org.openstreetmap.osmosis.core.domain.v0_6.WayNode;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;

import crosby.binary.osmosis.OsmosisReader;


public class OsmReader implements Sink {
	 int cnt = 0;

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
        InputStream inputStream = new FileInputStream("./data/planet_10.92,43.81_11.2627,43.9551.osm.pbf");
        OsmosisReader reader = new OsmosisReader(inputStream);
        reader.setSink(new OsmReader());
        reader.run();
	}

	@Override
	public void initialize(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void complete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
            System.out.println("Total = " + cnt);
	}

	@Override
	public void process(EntityContainer entityContainer ) {
		// TODO Auto-generated method stub
        if (entityContainer instanceof NodeContainer) {
            // Nothing to do here
        } else if (entityContainer instanceof WayContainer) {
            Way myWay = ((WayContainer) entityContainer).getEntity();
            for (Tag myTag : myWay.getTags()) {
                if ("building".equalsIgnoreCase(myTag.getKey())) {
                	if("house".equalsIgnoreCase(myTag.getValue())) {
                		cnt ++;
                     System.out.println("It's a apartment: " + myWay.getId());
                    
                	List<WayNode> nodes = myWay.getWayNodes();
                	for(WayNode node:nodes) {

                		System.out.println("lat = " + node.getLatitude() + " lon = " + node.getLongitude());
                	}
                	}
                }
            }
        } else if (entityContainer instanceof RelationContainer) {
            // Nothing to do here
        } else {
            System.out.println("Unknown Entity!");
        }
	}

}
