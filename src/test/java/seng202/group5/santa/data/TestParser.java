package seng202.group5.santa.data;

import junit.framework.TestCase;
import org.junit.BeforeClass;
import org.junit.Test;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * JUnit tests for the parser
 * @author Group5
 */
public class TestParser extends TestCase {

	ArrayList<CrimeRecord> testList = null;

	@BeforeClass
	public void setUp() {
		URL resourceUrl = getClass().getResource("/seng202_2015_crimes_one_year_prior_to_present_5k.csv");
		Parser aParser = new Parser();
		testList = aParser.Parsefile(resourceUrl.getPath());
	}

	/**
	 * Checks if the case number was parsed correctly
	 */
	@Test
	public void testCaseNumber() {
		assertEquals(testList.get(0).getCaseNumber(), "HX321538");
	}

	/**
	 * Checks if the crime date was parsed correctly
	 */
	@Test
	public void testCrimeDate() throws ParseException {
		SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm");
		SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
		Date crimeDate = inputFormat.parse("06/27/2014 07:31:00 PM");
		assertEquals(testList.get(0).getCrimeDate(), outputFormat.format(crimeDate.getTime()));
	}

	/**
	 * Checks if the block was parsed correctly
	 */
	@Test
	public void testBlock() {
		assertEquals(testList.get(0).getBlock(), "080XX S HALSTED ST");
	}

	/**
	 * Checks if the IUCR was parsed correctly
	 */
	@Test
	public void testIUCR(){
		assertEquals(testList.get(0).getIucr(), "0860");
	}

	/**
	 * Checks if the primary description was parsed correctly
	 */
	@Test
	public void testPrimaryDescription() {
		assertEquals(testList.get(0).getPrimaryDescription(), "THEFT");
	}

	/**
	 * Checks if the secondary description was parsed correctly
	 */
	@Test
	public void testSecondaryDescription() {
		assertEquals(testList.get(0).getSecondaryDescription(), "RETAIL THEFT");
	}

	/**
	 * Checks if the location description was parsed correctly
	 */
	@Test
	public void testLocationDescription() {
		assertEquals(testList.get(0).getLocationDescription(), "DEPARTMENT STORE");
	}

	/**
	 * Checks if the arrest field was parsed correctly
	 */
	@Test
	public void testArrest() {
		assertEquals(testList.get(0).getArrest(), "true");
	}

	/**
	 * Checks if the domestic field was parsed correctly
	 */
	@Test
	public void testDomestic() {
		assertEquals(testList.get(0).getDomestic(), "false");
	}

	/**
	 * Checks if the beat field was parsed correctly
	 */
	@Test
	public void testBeat() {
		assertEquals((int) testList.get(0).getBeat(), 621);
	}

	/**
	 * Checks if the ward field was parsed correctly
	 */
	@Test
	public void testWard() {
		assertEquals((int) testList.get(0).getWard(), 21);
	}

	/**
	 * Checks if the fbi_cd field was parsed correctly
	 */
	@Test
	public void testFbi_cd() {
		assertEquals(testList.get(0).getFbi_cd(), "06");
	}

	/**
	 * Checks if the x coordinate was parsed correctly
	 */
	@Test
	public void testXCoordinate() {
		assertEquals((int) testList.get(0).getXCoordinate(), 1172409);
	}

	/**
	 * Checks if the y coordinate was parsed correctly
	 */
	@Test
	public void testYCoordinate() {
		assertEquals((int) testList.get(0).getYCoordinate(), 1851438);
	}

	/**
	 * Checks if the latitude was parsed correctly.
	 * In this case, the value 0.0 is expected.
	 * This is because the value was missing in the .csv file.
	 */
	@Test
	public void testMissingLatitude() {
		assertEquals(testList.get(0).getLatitude(), (float) 0.0);
	}

	/**
	 * Checks if the longitude was parsed correctly
	 * In this case, the value 0.0 is expected.
	 * This is because the value was missing in the .csv file.
	 */
	@Test
	public void testMissingLongitude() {
		assertEquals(testList.get(0).getLongitude(), (float) 0.0);
	}

	/**
	 * Checks if the longitude was parsed correctly
	 */
	@Test
	public void testLongitude() {
		assertEquals(testList.get(1).getLongitude(), (float) -87.70587610484924);
	}

	/**
	 * Checks if the latitude was parsed correctly
	 */
	@Test
	public void testLatitude() {
		assertEquals(testList.get(1).getLatitude(), (float) 41.8808655731203);
	}

	/**
	 * Checks if the correct number of crimes were parsed
	 */
	@Test
	public void testSize() {
		assertEquals(testList.size(), 5000);
	}

	/**
	 * Tests to make sure the crimes at the end of the .csv file are correct
	 */
	@Test
	public void testFinalEntries() {
		assertEquals(testList.get(4999).getCaseNumber(), "HX331113");
		assertEquals(testList.get(4999).getYCoordinate(), Integer.valueOf(1896707));
	}

	/**
	 * Tests to make sure the parser works correctly with 10,000 records
	 */
	@Test
	public void testParser10000() {
		URL resourceUrl = getClass().getResource("/seng202_2015_crimes_one_year_prior_to_present_10k.csv");
		Parser aParser = new Parser();
		ArrayList<CrimeRecord> testList = aParser.Parsefile(resourceUrl.getPath());
		assertEquals(testList.size(), 10000);
		assertEquals(testList.get(0).getCaseNumber(), "HX321538");
		assertEquals(testList.get(9999).getCaseNumber(), "HX340516");
		assertEquals(testList.get(9999).getYCoordinate(), Integer.valueOf(1897645));
	}
}