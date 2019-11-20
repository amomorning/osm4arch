package gmaps;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlacesSearchResponse;

import utils.Container;

public class test {
            
    
    public static String COLUMNS[][] = {
            {"placeid", "char(255)"}, 
            {"lat", "float"},
            {"lng", "float"},
            {"name", "char(255)"},
            {"type", "char(255)"}
            };
    public static int NOT_NULL_LENGTH = 3;
    public static String TABLENAME = "function";

    public static void main(String[] args) throws Exception {
        test testCase = new test();

        Connection c = testCase.connectSQL();
//        testCase.createTable(c);
//        testCase.deleteTable(c);

        testCase.testGoogleAPI(c);
    }

    public Connection connectSQL() {

        try {
            Connection c = null;
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/prato", "postgres", "1218");
            System.out.println("Opened database successfully");
            return c;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
            return null;
        }
    }

    public void createTable(Connection c) {
        Statement stmt = null;
        try {

            stmt = c.createStatement();
            String sql = "create table " + TABLENAME + " (";
            for(int i = 0; i < COLUMNS.length; ++ i) {
                sql += COLUMNS[i][0] + " "+ COLUMNS[i][1];
                if(i == 0) sql += "primary key";
                if(i < NOT_NULL_LENGTH) sql += " not null";
                if(i != COLUMNS.length-1) sql += ", ";
                else sql += ")";
            }
            stmt.executeUpdate(sql);
            stmt.close();
            System.out.println("create table successfully");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }
    
    public void deleteTable(Connection c) {
        
        Statement stmt = null;
        try {

            stmt = c.createStatement();
            String sql = "DROP TABLE " + TABLENAME;
            stmt.executeUpdate(sql);
            stmt.close();
            System.out.println("delete table successfully");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
    
    public void insertData(Connection c, String values) {
        Statement stmt = null;
        try {

            stmt = c.createStatement();
            String sql = "insert into " + TABLENAME + " (";
            for(int i = 0; i < COLUMNS.length; ++ i) {
                sql += COLUMNS[i][0];
                if(i == COLUMNS.length - 1) sql += ");";
                else sql += ", ";
            }
            sql += "values (" + values + ");";
             
            stmt.executeUpdate(sql);
            stmt.close();
            System.out.println("records created successfully");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public void testGoogleAPI(Connection c) throws Exception {

        GeoApiContext context = new GeoApiContext.Builder().apiKey("AIzaSyB5hXZRD8xQP4NCHODHCX9torCcH1ClMk0").build();
        LatLng PRATO = new LatLng(Container.MAP_LAT_LNG[0], Container.MAP_LAT_LNG[1]);

        PlacesSearchResponse response = PlacesApi.nearbySearchQuery(context, PRATO).radius(50).await();
        String value = "";
        LatLng position = response.results[0].geometry.location;
        value += position.lat;
        String[] type = response.results[0].types;
        
    }

}
