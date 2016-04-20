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
 * Created by sam on 5/10/2015.
 */
public class IUCRParserTest extends TestCase {
    /**
     * JUnit tests for the IUCR parser
     * @author Group5
     */

    @Test
    public void testfirst() {
        assertEquals("110",IUCRParser.Parsefile().get(0)[0]);
        assertEquals("HOMICIDE",IUCRParser.Parsefile().get(0)[1]);
        assertEquals("FIRST DEGREE MURDER",IUCRParser.Parsefile().get(0)[2]);
    }

    @Test
    public void testlast() {
        assertEquals("5132",IUCRParser.Parsefile().get(400)[0]);
        assertEquals("OTHER OFFENSE",IUCRParser.Parsefile().get(400)[1]);
        assertEquals("VIOLENT OFFENDER: FAIL TO REGISTER NEW ADDRESS",IUCRParser.Parsefile().get(400)[2]);
    }

    @Test
    public void testmiddle() {
        assertEquals("1540",IUCRParser.Parsefile().get(200)[0]);
        assertEquals("OBSCENITY",IUCRParser.Parsefile().get(200)[1]);
        assertEquals("OBSCENE MATTER",IUCRParser.Parsefile().get(200)[2]);
    }

    @Test
    public void testsize() {
        assertEquals(401,IUCRParser.Parsefile().size());
    }

}