package acquire;

import java.io.File;
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
import org.openstreetmap.osmosis.xml.common.CompressionMethod;
import org.openstreetmap.osmosis.xml.v0_6.XmlReader;

import crosby.binary.osmosis.OsmosisSerializer;

public class MergeOsmFile {

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
			String filename = "./data/prato/prato_" + i + ".osm";
			System.out.println(filename);
			File file = new File(filename);
			XmlReader reader = new XmlReader(file, true, CompressionMethod.None);
			reader.setSink(new ReaderSink());
			reader.run();
		}

		Bound nb = bound;
		bound = new Bound(right, left, top, bottom, nb.getOrigin());
		bound.setChangesetId(nb.getChangesetId());
		bound.setVersion(nb.getVersion());
		bound.setId(nb.getId());
		bound.setTimestamp(nb.getTimestamp());
		bound.setUser(nb.getUser());

		OutputStream outputStream = new FileOutputStream("./data/prato_test.pbf");
		PbfWriter writer = new PbfWriter();
		writer.setSink(new OsmosisSerializer(new BlockOutputStream(outputStream)));
		writer.write();
		writer.complete();
	}
	
	public static void merge(String outfile, List<File> files) throws FileNotFoundException{ 
		nodes = new ArrayList<>();
		ways = new ArrayList<>();
		relations = new ArrayList<>();
		left = 180;
		bottom = 90;

		// TODO Auto-generated method stub
		for(File file:files) {
			System.out.println(file.toString());
			XmlReader reader = new XmlReader(file, true, CompressionMethod.None);
			reader.setSink(new ReaderSink());
			reader.run();
		}

		Bound nb = bound;
		bound = new Bound(right, left, top, bottom, nb.getOrigin());
		bound.setChangesetId(nb.getChangesetId());
		bound.setVersion(nb.getVersion());
		bound.setId(nb.getId());
		bound.setTimestamp(nb.getTimestamp());
		bound.setUser(nb.getUser());

		OutputStream outputStream = new FileOutputStream(outfile);
		PbfWriter writer = new PbfWriter();
		writer.setSink(new OsmosisSerializer(new BlockOutputStream(outputStream)));
		writer.write();
		writer.complete();	
	}

}
