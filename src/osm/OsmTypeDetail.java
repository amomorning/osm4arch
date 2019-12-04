package osm;

import java.util.HashMap;
import java.util.Map;

import utils.ColorHelper;

public class OsmTypeDetail {
	public enum Road {
		R1("motorway", "motorway_link"), 
		R2("trunk", "primary", "secondary", "trunk_link", "primary_link", "secondary_link"), 
		S1("tertiary", "tertiary_link"), 
		S2("unclassified", "residential", "living_street", "road"), 
		S3("foot", "footway",  "service", 	"pedestrian", "steps", "cycleway");

		private String[] string;

		Road(String... strs) {
			this.string = strs;
		}

		public String[] getString() {
			return string;
		}
	}

	public static final Map<String, Road> roadMap;
	public static final Map<Road, int[]> roadColor;
	static {
		roadMap = new HashMap<>();
		for (Road road : Road.values()) {
			String[] str = road.getString();
			for (int i = 0; i < str.length; ++i) {
				roadMap.put(str[i], road);
			}
		}

		int[][] c = ColorHelper.createGradientHue(Road.values().length, ColorHelper.RED, ColorHelper.BLUE);
		roadColor = new HashMap<>();
		for(int i = 0; i < Road.values().length; ++ i) {
			roadColor.put(Road.values()[i], c[i]);
		}
	}
}
