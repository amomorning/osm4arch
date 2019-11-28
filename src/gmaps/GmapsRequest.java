package gmaps;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;

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
		GmapsRequest gr = new GmapsRequest();
		GmapsDb db = new GmapsDb();

		for (PlaceType type : PlaceType.values()) {
			if (GmapsTypeDetail.map.get(type.toUrlValue()) == null) {
				System.err.println(type.toUrlValue() + " null");
			} else
				System.out.println(type.toUrlValue() + " " + GmapsTypeDetail.map.get(type.toUrlValue()));

		}

		db.deleteTable();
		db.createTable();

		GeoApiContext context = new GeoApiContext.Builder().apiKey(Info.API_KEY).build();
		Random rand = new Random();
		for (int i = 0; i < 3; ++i) {
			LatLng PRATO = new LatLng(43.8830732 + rand.nextDouble() / 1000.0, 11.0897498 + rand.nextDouble() / 1000.0);
			int tot = gr.searchNearBy(db, context, PRATO);
			System.out.println("-------------\n" + tot + " at position " + PRATO + "\n--------------");
		}
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return total;
	}

	private void resultToDb(GmapsDb db, PlacesSearchResult result) {
		LatLng position = result.geometry.location;

		String name = result.name;
		if (name.indexOf("'") != -1)
			name = name.replace("'", "''");

		String values = "\'" + result.placeId + "\', " + position.lat + ", " + position.lng + ", " + result.rating
				+ ", " + result.userRatingsTotal + ", \'" + name + "\'";

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

}
