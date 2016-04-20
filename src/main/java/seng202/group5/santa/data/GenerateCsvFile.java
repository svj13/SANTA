package seng202.group5.santa.data;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * @author group5
 * Writes a user list to a .csv file
 */
public class GenerateCsvFile {

    /**
     * Export crimes into a file with a given name.
     * If that file already exists it is over written.
     * @param data The list of crime record that are to be exported
     * @param fileName The name of the file that the crimes are to be added to
     */
    public static void generateCsv(ArrayList<CrimeRecord> data, String fileName){
        try
        {
            // create a csv file to be written to
            FileWriter writer = new FileWriter(fileName + ".csv");
            // add the first line of the file
            writer.append("CASE#,DATE  OF OCCURRENCE,BLOCK, IUCR, PRIMARY DESCRIPTION, SECONDARY DESCRIPTION, LOCATION DESCRIPTION,ARREST,DOMESTIC,BEAT,WARD,FBI CD,X COORDINATE,Y COORDINATE,LATITUDE,LONGITUDE,LOCATION\n");
            // goes through the crime list adding all the records to the csv file
            for (int i = 0; i < data.size(); i++) {
                CrimeRecord temp = data.get(i);
                writer.append(temp.getCaseNumber());
                writer.append(',');
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
                writer.append(dateFormat.format(temp.getBetterCrimeDate().getTime()));  //SimpleDateFormat("yyyy.MM.dd 'at' HH:mm")
                writer.append(temp.getCrimeDate());
                writer.append(',');
                writer.append(temp.getBlock());
                writer.append(',');
                writer.append(temp.getIucr());
                writer.append(',');
                writer.append(temp.getPrimaryDescription());
                writer.append(',');
                writer.append(temp.getSecondaryDescription());
                writer.append(',');
                writer.append(temp.getLocationDescription());
                writer.append(',');
                if (temp.getArrest().equals("true")) {
                    writer.append("Y");
                } else {
                    writer.append("N");
                }
                writer.append(',');
                if (temp.getDomestic().equals("true")) {
                    writer.append("Y");
                } else {
                    writer.append("N");
                }
                writer.append(',');
                writer.append(temp.getBeat().toString());
                writer.append(',');
                writer.append(temp.getWard().toString());
                writer.append(',');
                writer.append(temp.getFbi_cd());
                writer.append(',');
                writer.append(temp.getXCoordinate().toString());
                writer.append(',');
                writer.append(temp.getYCoordinate().toString());
                writer.append(',');
                writer.append(temp.getLatitude().toString());
                writer.append(',');
                writer.append(temp.getLongitude().toString());
                writer.append('\n');
            }
            //generate whatever data you want
            writer.flush();
            writer.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }
}
