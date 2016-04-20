
package seng202.group5.santa.data;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * JUnit tests for the database
 * @author Group5
 */
public class DatabaseTest extends TestCase {

    private ArrayList<CrimeRecord> crimeList;
    private ArrayList<CrimeRecord> testList;
    private Database database;

    @Before
    public void setUp() throws Exception {
        String i =  System.getProperty("user.dir");
        new File(i + File.separator + "Crime.db").delete();
        testList = new ArrayList<CrimeRecord>();
        database = new Database();
    }

    @After
    public void tearDown() throws Exception {
        String i =  System.getProperty("user.dir");
        new File(i + File.separator + "Crime.db").delete();
    }

    @Test
    public void testMakeTable() throws Exception {
        database.makeTable();
        testList = database.queryDatabase("SELECT * FROM CRIMETABLE");
        assertEquals(0, testList.size());
    }



    @Test
    public void testRecordData () throws  Exception{
        // test the the crime record that goes into the database is the same as the one that come out
        ArrayList<CrimeRecord> testList = new ArrayList<CrimeRecord>();
        Calendar calendar = new GregorianCalendar();
        Date tempdate = new Date();
        long tempTime = 140385426;
        tempdate.setTime(tempTime * 1000);
        calendar.setTime(tempdate);
        CrimeRecord testRecord = new CrimeRecord("HX321538",calendar,"080XX S HALSTED ST","860","THEFT", "RETAIL THEFT","DEPARTMENT STORE","true","false",621,21,"06",1172409,1851438,0.1f,0.1f);
        testList.add(testRecord);
        database.makeTable();
        database.addCrimeList(testList, 1);
        crimeList = database.queryDatabase("SELECT * FROM CRIMETABLE");
        assertEquals(1, crimeList.size());
        assertEquals(testRecord.toString(), crimeList.get(0).toString());
    }

    @Test
    public void testAddTwiceToSameList () throws  Exception{
        // test to see that the same crime reocrd cant be add twice to the same user list
        ArrayList<CrimeRecord> testList = new ArrayList<CrimeRecord>();
        Calendar calendar = new GregorianCalendar();
        Date tempdate = new Date();
        long tempTime = 140385426;
        tempdate.setTime(tempTime * 1000);
        calendar.setTime(tempdate);

        CrimeRecord testRecord = new CrimeRecord("HX321538",calendar,"080XX S HALSTED ST","860","THEFT", "RETAIL THEFT","DEPARTMENT STORE","true","false",621,21,"06",1172409,1851438,0.1f,0.1f);
        testList.add(testRecord);
        database.makeTable();
        database.addCrimeList(testList, 1);
        database.addCrimeList(testList, 1);
        crimeList = database.queryDatabase("SELECT * FROM CRIMETABLE");
        assertEquals(1, crimeList.size());
        assertEquals(testRecord.toString(), crimeList.get(0).toString());
    }

    @Test
    public void testToDifferentLists () throws  Exception{
        // test to see that the same crime reocrd can be add do differnet user lists
        ArrayList<CrimeRecord> testList = new ArrayList<CrimeRecord>();
        Calendar calendar = new GregorianCalendar();
        Date tempdate = new Date();
        long tempTime = 140385426;
        tempdate.setTime(tempTime * 1000);
        calendar.setTime(tempdate);

        CrimeRecord testRecord = new CrimeRecord("HX321538",calendar,"080XX S HALSTED ST","860","THEFT", "RETAIL THEFT","DEPARTMENT STORE","true","false",621,21,"06",1172409,1851438,0.1f,0.1f);
        testList.add(testRecord);
        database.makeTable();
        database.addCrimeList(testList, 1);
        database.addCrimeList(testList, 2);
        crimeList = database.queryDatabase("SELECT * FROM CRIMETABLE");
        assertEquals(2, crimeList.size());
        assertEquals(testRecord.toString(), crimeList.get(0).toString());
    }

    @Test
    public void testUpDateOnDupIds () throws  Exception{
        // test to see that then a crime record with the same id as an record already in the table is added to the same user list the reocord is updated
        ArrayList<CrimeRecord> testList = new ArrayList<CrimeRecord>();
        ArrayList<CrimeRecord> upDateList = new ArrayList<CrimeRecord>();
        Calendar calendar = new GregorianCalendar();
        Date tempdate = new Date();
        long tempTime = 140385426;
        tempdate.setTime(tempTime * 1000);
        calendar.setTime(tempdate);

        CrimeRecord testRecord = new CrimeRecord("HX321538",calendar,"080XX S HALSTED ST","860","THEFT", "RETAIL THEFT","DEPARTMENT STORE","true","false",621,21,"06",1172409,1851438,0.1f,0.1f);
        CrimeRecord updateRecord = new CrimeRecord("HX321538",calendar,"NewRalue st","86","THEFT", "RETAIL THEFT","DEPARTMENT STORE","true","false",621,21,"06",1172409,1851438,0.1f,0.1f);
        testList.add(testRecord);
        upDateList.add(updateRecord);
        database.makeTable();
        database.addCrimeList(testList, 1);
        database.addCrimeList(upDateList, 1);
        crimeList = database.queryDatabase("SELECT * FROM CRIMETABLE");
        assertEquals(1, crimeList.size());
        assertEquals(updateRecord.toString(), crimeList.get(0).toString());
    }

