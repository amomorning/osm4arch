package evaluate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.csvreader.CsvWriter;

import osm.GeoMath;
import osm.OsmTypeDetail;
import utils.Aoi;
import utils.Container;
import wblut.geom.WB_Coord;
import wblut.geom.WB_CoordCollection;
import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;
import wblut.geom.WB_Segment;

public class StreetAnalysis {
	
	private double sampleDistance = 20;
	
	private static class AnalysisLoader {
		private static final StreetAnalysis instance = new StreetAnalysis();
	}
	
	public static StreetAnalysis getInstance() {
		return AnalysisLoader.instance;
	}

	public static List<WB_Point> getSamplePoint(List<WB_PolyLine> plys) {
		List<WB_Point> ret = new ArrayList<>();
		for (WB_PolyLine ply : plys) {
			WB_CoordCollection pts = ply.getPoints();
			for (int i = 0; i < pts.size(); ++i) {
				ret.add(new WB_Point(pts.get(i)));
			}
		}
		return ret;
	}


	public List<WB_Point> writeSamplePoint(String filename) throws IOException {
		double step = 30;

		GeoMath geoMath = new GeoMath(Container.MAP_LAT_LNG);
		double[] min = geoMath.latLngToXY(Container.SW_LAT_LNG);
		double[] max = geoMath.latLngToXY(Container.NE_LAT_LNG);

		int n = (int) ((max[0] - min[0]) / step + 1);
		int m = (int) ((max[1] - min[1]) / step + 1);

		System.out.println("n = " + n + ", m = " + m);
		System.out.println("[" + min[0] + ", " + max[0] + "]");
		System.out.println("[" + min[1] + ", " + max[1] + "]");

		CsvWriter csvWriter = new CsvWriter(filename, ',', Charset.forName("UTF-8"));

		String[] header = { "lat", "lng", "wayid" };
		csvWriter.writeRecord(header);

		int[][] cnt = new int[n][m];
		for (int i = 0; i < n; ++i)
			for (int j = 0; j < m; ++j)
				cnt[i][j] = 0;

		List<WB_Point> ret = new ArrayList<>();
		for (Aoi aoi : Container.aois) {
			if (!aoi.isHighway)
				continue;
			String key = aoi.getTags().get("highway");
			if (key.indexOf("link") > -1)
				continue;
			if (OsmTypeDetail.roadMap.containsKey(key) && OsmTypeDetail.roadMap.get(key) != OsmTypeDetail.Road.R1
					&& OsmTypeDetail.roadMap.get(key) != OsmTypeDetail.Road.S3) {

				WB_CoordCollection pts = aoi.getPly().getPoints();

				for (int i = 0; i < pts.size(); ++i) {

					if (i + 1 < pts.size()) {
						WB_Coord p0 = pts.get(i);
						WB_Coord p1 = pts.get(i+1);
						List<WB_Point> points = distDivideLine(p0, p1);
						for(WB_Point pt : points) {

							int x = (int) ((pt.xd() - min[0]) / step);
							int y = (int) ((pt.yd() - min[1]) / step);

							if(x < 0 || y < 0 || x >= n || y >= m) continue;
							if(cnt[x][y] > 0) continue;
							cnt[x][y] ++;

							ret.add(pt);
							double[] latlng = geoMath.xyToLatLng(pt.xd(), pt.yd());
							String[] content = {
									Double.toString(latlng[0]),
									Double.toString(latlng[1]),
									Long.toString(aoi.getWayid())
							};
							csvWriter.writeRecord(content);
						}
					}
				}
			}
		}
		csvWriter.close();
		return ret;
	}

	
	public List<WB_Point> distDivideLine(WB_Coord p0, WB_Coord p1) {
		WB_Segment seg = new WB_Segment(p0, p1);

		double length = seg.getLength();
		int n = (int) ( length / sampleDistance );

		List<WB_Point> ret = new ArrayList<>();
		ret.add(new WB_Point(p0));
		for(int i = 1; i <= n; ++ i) {
			double t = sampleDistance * i / length;
//			System.out.println(t);
			ret.add(seg.getParametricPoint(t));
		}
		return ret;
	}
}
