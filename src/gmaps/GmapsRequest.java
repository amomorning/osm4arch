package gmaps;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;

import osm.GeoMath;
import utils.Container;

/**
 * 
 * @ClassName: GmapsRequest
 * @Description: TODO
 * @author: amomorning
 * @date: Nov 20, 2019 10:07:14 PM
 */
public class GmapsRequest {

	public static void main(String[] args) {

		for (PlaceType type : PlaceType.values()) {
			if (GmapsTypeDetail.map.get(type.toUrlValue()) == null) {
				System.err.println(type.toUrlValue() + " null");
			} else
				System.out.println(type.toUrlValue() + " " + GmapsTypeDetail.map.get(type.toUrlValue()));

		}

//		db.deleteTable();
//		db.createTable();

		GmapsRequest gr = new GmapsRequest();
		try {
			gr.gridSearch(Container.SW_LAT_LNG, Container.NE_LAT_LNG, 50);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void gridSearch(double[] bl, double[] tr, int radius) throws IOException {
		GeoMath geoMath = new GeoMath(Container.MAP_LAT_LNG);
		double[] min = geoMath.latLngToXY(bl);
		double[] max = geoMath.latLngToXY(tr);

		GmapsDb db = new GmapsDb();
		GeoApiContext context = new GeoApiContext.Builder().apiKey(Info.API_KEY).build();

		File file = new File("./data/number_of_result.txt");
		FileWriter out = new FileWriter(file);

		double step = Math.floor(radius * Math.sqrt(2.0));
//		double step = 3000;
		for (double x = min[0] + step / 2; x < max[0]; x += step) {
			for (double y = min[1] + step / 2; y < max[1]; y += step) {
				double[] latlng = geoMath.xyToLatLng(x, y);
				LatLng position = new LatLng(latlng[0], latlng[1]);
				int tot = searchNearBy(db, context, position, radius);
				System.out.println("( " + x + " " + y + " )");
				System.out.println("-------------\n" + tot + " at position " + position + "\n--------------");
			}
			out.write("\r\n");
		}
		out.close();
	}

	/**
	 * @Function: searchNearBy
	 * @Description: TODO
	 * @param db
	 *
	 * @return: void
	 */
	public int searchNearBy(GmapsDb db, GeoApiContext context, LatLng position, int radius) {
		System.out.println("https://maps.googleapis.com/maps/api/place/nearbysearch/json?" + "location=" + position.lat
				+ "," + position.lng + "&radius=" + radius + "&key=" + Info.API_KEY);
		int total = 0;
		try {
			PlacesSearchResponse response = PlacesApi.nearbySearchQuery(context, position).radius(radius)
					.language("zh-CN").await();

			for (int i = 0; i < response.results.length; ++i) {
				resultToDb(db, response.results[i]);
			}
			total = response.results.length;

			total += searchNextPage(db, context, response.nextPageToken);
		} catch (ApiException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return total;
	}

	public int searchNextPage(GmapsDb db, GeoApiContext context, String nextPageToken) {
		int total = 0;
		try {
			while (nextPageToken != null) {
				TimeUnit.SECONDS.sleep(10);
				System.out.println("https://maps.googleapis.com/maps/api/place/nearbysearch/json?" + "pagetoken="
						+ nextPageToken + "&key=" + Info.API_KEY);
				PlacesSearchResponse response = PlacesApi.nearbySearchNextPage(context, nextPageToken).await();

				for (int i = 0; i < response.results.length; ++i) {
					resultToDb(db, response.results[i]);
				}

				total += response.results.length;	
				nextPageToken = response.nextPageToken;
				
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return total;
	}

	/**
	 * @Function: resultToDb
	 * @Description: TODO
	 * @param db
	 * @param result
	 *
	 * @return: void
	 */
	private void resultToDb(GmapsDb db, PlacesSearchResult result) {
		LatLng position = result.geometry.location;

		String name = result.name;
		Boolean isChinese = false;

		if (isContainChinese(name))
			isChinese = true;
		if (name.indexOf("'") != -1)
			name = name.replace("'", "''");

		if (name.length() > 255)
			name = name.substring(0, 255);

		String values = "\'" + result.placeId + "\', " + position.lat + ", " + position.lng + ", " + result.rating
				+ ", " + result.userRatingsTotal + "," + isChinese + ", \'" + name + "\'";

		GmapsTypeDetail.Types type = null;
		String typeDetail = null;
		for (int i = 0; i < result.types.length; ++i) {
			typeDetail = result.types[i];
			type = GmapsTypeDetail.map.get(typeDetail);
			if (type != null)
				break;
		}

		values += ", \'" + type + "\'" + ", \'" + typeDetail + "\'";

		db.insertData(values);
	}

	/**
	 * @Function: isContainChinese
	 * @Description: TODO
	 * @param str
	 * @return
	 *
	 * @return: boolean
	 */
	public boolean isContainChinese(String str) {
		if (str.indexOf("Chinese") != -1)
			return true;
		if (str.indexOf("chinese") != -1)
			return true;
		if (str.indexOf("China") != -1)
			return true;
		if (str.indexOf("china") != -1)
			return true;
		if (str.indexOf("Cinese") != -1)
			return true;
		if (str.indexOf("cinese") != -1)
			return true;
		if (str.indexOf("Cina") != -1)
			return true;
		if (str.indexOf("cina") != -1)
			return true;

		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
	}

}
