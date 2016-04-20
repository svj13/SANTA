package seng202.group5.santa.data;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

/**
* Junit tests for crimeStatistics Generation.
* Created by Group5
*/
public class CrimeStatisticsTest extends TestCase {

    private Database database;

    @Before
    public void setUp() throws Exception {

        String i =  System.getProperty("user.dir");
        new File(i + File.separator + "Crime.db").delete();
        database = new Database();
    }

    @After
    public void tearDown() throws Exception {
        String i =  System.getProperty("user.dir");
        new File(i + File.separator + "Crime.db").delete();
    }

    @Test
    public void testTotalCrimes5000()
    {
        //Tests that the total amount of crimes in the 5000 crime database is calculated correctly.
        URL resourceUrl = getClass().getResource("/seng202_2015_crimes_one_year_prior_to_present_5k.csv");
        Parser aParser = new Parser();
        ArrayList<CrimeRecord> testList = aParser.Parsefile(resourceUrl.getPath());
        database.makeTable();
        database.addCrimeList(testList, 0);
        CrimeStatistics stats = new CrimeStatistics("(list_id = 0)");
        assertEquals(String.valueOf(5000), stats.getTotalCrimes());
    }

    @Test
    public void testTotalCrimes10000()
    {
        // Tests that the total amount of crimes in the 10000 crime database is calculated correctly.
        URL resourceUrl2 = getClass().getResource("/seng202_2015_crimes_one_year_prior_to_present_10k.csv");
        Parser aParser2 = new Parser();
        ArrayList<CrimeRecord> testTen = aParser2.Parsefile(resourceUrl2.getPath());
        database.makeTable();
        database.addCrimeList(testTen, 0);
        CrimeStatistics statsTen = new CrimeStatistics("(list_id = 0)");
        assertEquals(String.valueOf(9999), statsTen.getTotalCrimes()); // Dup ID - 9999 crimes loaded
    }

    @Test
    public void testArrestTotalFiveThousand()
    {
        // Tests that the total number of arrests in the 5000 crime database is calculated correctly.
        URL resourceUrl = getClass().getResource("/seng202_2015_crimes_one_year_prior_to_present_5k.csv");
        Parser aParser = new Parser();
        ArrayList<CrimeRecord> testList = aParser.Parsefile(resourceUrl.getPath());
        database.makeTable();
        database.addCrimeList(testList, 0);
        CrimeStatistics stats = new CrimeStatistics("(list_id = 0)");
        assertEquals(String.valueOf(1349), stats.getTotalArrests());
    }

    @Test
    public void testArrestTotalTenThousand()
    {
        // Tests that the total number of arrests in the 10000 crime database is calculated correctly.
        URL resourceUrl2 = getClass().getResource("/seng202_2015_crimes_one_year_prior_to_present_10k.csv");
        Parser aParser2 = new Parser();
        ArrayList<CrimeRecord> testTen = aParser2.Parsefile(resourceUrl2.getPath());
        database.makeTable();
        database.addCrimeList(testTen, 0);
        CrimeStatistics statsTen = new CrimeStatistics("(list_id = 0)");
        assertEquals(String.valueOf(2767), statsTen.getTotalArrests());
    }

    @Test
    public void testDomesticTotalFiveThousand()
    {
        // Tests that the total number of domestics in the 5000 crime database is calculated correctly.
        URL resourceUrl = getClass().getResource("/seng202_2015_crimes_one_year_prior_to_present_5k.csv");
        Parser aParser = new Parser();
        ArrayList<CrimeRecord> testList = aParser.Parsefile(resourceUrl.getPath());
        database.makeTable();
        database.addCrimeList(testList, 0);
        CrimeStatistics stats = new CrimeStatistics("(list_id = 0)");
        assertEquals(String.valueOf(774), stats.getTotalDomestics());
    }


    @Test
    public void testDomesticTotalTenThousand()
    {
        // Tests that the total number of domestics in the 10000 crime database is calculated correctly.
        URL resourceUrl2 = getClass().getResource("/seng202_2015_crimes_one_year_prior_to_present_10k.csv");
        Parser aParser2 = new Parser();
        ArrayList<CrimeRecord> testTen = aParser2.Parsefile(resourceUrl2.getPath());
        database.makeTable();
        database.addCrimeList(testTen, 0);
        CrimeStatistics statsTen = new CrimeStatistics("(list_id = 0)");
        assertEquals(String.valueOf(1608), statsTen.getTotalDomestics());
    }

