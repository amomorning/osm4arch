package test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.openstreetmap.osmosis.core.domain.v0_6.Bound;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.Relation;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import org.openstreetmap.osmosis.osmbinary.file.BlockOutputStream;

import crosby.binary.osmosis.OsmosisReader;
import crosby.binary.osmosis.OsmosisSerializer;

public class MergeOSMFile {

	public static List<Node> nodes;
	public static List<Way> ways;
	public static List<Relation> relations;
	public static Bound bound;
	public static double right, left, top, bottom; 
	public static String origin;

	public static void main(String[] args) throws FileNotFoundException {
		nodes = new ArrayList<>();
		ways = new ArrayList<>();
		relations = new ArrayList<>();
		left = 180;
		bottom = 90;
		
		
		// TODO Auto-generated method stub
		for (int i = 0; i < 12; ++i) {
			String filename = "./data/prato/prato_" + i + ".pbf";
			System.out.println(filename);
			try {
				InputStream inputStream = new FileInputStream(filename);
				OsmosisReader reader = new OsmosisReader(inputStream);
				reader.setSink(new PbfReader());
				reader.run();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		Bound nb = bound;
		bound = new Bound(right, left, top, bottom, nb.getOrigin());
		bound.setChangesetId(nb.getChangesetId());
		bound.setVersion(nb.getVersion());
		bound.setId(nb.getId());
		bound.setTimestamp(nb.getTimestamp());
		bound.setUser(nb.getUser());

        OutputStream outputStream = new FileOutputStream("./data/prato.pbf");
        PbfWriter writer = new PbfWriter();
        writer.setSink(new OsmosisSerializer(new BlockOutputStream(outputStream)));
        writer.write();
        writer.complete();
	}


}