    @Test
    public void testDeleteRecord () throws  Exception{
        // test that the delete record method deletes a given record
        ArrayList<CrimeRecord> testList = new ArrayList<CrimeRecord>();
        Calendar calendar = new GregorianCalendar();
        Date tempdate = new Date();
        long tempTime = 140385426;
        tempdate.setTime(tempTime * 1000);
        calendar.setTime(tempdate);

        CrimeRecord testRecord = new CrimeRecord("HX321538",calendar,"080XX S HALSTED ST","860","THEFT", "RETAIL THEFT","DEPARTMENT STORE","true","false",621,21,"06",1172409,1851438,0.1f,0.1f);
        testList.add(testRecord);
        database.makeTable();
        database.addCrimeList(testList, 1);
        database.deleteRecord(1, "HX321538");
        crimeList = database.queryDatabase("SELECT * FROM CRIMETABLE");
        assertEquals(0, crimeList.size());
    }

    @Test
    public void testDeleteRecordFromDifList () throws  Exception{
        // test the the delete record method only deletes a record from the given user list
        ArrayList<CrimeRecord> testList = new ArrayList<CrimeRecord>();
        Calendar calendar = new GregorianCalendar();
        Date tempdate = new Date();
        long tempTime = 140385426;
        tempdate.setTime(tempTime * 1000);
        calendar.setTime(tempdate);

        CrimeRecord testRecord = new CrimeRecord("HX321538",calendar,"080XX S HALSTED ST","860","THEFT", "RETAIL THEFT","DEPARTMENT STORE","true","false",621,21,"06",1172409,1851438,0.1f,0.1f);
        testList.add(testRecord);
        database.makeTable();
        database.addCrimeList(testList, 1);
        database.addCrimeList(testList, 2);
        database.deleteRecord(1, "HX321538");
        crimeList = database.queryDatabase("SELECT * FROM CRIMETABLE");
        assertEquals(1, crimeList.size());
        assertEquals(testRecord.toString(), crimeList.get(0).toString());
    }

    @Test
    public void testZoreDateTest () throws  Exception{
        // test the the delete record method only deletes a record from the given user list
        ArrayList<CrimeRecord> testList = new ArrayList<CrimeRecord>();
        Calendar calendar = new GregorianCalendar();
        Date tempdate = new Date();
        long tempTime = 0;
        tempdate.setTime(tempTime * 1000);
        calendar.setTime(tempdate);

        CrimeRecord testRecord = new CrimeRecord("HX321538",calendar,"080XX S HALSTED ST","860","THEFT", "RETAIL THEFT","DEPARTMENT STORE","true","false",621,21,"06",1172409,1851438,0.1f,0.1f);
        testList.add(testRecord);
        database.makeTable();
        database.addCrimeList(testList, 1);
        crimeList = database.queryDatabase("SELECT * FROM CRIMETABLE");
        assertEquals(1, crimeList.size());
        assertEquals(testRecord.toString(), crimeList.get(0).toString());
    }

    @Test
    public void testNegitiveDate () throws  Exception{
        // test the the delete record method only deletes a record from the given user list
        ArrayList<CrimeRecord> testList = new ArrayList<CrimeRecord>();
        Calendar calendar = new GregorianCalendar();
        Date tempdate = new Date();
        long tempTime = -10000000;
        tempdate.setTime(tempTime * 1000);
        calendar.setTime(tempdate);

        CrimeRecord testRecord = new CrimeRecord("HX321538",calendar,"080XX S HALSTED ST","860","THEFT", "RETAIL THEFT","DEPARTMENT STORE","true","false",621,21,"06",1172409,1851438,0.1f,0.1f);
        testList.add(testRecord);
        database.makeTable();
        database.addCrimeList(testList, 1);
        crimeList = database.queryDatabase("SELECT * FROM CRIMETABLE");
        assertEquals(1, crimeList.size());
        assertEquals(testRecord.toString(), crimeList.get(0).toString());
    }

    @Test
    public void test5000record () throws  Exception{
        URL resourceUrl = getClass().getResource("/seng202_2015_crimes_one_year_prior_to_present_5k.csv");
        Parser aParser = new Parser();
        ArrayList<CrimeRecord> testList = aParser.Parsefile(resourceUrl.getPath());
        database.makeTable();
        database.addCrimeList(testList, 1);
        crimeList = database.queryDatabase("SELECT * FROM CRIMETABLE");
        assertEquals(5000, crimeList.size());
    }

