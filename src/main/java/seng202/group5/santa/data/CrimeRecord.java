package seng202.group5.santa.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

/**
 * Class which contains all of the information contained within a crime record.
 * Some of these may be able to be edited (future release), with a few exceptions, such as 'Case Number'.
 * @author Group5
 */
public class CrimeRecord
{
    private String caseNumber;
    private Calendar crimeDate;
    private String block;
    private String iucr;
    private String primaryDescription;
    private String secondaryDescription;
    private String locationDescription;
    private String arrest;
    private String domestic;
    private Integer beat;
    private Integer ward;
    private String fbi_cd;
    private Integer xCoordinate;
    private Integer yCoordinate;
    private Float latitude;
    private Float longitude;

    /**
     * Creates a crime record with parsed data. Variable names are fairly self-explanatory
     * @param caseNumber Unique case identifier
     * @param crimeDate Date the crime occurred (Using java time)
     * @param block Block on which the crime occurred
     * @param iucr Illinois Uniform Crime Reporting Code
     * @param primaryDescription Primary crime type
     * @param secondaryDescription Further explanation of crime type
     * @param locationDescription Description of where the crime occurred
     * @param arrest Whether or not an arrest was made
     * @param domestic Whether or not the crime involved domestic abuse
     * @param beat The beat in which the crime occurred
     * @param ward The ward in which the crime occurred
     * @param fbi_cd Whatever this means
     * @param xCoordinate x Map coordinate of crime occurrence
     * @param yCoordinate y Map coordinate of crime occurrence
     * @param latitude Latitude of crime location
     * @param longitude Longitude of crime location
     */
    public CrimeRecord(String caseNumber, Calendar crimeDate, String block, String iucr,
                       String primaryDescription, String secondaryDescription,
                       String locationDescription, String arrest, String domestic,
                       Integer beat, Integer ward, String fbi_cd, Integer xCoordinate,
                       Integer yCoordinate, Float latitude, Float longitude)
    {
        this.caseNumber = caseNumber;
        this.crimeDate = crimeDate;
        this.block = block;
        this.iucr = iucr;
        this.primaryDescription = primaryDescription;
        this.secondaryDescription = secondaryDescription;
        this.locationDescription = locationDescription;
        this.arrest = arrest;
        this.domestic = domestic;
        this.beat = beat;
        this.ward = ward;
        this.fbi_cd = fbi_cd;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public Calendar getBetterCrimeDate() {
        return crimeDate;
    }

    public String getCrimeDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm");
        return dateFormat.format(crimeDate.getTime());
    }

    public void setCrimeDate(Calendar date) {
        this.crimeDate = date;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getIucr() {
        return iucr;
    }

    public void setIucr(String iucr) {
        this.iucr = iucr;
    }

    public String getPrimaryDescription() {
        return primaryDescription;
    }

    public void setPrimaryDescription(String primaryDescription) {
        this.primaryDescription = primaryDescription;
    }

    public String getSecondaryDescription() {
        return secondaryDescription;
    }

    public void setSecondaryDescription(String secondaryDescription) {
        this.secondaryDescription = secondaryDescription;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public String getArrest() {
        return arrest;
    }

    public void setArrest(String arrest) {
        this.arrest = arrest;
    }

    public String getDomestic() {
        return domestic;
    }

    public void setDomestic(String domestic) {
        this.domestic = domestic;
    }

    public Integer getBeat() {
        return beat;
    }

    public void setBeat(Integer beat) {
        this.beat = beat;
    }

    public Integer getWard() {
        return ward;
    }

    public void setWard(Integer ward) {
        this.ward = ward;
    }

    public String getFbi_cd() {
        return fbi_cd;
    }

    public void setFbi_cd(String fbi_cd) {
        this.fbi_cd = fbi_cd;
    }

    public Integer getXCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(Integer xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public Integer getYCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(Integer yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    /**
     * Calculates the difference in time between this crime and a given crime
     * @param otherTime  The time of the other crime
     * @return The difference in time between the two crimes
     */
    public String getTimeDifference (Calendar otherTime) {
        return Objects.toString((Math.abs((this.getBetterCrimeDate().getTimeInMillis() - otherTime.getTimeInMillis()) /60000)));
    }

    /**
     * Calculates the difference in distance between this crime and a given crime
     * @param lat1  The latitude of the other crime
     * @param lon1  The longitude of the other crime
     * @return The difference in distance between the two crimes
     */
    public double getDistanceDifference (double lat1, double lon1) {
        double theta = lon1 - getLongitude();
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(getLatitude())) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(getLatitude())) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        return (dist);
    }

    /**
     * This function converts decimal degrees to radians
     * @param deg Degrees
     * @return Radians
     */
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /**
     * This function converts radians to decimal degrees
     * @param rad Radians
     * @return Degrees
     */
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @Override
    public String toString() {
        return "Case Number:" + this.caseNumber
                + " Date:" + getCrimeDate()
                +  "Block:" + this.block
                + "		IUCR: " + this.iucr
                + "	Primary Description: " + this.primaryDescription
                +  "	Secondary Description: " + this.secondaryDescription
                + "	Location Description: " + this.locationDescription
                + "	Arrest: " + this.arrest
                +  "	Domestic: " + this.domestic
                +  "	Beat: " + this.beat
                + "		Ward: " + this.ward
                + "	FBI_CD: " + this.fbi_cd
                + "	X COORDINATE,: " + this.xCoordinate
                + "	Y COORDINATE: " + this.yCoordinate
                +	"	Latitude: " + this.latitude
                + "	Longitude: " + this.longitude;
    }
}