package acquire;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Main {

	private static final String PATHNAME = "./data/pratotest";
	private static final double[] BBOX = { 43.8314126, 11.0202649, 43.9252112, 11.1474862 };

	public static void main(String[] args) throws FileNotFoundException {
		File path = new File(PATHNAME);
		if (!path.exists() && path.mkdirs()) {
			System.out.println(path.getName() + "is created!");
		}
//
		OsmDownload.setFilepath(PATHNAME);
		// WARNING: find (latitude, longitude) swapped in Europe, 
		// 			need to manually check if you can get right result...
		if (OsmDownload.getBoundary(BBOX[1], BBOX[0], BBOX[3], BBOX[2])) {

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