    @Test
    public void test10000record () throws  Exception{
        URL resourceUrl = getClass().getResource("/seng202_2015_crimes_one_year_prior_to_present_10k.csv");
        Parser aParser = new Parser();
        ArrayList<CrimeRecord> testList = aParser.Parsefile(resourceUrl.getPath());
        database.makeTable();
        database.addCrimeList(testList, 1);
        ArrayList<String> inputList = new ArrayList<String>();
        crimeList = database.queryDatabase("SELECT * FROM CRIMETABLE");
        assertEquals(9999, crimeList.size()); // record 5567 have the same id as 7221 and so is one less than 10000
    }

    @Test
    public void testgetFilterBtween () throws  Exception{
        ArrayList < String > filterList = new ArrayList<String>();
        database.makeTable();
        filterList.add("DATE,BETWEEN,0,14000");
        String filterQuery = database.getFilterQuery(1, filterList);
        assertEquals("SELECT * FROM CRIMETABLE WHERE ((list_id = 1) AND ( DATE BETWEEN 0 AND 14000 ) );", filterQuery);
    }

    @Test
    public void testgetFilterBtweenTwice () throws  Exception{
        ArrayList < String > filterList = new ArrayList<String>();
        database.makeTable();
        filterList.add("DATE,BETWEEN,0,1400");
        filterList.add("DATE,BETWEEN,10000,14000");
        String filterQuery = database.getFilterQuery(1, filterList);
        assertEquals("SELECT * FROM CRIMETABLE WHERE ((list_id = 1) AND ( DATE BETWEEN 0 AND 1400  OR DATE BETWEEN 10000 AND 14000) );", filterQuery);
    }


    @Test
    public void testgetFilterQuery () throws  Exception{
        ArrayList < String > filterList = new ArrayList<String>();
        database.makeTable();
        filterList.add("PRIMARY_DESCRIPTION,=,BATTERY");
        filterList.add("PRIMARY_DESCRIPTION,=,ASSAULT");
        filterList.add("DATE,BETWEEN,0,14000000000");
        String filterQuery = database.getFilterQuery(1, filterList);
        assertEquals("SELECT * FROM CRIMETABLE WHERE ((list_id = 1) AND ( PRIMARY_DESCRIPTION = 'BATTERY' OR PRIMARY_DESCRIPTION = 'ASSAULT')  AND ( DATE BETWEEN 0 AND 14000000000  ));", filterQuery);
    }

    @Test
    public void testFilterQuery () throws  Exception{
        // test how the get filter method deals with no filter
        database.makeTable();
        ArrayList < String > filterList = new ArrayList<String>();
        String filterQuery = database.getFilterQuery(1, filterList);
        assertEquals("SELECT * FROM CRIMETABLE WHERE (list_id = 1);", filterQuery);
    }

    @Test
    public void testFilterQueryOneFilter () throws  Exception{
        // test how the get filter method deals with searching on two of the same field
        database.makeTable();
        ArrayList < String > filterList = new ArrayList<String>();
        filterList.add("PRIMARY_DESCRIPTION,=,BATTERY");
        String filterQuery = database.getFilterQuery(1, filterList);
        assertEquals("SELECT * FROM CRIMETABLE WHERE ((list_id = 1) AND ( PRIMARY_DESCRIPTION = 'BATTERY') );", filterQuery);
    }

    @Test
    public void testFilterQuery1 () throws  Exception{
        // test how the get filter method deals with searching on two of the same field
        database.makeTable();
        ArrayList < String > filterList = new ArrayList<String>();
        filterList.add("PRIMARY_DESCRIPTION,=,BATTERY");
        filterList.add("PRIMARY_DESCRIPTION,=,ASSAULT");
        String filterQuery = database.getFilterQuery(1, filterList);
        assertEquals("SELECT * FROM CRIMETABLE WHERE ((list_id = 1) AND ( PRIMARY_DESCRIPTION = 'BATTERY' OR PRIMARY_DESCRIPTION = 'ASSAULT') );", filterQuery);
    }

    @Test
    public void testFilterQuery2 () throws  Exception{
        // test how the get filter method deals with searching on two different fields
        database.makeTable();
        ArrayList < String > filterList = new ArrayList<String>();
        filterList.add("PRIMARY_DESCRIPTION,=,BATTERY");
        filterList.add("SECONDARY_DESCRIPTION,=,AGGRAVATED: HANDGUN");
        String filterQuery = database.getFilterQuery(1, filterList);
        assertEquals("SELECT * FROM CRIMETABLE WHERE ((list_id = 1) AND ( PRIMARY_DESCRIPTION = 'BATTERY')  AND ( SECONDARY_DESCRIPTION = 'AGGRAVATED: HANDGUN' ));", filterQuery);
    }

