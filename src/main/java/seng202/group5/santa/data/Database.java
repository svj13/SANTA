package seng202.group5.santa.data;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Database wrapper class. this class is used to control our sql database
 * @author Group5
 */
public class Database {

	private ArrayList<CrimeRecord> queryCrimeList = new ArrayList<CrimeRecord>();

	/**
	 * Creates a table with the name crime table in the Crime sqlite database for storing crime records
	 */
	public void makeTable() {
		Connection c = null;
		Statement stmt = null;

		try {
			//connects to the database
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:Crime.db");
			// check if a table with that name has already been created in the database
			DatabaseMetaData md = c.getMetaData();
			ResultSet rs = md.getTables(null, null, "%", null);
			while (rs.next()) {
				if (rs.getString(3).equals("CRIMETABLE")) {
					c.close();
					return;
				}
			}

			stmt = c.createStatement();
			//sql command to create a new table for storing crime records
			String sql = "CREATE TABLE CRIMETABLE"
					+ " (LIST_ID INT NOT NULL," + "CRIME_ID TEXT  NOT NULL, " + " DATE                 INT, "
					+ " BLOCK                	TEXT, " + " IUCR                    TEXT, "
					+ " PRIMARY_DESCRIPTION      TEXT, " + " SECONDARY_DESCRIPTION    TEXT, "
					+ " LOCATION                TEXT, " + " ARREST                  TEXT, "
					+ " DOMESTIC                TEXT, " + " BEAT                    INT, "
					+ " WARD                    INT,  " + " FBI_CD                  TEXT, "
					+ " XCOOR                   INT,  " + " YCOOR                   INT, "
					+ " LATITUDE                REAL, " + " LONGITUDE               REAL" +
					", UNIQUE(LIST_ID,  CRIME_ID) ON CONFLICT REPLACE)";

			stmt.executeUpdate(sql);
			//added an index to speed up getting data from the database
			stmt.executeUpdate("CREATE INDEX dataSet ON CRIMETABLE (CRIME_ID);");
			//close sql statement and connection to the database
			stmt.close();
			c.close();
		} catch (Exception e) {
			// catch the exception throwen by the database and prints out what failed
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	/**
	 * Inserts the crime records from the given list into a the crimetable if the table already has a record with the same list id and crime id the record is updated
	 * @param crimeList  an arraylist holding the crime records that are to be added to the crime table
	 * @param UserListId the user list id that the data is being add to.
	 */
	public void addCrimeList(ArrayList<CrimeRecord> crimeList, int UserListId) {
		Connection c = null;
		Statement stmt = null;
		try {
			// connect to the database
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:Crime.db");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			// iterates through the list of crime to add to the database adding them one by one
			StringBuilder sql = new StringBuilder("INSERT OR REPLACE INTO CRIMETABLE  (LIST_ID,  CRIME_ID, DATE, BLOCK, IUCR, PRIMARY_DESCRIPTION, SECONDARY_DESCRIPTION, LOCATION, ARREST,"
					+ " DOMESTIC, BEAT, WARD, FBI_CD, XCOOR, YCOOR, LATITUDE, LONGITUDE) VALUES");
			for (int i = 0; i < crimeList.size(); i++) {
				// turns the crime date field from a number of seconds so it can be store and queried in the database
				if (i != 0) {
					sql.append(",");
				}
				CrimeRecord tempRecord = crimeList.get(i);
				long intTime = tempRecord.getBetterCrimeDate().getTimeInMillis() / 1000;
				// make the value part of the insert record command
				String values = " " + UserListId + ",'" + tempRecord.getCaseNumber() + "','" + intTime + "', '"
						+ tempRecord.getBlock() + "', '" + tempRecord.getIucr() + "', '"
						+ tempRecord.getPrimaryDescription() + "','" + tempRecord.getSecondaryDescription() + "', '"
						+ tempRecord.getLocationDescription() + "', '" + tempRecord.getArrest() + "', '"
						+ tempRecord.getDomestic() + "', " + tempRecord.getBeat() + ", " + tempRecord.getWard() + ", '"
						+ tempRecord.getFbi_cd().toString() + "', " + tempRecord.getXCoordinate().toString() + ", "
						+ tempRecord.getYCoordinate() + ", " + tempRecord.getLatitude().toString() + ", "
						+ tempRecord.getLongitude().toString();
				sql.append("(");
				sql.append(values);
				sql.append(")");
			}
			stmt.executeUpdate(sql.toString());
			// closes database connection
			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			// catch the exception throwen by the database and prints out what failed
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	/**
	 * Sends a request to the database and creates a list of crime records that meet that query
	 * @param query the query uses to get the data from the database to create crime records on.
	 * @return returns an arraylist of crime records that meet the query
	 */
	public ArrayList<CrimeRecord> queryDatabase(String query) {
		Connection c = null;
		Statement stmt = null;
		//ArrayList<CrimeRecord> queryCrimeList = new ArrayList<CrimeRecord>();
		try {
			// connected to the database
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:Crime.db");
			c.setAutoCommit(false);

			stmt = c.createStatement();
			// make a result set from the query to the database
			ResultSet rs = stmt.executeQuery(query);
			queryCrimeList.clear();
			while (rs.next()) {
				Calendar calendar = new GregorianCalendar();
				// turns the date from an integer (number of seconds) back into a calender type.
				Date tempdate = new Date();
				long tempTime = rs.getInt("date");
				tempdate.setTime(tempTime * 1000);
				calendar.setTime(tempdate);
				// creates a new crime record with the data from the currently selected result
				queryCrimeList.add(new CrimeRecord(rs.getString("crime_id"), calendar, rs.getString("block"),
						rs.getString("iucr"), rs.getString("primary_description"), rs.getString("secondary_description"), rs.getString("location"),
						rs.getString("ARREST"), rs.getString("domestic"), rs.getInt("beat"), rs.getInt("ward"),
						rs.getString("fbi_cd"), rs.getInt("xcoor"), rs.getInt("ycoor"), rs.getFloat("latitude"),
						rs.getFloat("longitude")));
			}
			// close database
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			// catch the exception throwen by the database and prints out what failed
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return queryCrimeList;
	}

	/**
	 * Creates a sqlite query for filtering multiple fields from the given parameters
	 * @return the created sql query
	 * @param userListId	the number of the user list you want to select data from.
	 * @param filterList	the list containing string of what to filter the database by
	 */
	public String getFilterQuery(int userListId, ArrayList<String> filterList) {
		ArrayList<CrimeRecord> CrimeList;
		ArrayList<String[]> filtersText = new ArrayList<String[]>();
		boolean found;
		String[] filter;
		// if the filter list is empty return sql query that will get all the records from a selected user list
		if (filterList.size() == 0) {
			return "SELECT * FROM CRIMETABLE WHERE (list_id = " + userListId + ");";
			// if the filter list is not empty make an advanced query
		} else {
			for (int i = 0; i < filterList.size(); i++) {
				found = false;
				//splits the filter string into three different parts (table field, operator, competition value)
				filter = filterList.get(i).split(",");
				// checks so see if the current table field to be searched has already been added to the filter text list
				for (int z = 0; z < filtersText.size(); z++) {
					if (filter[0].equals(filtersText.get(z)[0])) {
						found = true;
						// check which operation the current filter parameter is wanting to carry out
						if (filter[1].equals("=") || filter[1].equals("<") || filter[1].equals(">")) {
							// takes the current filter text for that field and add the new filter parameters for that field in an or format
							filtersText.get(z)[1] = filtersText.get(z)[1] + " OR " + filter[0] + " " + filter[1] + " '" + filter[2] + "'";
							break;
						}
						// takes the current filter text for that field and add the new filter parameters for that field in an between format
						if (filter[1].equals("BETWEEN")) {
							filtersText.get(z)[1] += " OR " + filter[0] + " BETWEEN " + filter[2] + " AND " + filter[3] + "";
							break;
						}
					}
				}
				// if the current supp filter table field parameter in not all ready in the list add a new string with the new supp filter parameter
				if (!found) {
					// set up a new field filter String list with the first string being the table search field and the second being the current sup filter parameter in the or format
					if (filter[1].equals("=") || filter[1].equals("<") || filter[1].equals(">")) {
						String[] temp = new String[2];
						temp[0] = filter[0];
						temp[1] = filter[0] + " " + filter[1] + " '" + filter[2] + "'";
						filtersText.add(temp);
					}
					// set up a new field filter String list with the first string being the table search field and the second being the current sup filter parameter in the between format
					if (filter[1].equals("BETWEEN")) {
						String[] temp = new String[2];
						temp[0] = filter[0];
						temp[1] = filter[0] + " " + filter[1] + " " + filter[2] + " AND " + filter[3] + " ";
						filtersText.add(temp);
					}
				}
			}
		}
		// iterates through the filter text array adding all the filter together to make the query part of the sql statement
		String query = "( " + filtersText.get(0)[1] + ") ";
		for (int x = 1; x < filtersText.size(); x++) {
			query = query + " AND ( " + filtersText.get(x)[1] + " )";
		}
		// add together all the different parts of the sql statement and returns them
		return "SELECT * FROM CRIMETABLE WHERE ((list_id = " + userListId + ") AND " + query + ");";
	}

	/**
	 * Queries the database using the query created by the get filter query to get the crime records that meet the given filtering parameter
	 * @return the crime record that meet the parameter of the sql statement
	 * @param userListId	the number of the user list you want to select data from.
	 * @param filterList	the list containing string of what to filter the database by
	 */
	public ArrayList<CrimeRecord> filterCrimes(int userListId, ArrayList<String> filterList) {
		return queryDatabase(getFilterQuery(userListId, filterList));
	}

	/**
	 * Queries the database to find all the distinct elements of a column in the crime table
	 * @return a list of all the distinct elements of the column
	 * @param userListId	the number of the user list you want to select data from
	 * @param column	the name of the column you want to get the distinct values from in the crime table
	 */
	public ArrayList<String> getDistnct(int userListId, String column) {
		Connection c = null;
		Statement stmt = null;
		ArrayList<String> distnctList = new ArrayList<String>();
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:Crime.db");
			stmt = c.createStatement();
			// queries the database to find all the distinct elements of a column in the crime table
			ResultSet rs = stmt.executeQuery("SELECT DISTINCT " + column + " FROM CRIMETABLE WHERE (list_id = " + userListId + ") ORDER BY " + column +" ASC;");
			while (rs.next()) {
				distnctList.add(rs.getString(column));
			}
			// closes the database
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return distnctList;
	}

	/**
	 * Deletes a record with a given crime id from a given user list
	 * @param userListId	the name of the table you want add records to.
	 * @param crimeId	 the id of the crime record you want to delete
	 */
	public void deleteRecord(int userListId, String crimeId) {
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:Crime.db");
			stmt = c.createStatement();
			// deletes the record
			if (crimeId.equals("ALL")) {
				stmt.executeUpdate("DELETE FROM CRIMETABLE WHERE (LIST_ID = '" + userListId + "');");
			}
			else {
				stmt.executeUpdate("DELETE FROM CRIMETABLE WHERE (LIST_ID = '" + userListId + "' AND CRIME_ID = '" + crimeId + "');");
			}
			c.close();
		} catch (Exception e) {
			// used to catch exception were record with that crime id is not found
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	/**
	 * Returns count of a given variable with allowance for other input.
	 * This allows for returning values as a tuple with name and count.
	 * @param key String
	 * @param tableName String
	 * @param argument String
	 * @param countQuery String
	 * @return Returns an Arraylist of CrimeTuple objects.
	 */
	public ArrayList<CrimeTuple> getTupleCount(String key, String tableName, String argument, String countQuery)
	{
		Connection c = null;
		Statement stmt = null;
		ArrayList<CrimeTuple> r_list = new ArrayList<CrimeTuple>();
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:Crime.db");
			c.setAutoCommit(false);

			stmt = c.createStatement();
			String q = "SELECT " + key +", COUNT(*)" + " FROM " + tableName +
					argument + countQuery + ";";
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next())
			{
				r_list.add(new CrimeTuple(rs.getString(1), rs.getInt(2), 0, 0));
			}
			rs.close();
			stmt.close();
			c.close();
		}
		catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return r_list;
	}

	/**
	 * Returns the count of a given variable in the data set
	 * @param loc Location
	 * @param tableName Table Name
	 * @param argument Argument
	 * @return int count
	 */
	public int getCount(String loc, String tableName, String argument, String countQuery){
		Connection c = null;
		Statement stmt = null;
		int count = 0;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:Crime.db");
			c.setAutoCommit(false);

			stmt = c.createStatement();
			String q = "SELECT COUNT(" + loc + ")" + " FROM " + tableName +
					argument + countQuery + ";";
			ResultSet rs = stmt.executeQuery(q);
			count = rs.getInt(1);
			rs.close();
			stmt.close();
			c.close();
		}
		catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return count;
	}

	/**
	 * Returns an arraylist of all the dates in the data set.
	 * @param maxMin
	 * @param table_name
	 * @param daysQuery
	 * @return Date
	 */
	public Date getDays(String maxMin, String table_name, String daysQuery)
	{
		Date r_val = null;
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:Crime.db");
			c.setAutoCommit(false);

			stmt = c.createStatement();
			String q = "SELECT " + maxMin + "(date) FROM " + table_name + daysQuery +";";
			ResultSet rs = stmt.executeQuery(q);
			long tTime = rs.getInt(1);
			Date tDate = new Date();
			tDate.setTime(tTime * 1000);
			r_val = tDate;
			rs.close();
			stmt.close();
			c.close();
		}
		catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return r_val;
	}
}