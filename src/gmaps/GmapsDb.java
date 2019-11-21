package gmaps;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @ClassName: GmapsDBC
 * @Description: {@link #COLUMNS}
 * @author: amomorning
 * @date: Nov 20, 2019 9:37:24 PM
 */
public class GmapsDb {


	/**
	 * column of database name in 2-d array
	 * {@value #COLUMNS}
	 * @name COLUMNS[i][0]
	 * @type COLUMNS[i][1]
	 */
	public static String[][] COLUMNS = { 
			{ "placeid", "char(255)" }, 
			{ "lat", "float" }, 
			{ "lng", "float" },
			{ "name", "char(255)" }, 
			{ "type", "char(255)" } };

	/**
	 * 0 to n should be set as 'not null'
	 */
	public static int NOT_NULL_LENGTH = 3;
	public static String TABLENAME = "function";

	private Connection connection = null;
	private Statement stmt = null;
	private PreparedStatement pstmt = null;

	/**
	 * @Function:GmapsDb
	 * @Description:
	 */
	public GmapsDb() {
		connection = getConnection();
	}

	/**
	 * @Function: getConnection
	 * @Description: get connection
	 *
	 * @return: Connection
	 */
	public Connection getConnection() {
		Connection c = null;
		try {
			Class.forName(Info.DRIVE);
			c = DriverManager.getConnection(Info.URL, Info.USERNAME, Info.PASSWORD);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return c;
	}

	/**
	 * @Function: createTable
	 * @Description: Create table using {@link #COLUMNS} and {@link #TABLENAME}
	 *
	 * @return: void
	 */
	public void createTable() {
		try {
			stmt = connection.createStatement();
			String sql = "create table " + TABLENAME + " (";
			for (int i = 0; i < COLUMNS.length; ++i) {
				sql += COLUMNS[i][0] + " " + COLUMNS[i][1];
				if (i == 0) {
					sql += "primary key";
				}
				if (i < NOT_NULL_LENGTH) {
					sql += " not null";
				}
				if (i != COLUMNS.length - 1) {
					sql += ", ";
				} else {
					sql += ")";
				}
			}
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	/**
	 * @Function: deleteTable
	 * @Description: delete table {@link #TABLENAME}
	 *
	 * @return: void
	 */
	public void deleteTable() {
		try {
			stmt = connection.createStatement();
			String sql = "drop table " + TABLENAME;
			stmt.executeUpdate(sql);
			stmt.close();
			System.out.println("delete table successfully");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	/**
	 * @Function: insertData
	 * @Description: insert a row of data into database
	 * @param values
	 *
	 * @return: void
	 */
	public void insertData(String values) {
        try {
            stmt = connection.createStatement();
            String sql = "insert into " + TABLENAME + " (";
            for(int i = 0; i < COLUMNS.length; ++ i) {
                sql += COLUMNS[i][0];
                if(i == COLUMNS.length - 1) sql += ") ";
                else sql += ", ";
            } 
            sql += "values (" + values + ") on conflict (" + COLUMNS[0][0] + ") do nothing;";

            System.out.println(sql); 
            stmt.executeUpdate(sql);
            stmt.close();
            System.out.println("records created successfully");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }	
	}
	
}
