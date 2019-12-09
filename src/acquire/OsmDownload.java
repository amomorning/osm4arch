package acquire;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**

 * @ClassName: OsmDownload
 * @Description: Recursive OSM file downloader from osm website(https://www.openstreetmap.org) 
 * @author: amomorning
 * @date: Dec 9, 2019 8:06:53 PM
 */
public class OsmDownload {

	private static String PATHNAME = null;

	public static boolean getBoundary(double minLng, double minLat, double maxLng, double maxLat) {
		URL url;
		HttpURLConnection connection;
		String bbox =minLng + "," + minLat + "," + maxLng + ","
				+ maxLat;
		try {
			url = new URL("https://www.openstreetmap.org/api/0.6/map?bbox=" + bbox);
			System.out.println(url.toString());
			connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			boolean flag = true;
			if (connection.getResponseCode() == 400) {
				System.err.println("WARNING: Map boundary box split to 4 parts.");
				double dlat = (maxLat - minLat)/2.;
				double dlng = (maxLng - minLng)/2.;
				
				for(int i = 0; i < 2; ++ i) {
					for(int j = 0; j < 2; ++ j) {
						double mlat = minLat + i*dlat;
						double mlng = minLng + j*dlng;
						flag &= getBoundary(mlng, mlat, mlng+dlng, mlat+dlat);
					}
				}
			} else if(connection.getResponseCode() == 200) {
				BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
				String filename = PATHNAME + "/" + bbox + ".osm";
				FileOutputStream fileOutputStream = new FileOutputStream(filename);
				byte dataBuffer[] = new byte[1024];
				int bytesRead;
				while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
					fileOutputStream.write(dataBuffer, 0, bytesRead);
				}
				System.out.println(filename + "download finished");
				fileOutputStream.close();
				in.close();
			} else {
				System.err.println("ERROR: Connection error.");
			}
			TimeUnit.SECONDS.sleep(1);
			return flag;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}


	public static void setFilepath(String path) {
		PATHNAME = path;
	}
}
