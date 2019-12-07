package test;

import java.io.FileNotFoundException;
import java.util.Date;

import org.openstreetmap.osmosis.core.container.v0_6.BoundContainer;
import org.openstreetmap.osmosis.core.container.v0_6.NodeContainer;
import org.openstreetmap.osmosis.core.container.v0_6.RelationContainer;
import org.openstreetmap.osmosis.core.container.v0_6.WayContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.CommonEntityData;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.OsmUser;
import org.openstreetmap.osmosis.core.domain.v0_6.Relation;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;
import org.openstreetmap.osmosis.core.task.v0_6.Source;

public class PbfWriter implements Source{

    private Sink sink;
    
    @Override
    public void setSink(Sink sink) {
        this.sink = sink;
    }
 
    public void write() {
    	sink.process(new BoundContainer(MergeOSMFile.bound));
    	for (Node node : MergeOSMFile.nodes) {
    		sink.process(new NodeContainer(node));
    	}
    	for (Way way : MergeOSMFile.ways) {
    		sink.process(new WayContainer(way));
    	}
    	for (Relation relation : MergeOSMFile.relations) {
    		sink.process(new RelationContainer(relation));
    	}
    }
 
    public void complete() {
        sink.complete();
    }
 
    private CommonEntityData createEntity(int idx) {
        return new CommonEntityData(idx, 1, new Date(), new OsmUser(idx, "Amo"), idx);
    }
 
    public static void main(String[] args) throws FileNotFoundException {

    }

}
