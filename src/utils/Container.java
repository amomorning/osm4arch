package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Multimap;

import crosby.binary.osmosis.OsmosisReader;
import gmaps.GmapsDb;
import osm.PbfReader;
import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;

public class Container {
//    public static String OSM_FILENAME = "./data/part_prato.osm.pbf";
	public static double[] MAP_LAT_LNG = new double[] { 43.8783119, 11.08387555 };
	public static double[] NE_LAT_LNG = new double[] { 43.9252112, 11.1474862 };
	public static double[] SW_LAT_LNG = new double[] { 43.8314126, 11.0202649 };
	public static long[] TIME_MIN_MAX = new long[] { 1215035100000L, 1572163411000L };

//    public static final String OSM_FILENAME = "./data/centro-latest.osm.pbf";
	public static final String OSM_FILENAME = "./data/planet_10.976,43.831_11.181,43.922.osm.pbf";
//    public static final double[] BASE = new double[] { 11.0913501, 43.88255};
	public static final int UTMZONE = 32;
	public static final char UTMLETTER = 'T';

	public static BiMap<Long, Integer> wayid;
	public static BiMap<Long, Integer> nodeid;
	public static int wayCount;
	public static int nodeCount;

	public static List<WB_Point> points;
	public static List<WB_PolyLine> plys;

	public static List<Gpoi> gpois;
	public static List<Poi> pois;
	public static List<Aoi> aois;

	public static Multimap<String, String> tagList;

	public static void initAll() {
		initContainer();
		initGmaps();
		initOsm();
	}

	public static void initContainer() {
		Tools.timerStart();
		points = new ArrayList<>();
		nodeid = HashBiMap.create();
		wayid = HashBiMap.create();

		plys = new ArrayList<>();
		pois = new ArrayList<>();
		aois = new ArrayList<>();

		tagList = ArrayListMultimap.create();
		Tools.timerShow("CONTAINER INIT");
	}

	public static void initGmaps() {

		gpois = new ArrayList<>();
		Tools.timerStart();
		GmapsDb db = new GmapsDb();
		db.collectData();
		Tools.timerShow("DATABASE");
	}

	public static void initOsm() {


		Tools.timerStart();
		wayCount = 0;
		nodeCount = 0;
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

		Tools.timerShow("OSM_READER");

	}

}