    @Test
    public void testFirstDayFiveThousand()
    {
        // Checks that the first day in range for the 5000 crime dataset is found correctly
        URL resourceUrl = getClass().getResource("/seng202_2015_crimes_one_year_prior_to_present_5k.csv");
        Parser aParser = new Parser();
        ArrayList<CrimeRecord> testList = aParser.Parsefile(resourceUrl.getPath());
        database.makeTable();
        database.addCrimeList(testList, 0);
        CrimeStatistics stats = new CrimeStatistics("(list_id = 0)");
        assertEquals("Fri Jun 27 19:31:00 NZST 2014", stats.getFirstDay());
    }

    @Test
    public void testLastDayFiveThousand()
    {
        // Checks that the last day in range for the 5000 crime dataset is found correctly
        URL resourceUrl = getClass().getResource("/seng202_2015_crimes_one_year_prior_to_present_5k.csv");
        Parser aParser = new Parser();
        ArrayList<CrimeRecord> testList = aParser.Parsefile(resourceUrl.getPath());
        database.makeTable();
        database.addCrimeList(testList, 0);
        CrimeStatistics stats = new CrimeStatistics("(list_id = 0)");
        assertEquals("Thu Jul 03 14:50:00 NZST 2014", stats.getLastDay());
    }

    @Test
    public void testFirstDayTenThousand()
    {
        // Checks that the first day in range for the 10000 crime dataset is found correctly
        URL resourceUrl2 = getClass().getResource("/seng202_2015_crimes_one_year_prior_to_present_10k.csv");
        Parser aParser2 = new Parser();
        ArrayList<CrimeRecord> testTen = aParser2.Parsefile(resourceUrl2.getPath());
        database.makeTable();
        database.addCrimeList(testTen, 0);
        CrimeStatistics statsTen = new CrimeStatistics("(list_id = 0)");
        assertEquals("Fri Jun 27 19:31:00 NZST 2014", statsTen.getFirstDay());
    }

    @Test
    public void testLastDayTenThousand()
    {
        // Checks that the last day in range for the 10000 crime dataset is found correctly
        URL resourceUrl2 = getClass().getResource("/seng202_2015_crimes_one_year_prior_to_present_10k.csv");
        Parser aParser2 = new Parser();
        ArrayList<CrimeRecord> testTen = aParser2.Parsefile(resourceUrl2.getPath());
        database.makeTable();
        database.addCrimeList(testTen, 0);
        CrimeStatistics statsTen = new CrimeStatistics("(list_id = 0)");
        assertEquals("Wed Jul 09 11:00:00 NZST 2014", statsTen.getLastDay());
    }

    @Test
    public void testGetTypes()
    {
        //Tests that the stats returns a list that is populated by 31 entries
        URL resourceUrl2 = getClass().getResource("/seng202_2015_crimes_one_year_prior_to_present_10k.csv");
        Parser aParser2 = new Parser();
        ArrayList<CrimeRecord> testTen = aParser2.Parsefile(resourceUrl2.getPath());
        database.makeTable();
        database.addCrimeList(testTen, 0);
        CrimeStatistics stats = new CrimeStatistics("(list_id = 0)");
        assertNotNull(stats.getTypeStats());
        assertEquals(31, stats.getTypeStats().size());
    }

    @Test
    public void testTotalsOverTime()
    {
        //Tests that the total of the
        URL resourceUrl2 = getClass().getResource("/seng202_2015_crimes_one_year_prior_to_present_10k.csv");
        Parser aParser2 = new Parser();
        ArrayList<CrimeRecord> testTen = aParser2.Parsefile(resourceUrl2.getPath());
        database.makeTable();
        database.addCrimeList(testTen, 0);
        CrimeStatistics stats = new CrimeStatistics("(list_id = 0)");
        int sumOfAll = 0;
        for (int i = 0; i < 10; i++)
        {
            sumOfAll += stats.getTotalsOverTime().get(i).getCount();
        }
        assertEquals(9999, sumOfAll);
    }
}