    @Test
    public void testFilterQueryLessThan () throws  Exception{
        // test how the get filter method deals with searching on two different fields
        database.makeTable();
        ArrayList < String > filterList = new ArrayList<String>();
        filterList.add("PRIMARY_DESCRIPTION,<,BATTERY");
        String filterQuery = database.getFilterQuery(1, filterList);
        assertEquals("SELECT * FROM CRIMETABLE WHERE ((list_id = 1) AND ( PRIMARY_DESCRIPTION < 'BATTERY') );", filterQuery);
    }

    @Test
    public void testFilterQueryGreaterThan () throws  Exception{
        // test how the get filter method deals with searching on two different fields
        database.makeTable();
        ArrayList < String > filterList = new ArrayList<String>();
        filterList.add("PRIMARY_DESCRIPTION,>,BATTERY");
        String filterQuery = database.getFilterQuery(1, filterList);
        assertEquals("SELECT * FROM CRIMETABLE WHERE ((list_id = 1) AND ( PRIMARY_DESCRIPTION > 'BATTERY') );", filterQuery);
    }


    @Test
    public void testfilterCrimes () throws  Exception{
        URL resourceUrl = getClass().getResource("/seng202_2015_crimes_one_year_prior_to_present_5k.csv");
        Parser aParser = new Parser();
        ArrayList<CrimeRecord> testList = aParser.Parsefile(resourceUrl.getPath());
        database.makeTable();
        database.addCrimeList(testList, 1);
        ArrayList < String > filterList = new ArrayList<String>();
        ArrayList<CrimeRecord> filterOutput = database.filterCrimes(1, filterList);
        assertEquals(5000, filterOutput.size());
    }

    @Test
    public void testUserList () throws  Exception{
        URL resourceUrl = getClass().getResource("/seng202_2015_crimes_one_year_prior_to_present_5k.csv");
        Parser aParser = new Parser();
        ArrayList<CrimeRecord> testList = aParser.Parsefile(resourceUrl.getPath());
        database.makeTable();
        database.addCrimeList(testList, 1);
        database.addCrimeList(testList, 2);
        ArrayList < String > filterList = new ArrayList<String>();
        ArrayList<CrimeRecord> filterOutput = database.filterCrimes(1, filterList);
        assertEquals(5000, filterOutput.size());
        filterOutput = database.filterCrimes(2, filterList);
        assertEquals(5000, filterOutput.size());
    }


    @Test
    public void  testgetDistnct() throws  Exception{
        ArrayList<CrimeRecord> testList = new ArrayList<CrimeRecord>();
        Calendar calendar = new GregorianCalendar();
        Date tempdate = new Date();
        long tempTime = 140385426;
        tempdate.setTime(tempTime * 1000);
        calendar.setTime(tempdate);

        CrimeRecord testRecord = new CrimeRecord("HX321538",calendar,"080XX S HALSTED ST","860","THEFT", "RETAIL THEFT","DEPARTMENT STORE","true","false",621,21,"06",1172409,1851438,0.1f,0.1f);
        testList.add(testRecord);
        database.makeTable();
        database.addCrimeList(testList, 1);
        ArrayList<String> filterOutput = database.getDistnct(1, "PRIMARY_DESCRIPTION");
        assertEquals(1, filterOutput.size());
        assertEquals("THEFT", filterOutput.get(0));
    }

    @Test
    public void  testgetDistnctTwoTables() throws  Exception{
        ArrayList<CrimeRecord> testList = new ArrayList<CrimeRecord>();
        ArrayList<CrimeRecord> testList2 = new ArrayList<CrimeRecord>();
        Calendar calendar = new GregorianCalendar();
        Date tempdate = new Date();
        long tempTime = 140385426;
        tempdate.setTime(tempTime * 1000);
        calendar.setTime(tempdate);

        CrimeRecord testRecord = new CrimeRecord("HX321538",calendar,"080XX S HALSTED ST","860","THEFT", "RETAIL THEFT","DEPARTMENT STORE","true","false",621,21,"06",1172409,1851438,0.1f,0.1f);
        CrimeRecord testRecord2 = new CrimeRecord("HX321538",calendar,"NewRalue st","86","ASSAULT", "RETAIL THEFT","DEPARTMENT STORE","true","false",621,21,"06",1172409,1851438,0.1f,0.1f);
        testList.add(testRecord);
        testList2.add(testRecord2);
        database.makeTable();
        database.addCrimeList(testList, 1);
        database.addCrimeList(testList2, 2);
        ArrayList<String> filterOutput = database.getDistnct(2, "PRIMARY_DESCRIPTION");
        assertEquals(1, filterOutput.size());
        assertEquals("ASSAULT", filterOutput.get(0));
    }
}