package acquire;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Main {

	private static final String PATHNAME = "./data/nanjing";
	private static final double[] BBOX = { 118.7062, 32.0020, 118.9758, 32.1231 };

	public static void main(String[] args) throws FileNotFoundException {
		File path = new File(PATHNAME);
		if (!path.exists() && path.mkdirs()) {
			System.out.println(path.getName() + " is created!");
		}
//
		OsmDownload.setFilepath(PATHNAME);
		// WARNING: find (latitude, longitude) swapped in Europe, 
		// 			need to manually check if you can get right result...
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
