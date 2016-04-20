package seng202.group5.santa.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Class to generate the statistics needed for the stats panel.
 * Created by Group5
 */
public class CrimeStatistics {
    private int totalCrimes = 0; // total crimes in selection
    private int totalArrests = 0; // total arrests in selection
    private int totalDomestics = 0; // total domestic incidents in selection
    private long totalDays; // total days in selection
    private double totalFrequency; // total frequency of crimes - currently not used.
    private ArrayList<CrimeTuple> typeStats = new ArrayList<CrimeTuple>(); // breakdown of statistics for
                                // Crimes by Primary_Description
    private static int numTypes = 31; // Total number of Primary Descriptions
    private static String[] crimeTypes = {"ARSON", "ASSAULT", "BATTERY", "BURGLARY",
            "CONCEALED CARRY LICENSE VIOLATION", "CRIMINAL DAMAGE", "CRIMINAL TRESPASS",
            "CRIM SEXUAL ASSAULT", "DECEPTIVE PRACTICE", "GAMBLING", "HOMICIDE", "HUMAN TRAFFICKING",
            "INTERFERENCE WITH PUBLIC OFFICER", "INTIMIDATION", "KIDNAPPING", "LIQUOR LAW VIOLATION",
            "MOTOR VEHICLE THEFT", "NARCOTICS", "NON - CRIMINAL", "OBSCENITY", "OFFENSE INVOLVING CHILDREN",
            "OTHER NARCOTIC VIOLATION", "OTHER OFFENSE", "PROSTITUTION", "PUBLIC INDECENCY", "PUBLIC PEACE VIOLATION",
            "ROBBERY", "SEX OFFENSE", "STALKING", "THEFT", "WEAPONS VIOLATION"}; // Enumerated Primary Descriptions
    private Date firstDay; // First day in current selection
    private Date lastDay; // Last day in current selection
    private ArrayList<CrimeTuple> beatStats = new ArrayList<CrimeTuple>(); // Statistics for beats - Name,Count,Freq
    private ArrayList<CrimeTuple> wardStats = new ArrayList<CrimeTuple>(); // Statistics for wards - Name,Count,Freq
    private String queryAnd = " AND "; // And query - to be concatenated for fast referencing
    private String queryWhere = " WHERE ("; // Where query - to be concatenated for fast referencing
    private Database database = new Database();
    private ArrayList<CrimeTuple> crimeTotalStats = new ArrayList<CrimeTuple>();

    /**
     * Constructor for the CrimeStatistics object.
     * Calls all calculation performing methods.
     */
    public CrimeStatistics(String query)
    {
        queryWhere = queryWhere.concat(query);
        queryWhere = queryWhere.concat(")");
        queryAnd += (query);
        queryAnd += ")";
    }

    /**
     * Finds the total amount of crimes by calling the getCount method - with query
     */
    public void setCrimes(Database database)
    {
        totalCrimes = database.getCount("*", "CRIMETABLE", "", queryWhere);
    }

    /**
     * Finds the total number of arrests by calling the getCount method
     */
    public void setArrests(Database database)
    {
        totalArrests = database.getCount("arrest", "CRIMETABLE", " WHERE ((arrest='true')", queryAnd);
    }

    /**
     * Finds the total number of Domestic incidents by calling the getCount method
     */
    public void setDomestics(Database database)
    {
        totalDomestics = database.getCount("domestic", "CRIMETABLE", " WHERE ((domestic='true')", queryAnd);
    }

    /**
     * Returns the total number of crimes as a string, for ease in displaying
     * If the stats have not yet been generated, they and their dependencies are first generated.
     * @return String
     */
    public String getTotalCrimes()
    {
        if (totalCrimes == 0)
        {
            setCrimes(database);
        }
        return Integer.toString(totalCrimes);
    }

    /**
     * Returns the total number of arrests as a string, for ease in displaying
     * If the stats have not yet been generated, they and their dependencies are first generated.
     * @return String
     */
    public String getTotalArrests()
    {
        if (totalArrests == 0)
        {
            setArrests(database);
        }
        return Integer.toString(totalArrests);
    }

    /**
     * Returns the total number of Domestic incidents as a string, for ease in displaying
     * If the stats have not yet been generated, they and their dependencies are first generated.
     * @return String
     */
    public String getTotalDomestics()
    {
        if (totalDomestics == 0)
        {
            setDomestics(database);
        }
        return Integer.toString(totalDomestics);
    }

