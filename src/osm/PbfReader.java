package osm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.openstreetmap.osmosis.core.container.v0_6.BoundContainer;
import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.container.v0_6.NodeContainer;
import org.openstreetmap.osmosis.core.container.v0_6.RelationContainer;
import org.openstreetmap.osmosis.core.container.v0_6.WayContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.Bound;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import org.openstreetmap.osmosis.core.domain.v0_6.WayNode;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;

import utils.Aoi;
import utils.Container;
import utils.Poi;
import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;

public class PbfReader implements Sink {
	private int RelationCount;

	long cnta;
	long cntb;

	GeoMath geoMath;

	@Override
	public void initialize(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		RelationCount = 0;
		System.out.println("this is initialize.");

		cnta = Long.MIN_VALUE;
		cntb = Long.MAX_VALUE;
	}

	@Override
	public void complete() {
//	    System.out.println("this is complete.");
		System.out.println("OSM_TIMESTAMP_RANGE [" + cntb + ", " + cnta + "]\n");

		System.out.println(Container.points.size() + " numbers of points is readed.");

		System.out.println("WayCount = " + Container.wayCount);
		System.out.println("NodeCount = " + Container.nodeCount);
		System.out.println("RelationCount = " + RelationCount);

		System.out.println(Container.points.get(0));
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void process(EntityContainer entityContainer) {

		if (entityContainer instanceof NodeContainer) {

			Node myNode = ((NodeContainer) entityContainer).getEntity();
//			System.out.println("Node" + Container.nodeCount);
			if (Container.nodeid.containsKey(myNode.getId()) == false) {
				WB_Point pts = new WB_Point(geoMath.latLngToXY(myNode.getLatitude(), myNode.getLongitude()));
				Container.nodeid.put(myNode.getId(), Container.nodeCount++);
				Container.points.add(pts);

				if (myNode.getTags().size() > 0) {
					Poi poi = new Poi(pts);
					for (Tag tag : myNode.getTags()) {
						String key = tag.getKey();
						String value = tag.getValue();
						poi.addTag(key, value);
//                    if(! Container.tagList.containsEntry(key, value)) {
//                    	Container.tagList.put(key, value);
//                    }
					}

					if (myNode.getTimestamp() != null) {
						cnta = (cnta > myNode.getTimestamp().getTime()) ? cnta : myNode.getTimestamp().getTime();
						cntb = (cntb < myNode.getTimestamp().getTime()) ? cntb : myNode.getTimestamp().getTime();
						poi.setDate(myNode.getTimestamp());
//                    System.out.println(myNode.getTimestamp().getTime());
					}
					Container.pois.add(poi);
				}

			}
		}

		else if (entityContainer instanceof WayContainer) {

//			System.out.println("Way"+Container.wayCount);
			// Get all geometry ways
			Way myWay = ((WayContainer) entityContainer).getEntity();

			if (Container.wayid.containsKey(myWay.getId()) == false) {
				List<WayNode> nodeList = myWay.getWayNodes();

				ArrayList<WB_Point> pts = new ArrayList<>();
				for (WayNode node : nodeList) {
					Integer id = Container.nodeid.get(node.getNodeId());
					pts.add(Container.points.get(id));
				}
				WB_PolyLine ply = new WB_PolyLine(pts);

				Container.wayid.put(myWay.getId(), Container.wayCount++);
				Container.plys.add(ply);
				if (myWay.isClosed() == true) {
					pts.remove(pts.size() - 1);
				}

				// Get Piazza

				// Get Area of Interest
				if (myWay.getTags().size() > 0) {

					Aoi aoi = new Aoi(ply, myWay.isClosed());
					for (Tag tag : myWay.getTags()) {
						String key = tag.getKey();
						String value = tag.getValue();
						if(key.equals("highway")) aoi.isHighway = true;
						if(key.equals("building")) aoi.isBuilding = true;
						aoi.addTag(key, value);
						if (!Container.tagList.containsEntry(key, value)) {
							Container.tagList.put(key, value);
						}
					}
					if (myWay.getTimestamp().getTime() != -1) {
						cnta = (cnta > myWay.getTimestamp().getTime()) ? cnta : myWay.getTimestamp().getTime();
						cntb = (cntb < myWay.getTimestamp().getTime()) ? cntb : myWay.getTimestamp().getTime();
//                    System.out.println(myWay.getTimestamp().getTime());
						aoi.setDate(myWay.getTimestamp());
					}
					Container.aois.add(aoi);
				}
			}
		}

		else if (entityContainer instanceof RelationContainer) {
			// Nothing to do here

		}

		else if (entityContainer instanceof BoundContainer) {
			Bound myBound = ((BoundContainer) entityContainer).getEntity();

			double y = (myBound.getBottom() + myBound.getTop()) / 2.0;
			double x = (myBound.getLeft() + myBound.getRight()) / 2.0;
			System.out.println("OSM_CENTER_LATLNG [" + x + ", " + y + "]");
			
			double[] center = new double[] {y, x};
			geoMath = new GeoMath(center);
			Container.MAP_LAT_LNG = center;
			Container.SW_LAT_LNG = new double[] {myBound.getBottom(), myBound.getLeft()};
			Container.NE_LAT_LNG = new double[] {myBound.getTop(), myBound.getRight()};
			System.out.println(Container.MAP_LAT_LNG[0] + " " + Container.MAP_LAT_LNG[1]);
			System.out.println(Container.SW_LAT_LNG[0] + " " + Container.SW_LAT_LNG[1]);
			System.out.println(Container.NE_LAT_LNG[0] + " " + Container.NE_LAT_LNG[1]);

		} else {
			System.out.println("Unknown Entity!");
		}
	}

}
