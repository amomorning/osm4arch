package acquire;

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

public class ReaderSink implements Sink {

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
			MergeOsmFile.nodes.add(myNode);

		}
		else if (entityContainer instanceof WayContainer) {

//			System.out.println("Way"+Container.wayCount);
			// Get all geometry ways
			Way myWay = ((WayContainer) entityContainer).getEntity();
			MergeOsmFile.ways.add(myWay);
		}
		else if (entityContainer instanceof RelationContainer) {
			// Nothing to do here
			Relation myRelation = ((RelationContainer) entityContainer).getEntity();
			MergeOsmFile.relations.add(myRelation);
		}
		else if (entityContainer instanceof BoundContainer) {
			Bound myBound = ((BoundContainer) entityContainer).getEntity();
			MergeOsmFile.left = Math.min(MergeOsmFile.left, myBound.getLeft());
			MergeOsmFile.bottom = Math.min(MergeOsmFile.bottom, myBound.getBottom());
			MergeOsmFile.right = Math.max(MergeOsmFile.right, myBound.getRight());
			MergeOsmFile.top = Math.max(MergeOsmFile.top, myBound.getTop());
			MergeOsmFile.bound = myBound;
		} else {
			System.out.println("Unknown Entity!");
		}
	}

}
