package acquire;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**

 * @ClassName: Main
 * @Description: Usage of acquire package
 * @author: amomorning
 * @date: Dec 9, 2019 8:03:22 PM
 */
public class Main {

	private static final String PATHNAME = "./data/zurich";
	private static final double[] BBOX = { 8.532250000000001,47.3902,8.599625000000001,47.420249999999996 };

	public static void main(String[] args) throws FileNotFoundException {
		File path = new File(PATHNAME);
		if (!path.exists() && path.mkdirs()) {
			System.out.println(path.getName() + " is created!");
		}
		OsmDownload.setFilepath(PATHNAME);
		if (OsmDownload.getBoundary(BBOX[0], BBOX[1], BBOX[2], BBOX[3])) {

			File[] files = path.listFiles();
			List<File> toPbf = new ArrayList<>();
			for (int i = 0; i < files.length; ++i) {
				if(files[i].toString().indexOf(".osm") > -1) {
					toPbf.add(files[i]);
				}
			}
			MergeOsmFile.merge(false, PATHNAME + ".pbf", toPbf);;

		} else {
			System.err.println("ERROR");
		}

	}

}
