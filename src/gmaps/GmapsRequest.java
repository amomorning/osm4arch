package gmaps;

import java.io.IOException;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlacesSearchResponse;

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
		db.deleteTable();
		db.createTable();
		gr.searchNearBy(db);
	}

	/**
	 * @Function: searchNearBy
	 * @Description: TODO
	 * @param db
	 *
	 * @return: void
	 */
	public void searchNearBy(GmapsDb db) {
		GeoApiContext context = new GeoApiContext.Builder().apiKey(Info.API_KEY).build();
		LatLng PRATO = new LatLng(Container.MAP_LAT_LNG[0], Container.MAP_LAT_LNG[1]);

		try {
			PlacesSearchResponse response = PlacesApi.nearbySearchQuery(context, PRATO).radius(50).await();
			LatLng position = response.results[0].geometry.location;

			String values = "\'" + response.results[0].placeId + "\', " + position.lat + ", " + position.lng + ", \'"
					+ response.results[0].types[0] + "\'";

			db.insertData(values);
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

	}

}
