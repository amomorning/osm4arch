package test;

import java.util.Map;

import org.openstreetmap.osmosis.core.container.v0_6.BoundContainer;
import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.container.v0_6.NodeContainer;
import org.openstreetmap.osmosis.core.container.v0_6.RelationContainer;
import org.openstreetmap.osmosis.core.container.v0_6.WayContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.Bound;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.Relation;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;

public class PbfReader implements Sink {

	@Override
	public void initialize(Map<String, Object> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void complete() {

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void process(EntityContainer entityContainer) {

		if (entityContainer instanceof NodeContainer) {

			Node myNode = ((NodeContainer) entityContainer).getEntity();
			MergeOSMFile.nodes.add(myNode);

		}
		else if (entityContainer instanceof WayContainer) {

//			System.out.println("Way"+Container.wayCount);
			// Get all geometry ways
			Way myWay = ((WayContainer) entityContainer).getEntity();
			MergeOSMFile.ways.add(myWay);
		}
		else if (entityContainer instanceof RelationContainer) {
			// Nothing to do here
			Relation myRelation = ((RelationContainer) entityContainer).getEntity();
			MergeOSMFile.relations.add(myRelation);
		}
		else if (entityContainer instanceof BoundContainer) {
			Bound myBound = ((BoundContainer) entityContainer).getEntity();
			MergeOSMFile.left = Math.min(MergeOSMFile.left, myBound.getLeft());
			MergeOSMFile.bottom = Math.min(MergeOSMFile.bottom, myBound.getBottom());
			MergeOSMFile.right = Math.max(MergeOSMFile.right, myBound.getRight());
			MergeOSMFile.top = Math.max(MergeOSMFile.top, myBound.getTop());
			MergeOSMFile.bound = myBound;
		} else {
			System.out.println("Unknown Entity!");
		}
	}

}
