package seng202.group5.santa.data;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Vincent on 22/09/2015.
 */
public class TestCrimeRecord extends TestCase {

    CrimeRecord testCrimeRecord = null;

    @Before
    public void setUp() throws ParseException {
        URL resourceUrl = getClass().getResource("/seng202_2015_crimes_one_year_prior_to_present_5k.csv");
        Parser aParser = new Parser();
        ArrayList<CrimeRecord> testList = aParser.Parsefile(resourceUrl.getPath());
        testCrimeRecord = testList.get(1);
    }

    @Test
    public void testSetLongitude() {
        assertEquals(testCrimeRecord.getLongitude(), (float) -87.7058761);
        testCrimeRecord.setLongitude((float) -87.76502842);
        assertEquals(testCrimeRecord.getLongitude(), (float) -87.76502842);
    }

    @Test
    public void testSetLatitude() {
        assertEquals(testCrimeRecord.getLatitude(), (float) 41.88086557);
        testCrimeRecord.setLatitude((float) 41.96061903);
        assertEquals(testCrimeRecord.getLatitude(), (float) 41.96061903);
    }

    @Test
    public void testSetCrimeDate() throws ParseException {
        assertEquals(testCrimeRecord.getCrimeDate(), "2014.06.27 at 19:35");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        Date date = dateFormat.parse("06/27/2014 07:50:00 PM");
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        testCrimeRecord.setCrimeDate(calendar);
        SimpleDateFormat output = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm");
        assertEquals(testCrimeRecord.getCrimeDate(), output.format(date.getTime()));
    }

    @Test
    public void testSetBlock() {
        assertEquals(testCrimeRecord.getBlock(), "031XX W MADISON ST");
        testCrimeRecord.setBlock("051XX W CORNELIA AVE");
        assertEquals(testCrimeRecord.getBlock(), "051XX W CORNELIA AVE");
    }

    @Test
    public void testSetIucr() {
        assertEquals(testCrimeRecord.getIucr(), "0460");
        testCrimeRecord.setIucr("850");
        assertEquals(testCrimeRecord.getIucr(), "850");
    }

    @Test
    public void testSetPrimaryDescription() {
        assertEquals(testCrimeRecord.getPrimaryDescription(), "BATTERY");
        testCrimeRecord.setPrimaryDescription("NARCOTICS");
        assertEquals(testCrimeRecord.getPrimaryDescription(), "NARCOTICS");
    }

    @Test
    public void testSetSecondaryDescription() {
        assertEquals(testCrimeRecord.getSecondaryDescription(), "SIMPLE");
        testCrimeRecord.setSecondaryDescription("AGGRAVATED: HANDGUN");
        assertEquals(testCrimeRecord.getSecondaryDescription(), "AGGRAVATED: HANDGUN");
    }

    @Test
    public void testSetLocationDescription() {
        assertEquals(testCrimeRecord.getLocationDescription(), "GROCERY FOOD STORE");
        testCrimeRecord.setLocationDescription("SIDEWALK");
        assertEquals(testCrimeRecord.getLocationDescription(), "SIDEWALK");
    }

    @Test
    public void testSetArrest() {
        assertEquals(testCrimeRecord.getArrest(), "false");
        testCrimeRecord.setArrest("true");
        assertEquals(testCrimeRecord.getArrest(), "true");
    }

    @Test
    public void testSetDomestic() {
        assertEquals(testCrimeRecord.getDomestic(), "false");
        testCrimeRecord.setDomestic("true");
        assertEquals(testCrimeRecord.getDomestic(), "true");
    }

    @Test
    public void testSetBeat() {
        assertEquals((int) testCrimeRecord.getBeat(), 1124);
        testCrimeRecord.setBeat(342);
        assertEquals((int) testCrimeRecord.getBeat(), 342);
    }


    @Test
    public void testSetWard() {
        assertEquals((int) testCrimeRecord.getWard(), 28);
        testCrimeRecord.setWard(31);
        assertEquals((int) testCrimeRecord.getWard(), 31);
    }

    @Test
    public void testSetFbi_cd() {
        assertEquals(testCrimeRecord.getFbi_cd(), "08B");
        testCrimeRecord.setFbi_cd("04B");
        assertEquals(testCrimeRecord.getFbi_cd(), "04B");
    }


    @Test
    public void testSetXCoordinate() {
        assertEquals((int) testCrimeRecord.getXCoordinate(), 1155119);
        testCrimeRecord.setxCoordinate(1141470);
        assertEquals((int) testCrimeRecord.getXCoordinate(), 1141470);
    }

    @Test
    public void testSetYCoordinate() {
        assertEquals((int) testCrimeRecord.getYCoordinate(), 1899802);
        testCrimeRecord.setyCoordinate(1847794);
        assertEquals((int) testCrimeRecord.getYCoordinate(), 1847794);
    }

    @Test
    public void testTimeDifference() {
        Calendar calendar = new GregorianCalendar();
        Date tempdate = new Date();
        tempdate.setTime(100000000);
        calendar.setTime(tempdate);
        assertEquals("23395908", testCrimeRecord.getTimeDifference(calendar));
    }

    @Test
    public void testTimeDifferenceSameTime() {
        Calendar calendar = new GregorianCalendar();
        Date tempdate = new Date();
        long sec = 1403854500000L;
        tempdate.setTime(sec);
        calendar.setTime(tempdate);
        assertEquals("0", testCrimeRecord.getTimeDifference(calendar));
    }

    @Test
    public void testTimeDifferenceGreater() {
        Calendar calendar = new GregorianCalendar();
        Date tempdate = new Date();
        long sec = 1503954500000L;
        tempdate.setTime(sec);
        calendar.setTime(tempdate);
        assertEquals("1668333", testCrimeRecord.getTimeDifference(calendar));
    }


    @Test
    public void testDistanceDifference() {
        testCrimeRecord.setLatitude(0.0f);
        testCrimeRecord.setLongitude(0.0f);
        assertEquals(0.0, testCrimeRecord.getDistanceDifference(0, 0));
    }

    @Test
    public void testDistanceDifferenceGreat() {
        testCrimeRecord.setLatitude(0.0f);
        testCrimeRecord.setLongitude(0.0f);
        assertEquals(15.730, testCrimeRecord.getDistanceDifference(0.1f, 0.1f),10);
    }

    @Test
    public void testDistanceDifferencelessThan() {
        testCrimeRecord.setLatitude(0.0f);
        testCrimeRecord.setLongitude(0.0f);
        assertEquals(15.730, testCrimeRecord.getDistanceDifference(-0.1f, -0.1f),10);
    }

}
