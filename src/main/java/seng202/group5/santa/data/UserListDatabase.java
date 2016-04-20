package seng202.group5.santa.data;

import java.sql.*;
import java.util.ArrayList;

/**
 * Stores custom user-created lists
 * @author group5
 */
public class UserListDatabase {
    /**
     * Creates a table with the name user list table in the Crime sqlite database for storing user list text adn id's
     */
    public void makeTable() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:Crime.db");
            // check if a table with that name has already been created in the database
            DatabaseMetaData md = c.getMetaData();
            ResultSet rs = md.getTables(null, null, "%", null);
            while (rs.next()) {
                if (rs.getString(3).equals("USERLISTTABLE")) {
                    c.close();
                    return;
                }
            }
            stmt = c.createStatement();
            //creates a new table call user list table for storeing crimetext
            String sql = "CREATE TABLE  USERLISTTABLE (LIST_ID INTEGER PRIMARY KEY AUTOINCREMENT, DISPLAYTEXT TEXT)";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    /**
     * queries the database to find all the distinct elements of a column in the user list table
     * @return a list of all the distinct elements of the column
     * @param column	the name of the column you want to get the distinct values from
     */
    public ArrayList<String> getDistinct(String column) {
        Connection c = null;
        Statement stmt = null;
        ArrayList<String> distinctList = new ArrayList<String>();
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:Crime.db");
            stmt = c.createStatement();
            // gets all the distinct values from a column and adds them to a list
            ResultSet rs = stmt.executeQuery("SELECT DISTINCT " + column + " FROM USERLISTTABLE ORDER BY " + column + " ASC;");
            while (rs.next()) {
                distinctList.add(rs.getString(column));
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return distinctList;
    }

    /**
     * checks to see if a record with that name is all ready in the database and then add it if it is not
     * @param crimeListText	the name new user list you want to add to the database
     */
    public void addCrimeListId(String crimeListText) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:Crime.db");
            c.setAutoCommit(false);
            stmt = c.createStatement();
            stmt.executeUpdate("INSERT INTO  USERLISTTABLE (DISPLAYTEXT) " + "VALUES ( '" + crimeListText + "');");
            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    /**
     * finds the list id for a given user list from the user list database
     * @return the id of the given user list
     * @param listName	the name of the user list you are tring to find the id for
     * */
    public Integer getListNumber(String listName) {
        Connection c = null;
        Statement stmt = null;
        Integer listNumber = new Integer(0);
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:Crime.db");
            c.setAutoCommit(false);

            stmt = c.createStatement();
            //finds the list id for a given user list from the user list database
            ResultSet rs = stmt.executeQuery("SELECT LIST_ID FROM USERLISTTABLE WHERE (DISPLAYTEXT = '" + listName + "');");
            while (rs.next()) {
                listNumber = rs.getInt("LIST_ID");
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return listNumber;
    }

    /**
     * deletes a record with a given user list id from the user list table
     * @param userListId	the name of the table you want add records to.
     */
    public void deleteUserList(Integer userListId) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:Crime.db");
            stmt = c.createStatement();
            // deletes the record
            if (getDistinct("DISPLAYTEXT").size() == 1) {
                addCrimeListId("Default Data List");
            }
            stmt.executeUpdate("DELETE FROM USERLISTTABLE WHERE (LIST_ID = " + userListId + ");");
            c.close();
        } catch (Exception e) {
            // used to catch exception were record with that crime id is not found
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}
