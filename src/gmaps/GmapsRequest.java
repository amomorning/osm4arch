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

	public void gridSearch(double[] bl, double[] tr, double radius) throws IOException {
		double[] min = GeoMath.latLngToXY(bl);
		double[] max = GeoMath.latLngToXY(tr);

		GmapsDb db = new GmapsDb();
		GeoApiContext context = new GeoApiContext.Builder().apiKey(Info.API_KEY).build();

		File file = new File("./data/number_of_result.txt");
		FileWriter out = new FileWriter(file);

//		double step = Math.floor(radius * Math.sqrt(2.0));
		double step = 3000;
		for (double x = min[0] + step / 2; x < max[0]; x += step) {
			for (double y = min[1] + step / 2; y < max[1]; y += step) {
				double[] latlng = GeoMath.xyToLatLng(x, y);
				LatLng position = new LatLng(latlng[0], latlng[1]);
				int tot = searchNearBy(db, context, position);
				System.out.println("( " + x + " " + y + " )");
				System.out.println("-------------\n" + tot + " at position " + position + "\n--------------");
				if (y + step < max[1])
					out.write(tot + ", ");
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
	public int searchNearBy(GmapsDb db, GeoApiContext context, LatLng position) {
		System.out.println("https://maps.googleapis.com/maps/api/place/nearbysearch/json?" + "location=" + position.lat
				+ "," + position.lng + "&radius=50" + "&key=" + Info.API_KEY);
		int total = 0;
		try {
			PlacesSearchResponse response = PlacesApi.nearbySearchQuery(context, position).radius(50).language("zh-CN")
					.await();

			for (int i = 0; i < response.results.length; ++i) {
				resultToDb(db, response.results[i]);
			}
			total = response.results.length;

			while (response.nextPageToken != null) {
				TimeUnit.SECONDS.sleep(10);
				System.out.println("https://maps.googleapis.com/maps/api/place/nearbysearch/json?" + "pagetoken="
						+ response.nextPageToken + "&key=" + Info.API_KEY);
				response = PlacesApi.nearbySearchNextPage(context, response.nextPageToken).await();

				for (int i = 0; i < response.results.length; ++i) {
					resultToDb(db, response.results[i]);
				}

				total += response.results.length;
			}
		} catch (ApiException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
