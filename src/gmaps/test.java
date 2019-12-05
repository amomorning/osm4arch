package gmaps;

import com.google.maps.GeoApiContext;
import com.google.maps.model.LatLng;

import osm.GeoMath;
import utils.Container;

public class test {

	public static void main(String[] args) throws Exception {
		GeoMath geoMath = new GeoMath(Container.MAP_LAT_LNG);
		double[] min = geoMath.latLngToXY(Container.SW_LAT_LNG);
		double[] max = geoMath.latLngToXY(Container.NE_LAT_LNG);
		int radius = 50;
		double step = Math.floor(radius * Math.sqrt(2.0));

		int dx = (int) ((max[0] - min[0]) / step);
		int dy = (int) ((max[1] - min[1]) / step);

		System.out.println(dx + " " + dy + " " + dx * dy);

		double[] pos = { 1211.8300000000745, 89.0800000000745 };

		int x = (int) ((pos[0] - min[0] - step / 2) / step);
		int y = (int) ((pos[1] - min[1] - step / 2) / step) + 1;

		System.out.println(x + " " + y + " " + (x * dy + y));
		GmapsRequest gr = new GmapsRequest();
		GmapsDb db = new GmapsDb();
		GeoApiContext context = new GeoApiContext.Builder().apiKey(Info.API_KEY).build();

//		for (double yy = min[1] + step / 2 + y * step; yy < max[1]; yy += step) {
//			double[] latlng = GeoMath.xyToLatLng(min[0] + step / 2 + x * step, yy);
//			LatLng position = new LatLng(latlng[0], latlng[1]);
//			int num = gr.searchNearBy(db, context, position);
//		}

		double[][] lossPos = new double[][] { { 43.8643515, 11.1289824 }, { 43.8618142, 11.128892 },
				{ 43.9150316, 11.1343014 }, { 43.9048825, 11.1339386 }, { 43.8693607, 11.1326703 },
				{ 43.8590804, 11.1393216 }, { 43.8463283, 11.1423738 } };

		int cnt = 0, tot = 0;

//		for (int i = 0; i < lossPos.length; ++i) {
//			double[] p = GeoMath.latLngToXY(lossPos[i]);
//			for (double xx = p[0] - 50; xx < p[0] + 60; xx += 100) {
//				for (double yy = p[1] - 50; yy < p[1] + 60; yy += 100) {
//					double[] latlng = GeoMath.xyToLatLng(xx, yy);
//					LatLng position = new LatLng(latlng[0], latlng[1]);
//					int num = gr.searchNearBy(db, context, position, radius);
//					cnt ++;
//					tot += num;
//					System.out.println("num = " + num);
//				}
//			}
//		}
		
		String nextPageToken = new String("CqQCFQEAAKVnqeVjuUh5w7rgAbxtP5gH8uTqySbbi4M-x5LPz-szlwz7DD8KbgbT20AF3OtAwGWKKzUN9Sn_HObdUB2ZQIPT0r8ZxkjRJPj-Plpfy8W9kjPqrN_EXZDJ7OaeFraMmID2NJUZCQbt5o5Ai6A5nu0iEyenGGhyn3_xLIDJdnMY9-bHC40p_rEEi_wWKXdcfuhfLyD8oyC1c6CgpANh9nPubfO6kZOAl1INXDDDq_5FFvtPvs0Lzc2eEhhoJB4rmb5E3piISX1Ca8pZqF-rpJHqv7Ziaj8PMRmZA3OMfvlF6930hgPHtH8W5o5xcMQfMH3n79aVxizy9J_IOcRSJxPX-0SL5NyLPnwdM8N8b__-al7TqukkCqeFq5XM-J6XLxIQ4oXqCYvgkDLPJnA9XB13wxoUwDpj8el0vI7jOv3hvdOyVo-nabM-bI3UlMMZYWxOqL4dRgTxpxu_2XNNYr1x6uI349_3lD1iLaXT_B3HgLaxQOzGFMA_Gb4oOXVxI5DjDfzYShNfAsa8-9XpjP1bg4l41LmnZEuGtznxK1wsDBL0yzrE8rB_RphN4nBIpOdXCTCxSgWNrXtcxAlaY9jM8Unb69h2oxWTJQedR5tXc8uhdayTBco6hHVPSm8SeOIlnQSkHi1UdcrX_J9AKT6oOrfNMmCgyqiBihGqpZ2mHa4YvlCXsu0Xcdjql0sXgZG3ysAKySwDszagJXgwkIKyoAAYJ6BIQFuEfDAPYedEh--UM6G2SJBoUZ506baGg47Q_VbSIUWLTiqCjS_U"); 
				
		
		tot += gr.searchNextPage(db, context, nextPageToken);

//		for (double xx = 3522.8300000000745 + step / 2; xx < max[0]; xx += step) {
//			for (double yy = min[0] + step / 2; yy < max[1]; yy += step) {
//
//				System.out.println("{ " + xx + ", " + yy + " )");
////
//				double[] latlng = GeoMath.xyToLatLng(xx, yy);
//				LatLng position = new LatLng(latlng[0], latlng[1]);
//				int num = gr.searchNearBy(db, context, position, radius);
//				cnt++;
//				tot += num;
//				System.out.println("Num = " + num);
//			}
//			System.out.println("--------------");
//		}

		System.out.println("Total costs " + cnt * 40 + "\nTotal num " + tot);
	}

}
