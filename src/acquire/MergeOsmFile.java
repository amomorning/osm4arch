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

import crosby.binary.osmosis.OsmosisReader;
import crosby.binary.osmosis.OsmosisSerializer;

/**

 * @ClassName: MergeOsmFile
 * @Description: Merge several OSM/PBF files
 * @author: amomorning
 * @date: Dec 9, 2019 8:03:56 PM
 */
public class MergeOsmFile {

	public static List<Node> nodes;
	public static List<Way> ways;
	public static List<Relation> relations;
	public static Bound bound;
	public static double right, left, top, bottom;
	public static String origin;


	/**
	 * @Function: merge
	 * @Description: Static function to merge files
	 * @param isPbf true uses pbf reader method, false uses xml reader method.
	 * @param outfile merged output file name
	 * @param files a list of raw osm files
	 * @throws FileNotFoundException
	 *
	 * @return: void
	 */
	public static void merge(boolean isPbf, String outfile, List<File> files) throws FileNotFoundException {
		nodes = new ArrayList<>();
		ways = new ArrayList<>();
		relations = new ArrayList<>();
		
		top = -90;
		right = -180;
		left = 180;
		bottom = 90;

		for (File file : files) {
			if (isPbf == false) {
				XmlReader reader = new XmlReader(file, true, CompressionMethod.None);
				reader.setSink(new ReaderSink());
				reader.run();
			} else {
				OsmosisReader reader = new OsmosisReader(new FileInputStream(file));
				reader.setSink(new ReaderSink());
				reader.run();
			}
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
