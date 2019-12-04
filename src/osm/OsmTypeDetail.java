package osm;

import java.util.HashMap;
import java.util.Map;

public class OsmTypeDetail {
	public enum Road {
		R1(), 
		R2("trunck"), 
		S1(), 
		S2(), 
		S3("foot", "footway");

		private String[] string;

		Road(String... strs) {
			this.string = strs;
		}

		public String[] getString() {
			return string;
		}
	}

	public static final Map<String, Road> roadMap;
	static {
		roadMap = new HashMap<String, Road>();
		for (Road road : Road.values()) {
			String[] str = road.getString();
			for (int i = 0; i < str.length; ++i) {
				roadMap.put(str[i], road);
			}
		}
	}
}