    /**
     * Finds the statistics for crimes by primary description.
     * These include: total incidences, total arrests for type, frequency over time frame, and percentage arrests.
     * Sets these as a new type CrimeTuple.
     */
    public void setTypeStats(Database database)
    {
        int count = 0;
        int arrestCount = 0;
        String current = null;
        double frequency = 0;
        double pcArrests = 0.00;
        ArrayList<CrimeTuple> tempList = new ArrayList<CrimeTuple>();
        for (int i = 0; i < numTypes; i++)
        {
            current = crimeTypes[i];
            count = database.getCount("PRIMARY_DESCRIPTION", "CRIMETABLE",
                    (" WHERE (PRIMARY_DESCRIPTION='" + current + "'"), queryAnd);
            arrestCount = database.getCount("PRIMARY_DESCRIPTION", "CRIMETABLE",
                    (" WHERE (PRIMARY_DESCRIPTION='" + current + "'" + " AND ARREST='true'"), queryAnd);
            frequency = ((double)count / this.totalDays);
            if (count == 0)
            {
                CrimeTuple tup = new CrimeTuple(current, count, frequency, 0);
                tempList.add(tup);
            }
            else
            {
                pcArrests = ((double)arrestCount / count) * 100;
                CrimeTuple tup = new CrimeTuple(current, count, frequency, pcArrests);
                tempList.add(tup);
            }
        }
        typeStats = tempList;
    }

    /**
     * Returns the list of statistics based around primary descriptions.
     * If the stats have not yet been generated, they and their dependencies are first generated.
     * @return ArrayList<CrimeTuple>
     */
    public ArrayList<CrimeTuple> getTypeStats()
    {
        if (typeStats.size() == 0)
        {
            if (firstDay == null)
            {
                setDayThings(database);
            }
            setTypeStats(database);
        }
        return typeStats;
    }

    /**
     * Finds within the data set:
     * First date
     * Last date
     * Number of Days.
     */
    public void setDayThings(Database database)
    {
        Date first = null;
        Date last = null;
        long diff;
        first = database.getDays("MIN", "CRIMETABLE", queryWhere);
        last = database.getDays("MAX", "CRIMETABLE", queryWhere);
        lastDay = last;
        firstDay = first;
        diff = TimeUnit.MILLISECONDS.toDays((lastDay.getTime()) - (firstDay.getTime()));
        totalDays = diff;
        if (totalDays == 0)
        {
            totalDays = 1;
        }
    }

    /**
     * Returns a string representation of the first day in the data set
     * If the stats have not yet been generated, they and their dependencies are first generated.
     * @return String
     */
    public String getFirstDay()
    {
        if (firstDay == null)
        {
            setDayThings(database);
        }
        return firstDay.toString();
    }

    /**
     * Returns a string representation of the last day in the data set
     * If the stats have not yet been generated, they and their dependencies are first generated.
     * @return String
     */
    public String getLastDay()
    {
        if (lastDay == null)
        {
            setDayThings(database);
        }
        return lastDay.toString();
    }

    /**
     * Return a string representation of the date-range of full days in the dataset.
     * If the stats have not yet been generated, they and their dependencies are first generated.
     * @return String
     */
    public String getTotalDays()
    {
        if (totalDays == 0)
        {
            setDayThings(database);
            setTotalFrequency();
        }
        return String.valueOf(totalDays);
    }

    /**
     * Calculates the total frequency of crimes, and sets the local variable to that value
     */
    public void setTotalFrequency()
    {
        totalFrequency = totalCrimes/(double)totalDays;
    }

    /**
     * Getter for the total frequency of crimes
     * If the stats have not yet been generated, they and their dependencies are first generated.
     * @return String
     */
    public String getTotalFrequency()
    {
        if (totalFrequency == 0)
        {
            setDayThings(database);
            setCrimes(database);
            setTotalFrequency();
        }
        return String.valueOf(totalFrequency);
    }

    /**
     * Sets the 'Totals over time', data which is used for the generation of the trend graph.
     */
    private void setTotalsOverTime()
    {
        Calendar first = Calendar.getInstance();
        first.setTime(firstDay);
        Calendar last = Calendar.getInstance();
        last.setTime(lastDay);
        long firstTime = first.getTimeInMillis() / 1000;
        long lastTime = last.getTimeInMillis() / 1000;
        long timeInterval = (lastTime - firstTime) / 10;
        ArrayList<CrimeTuple> totalStats = new ArrayList<CrimeTuple>();
        long prevDate = firstTime - 1;
        for (int i = 0; i < 10; i++)
        {
            long nextDate = prevDate + 1 + timeInterval;
            int count = database.getCount("*", "CRIMETABLE", " WHERE (DATE > " + prevDate +
                    " AND DATE <=" + nextDate, queryAnd);
            Date d = new Date();
            d.setTime(nextDate * 1000);
            totalStats.add(new CrimeTuple(d.toString(), count, 0, 0));
            prevDate = nextDate;
        }
        crimeTotalStats = totalStats;
    }

    /**
     * Getter for the crimeTotalStats variable
     * If it has not been computed yet, then it is computed before being returned
     * @return ArrayList<CrimeTuple>
     */
    public ArrayList<CrimeTuple> getTotalsOverTime()
    {
        if (crimeTotalStats.size() == 0)
        {
            if (firstDay == null)
            {
                setDayThings(database);
            }
            setTotalsOverTime();
        }
        return crimeTotalStats;
    }
}
