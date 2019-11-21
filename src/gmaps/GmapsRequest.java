package gmaps;

import java.io.IOException;
import java.util.Random;

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
//		db.deleteTable();
//		db.createTable();
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
		Random rand = new Random();
		LatLng PRATO = new LatLng(43.8830732 + rand.nextDouble()/1000.0, 11.0897498+rand.nextDouble()/1000.0);
		System.out.println("https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
				+"location="+PRATO.lat+","+PRATO.lng
				+"&radius=50"
				+"&key="+Info.API_KEY);
		try {
			PlacesSearchResponse response = PlacesApi.nearbySearchQuery(context, PRATO).radius(50).language("zh-CN").await();

			for (int i = 0; i < response.results.length; ++i) {
				LatLng position = response.results[i].geometry.location;

				String name = response.results[i].name;
				if(name.indexOf("'") != -1) name = name.replace("'", "''");

				int t = 0;
				String type = response.results[i].types[t];
				while(t < response.results[i].types.length-1 && (type.equals("establishment") || type.equals("point_of_interest")) ) type = response.results[i].types[++t];

				String values = "\'" + response.results[i].placeId + "\', " + position.lat + ", " + position.lng
						+ ", \'" + name + "\'"
						+ ", \'" + type + "\'";

				db.insertData(values);
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

	}

}
