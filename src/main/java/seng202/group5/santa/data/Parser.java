package seng202.group5.santa.data;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Contains a method that runs through a .csv file and creates a CrimeRecord for each crime
 * @author Group5
 */
public class Parser {

	/**
	 * Function that parses the .csv file
	 * @param filename The absolute path of a .csv file to be parsed
	 * @return An ArrayList containing CrimeRecords
	 */
	public ArrayList<CrimeRecord> Parsefile(String filename){
		ArrayList<CrimeRecord> crimeList = new ArrayList<CrimeRecord>();
		BufferedReader br = null;
		String line;
		File file = new File(filename);
		// If the file is greater than 100MB then it will not be imported
		if (file.length() > 100000000) {
			return crimeList;
		}
		try {
			br = new BufferedReader(new FileReader(filename));
			int loopCount = 0;
			while ((line = br.readLine()) != null) {
				// This 'if' statement ensures the parser does not try to make a CrimeRecord from the first line of .csv (which contains the titles)
				if (loopCount == 0) {
					loopCount++;
					continue;
				}
				// Regex is used to make sure the .split method splits only where it should
				String[] crime = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);

				String bool1;
				String bool2;
				if (crime[7].equals("Y")) {
					bool1 = "true";
				} else {
					bool1 = "false";
				}
				if (crime[8].equals("Y")) {
					bool2 = "true";
				} else {
					bool2 = "false";
				}

				int element9;
				int element10;
				int element12;
				int element13;
				float element14;
				float element15;

				try {
					element9 = Integer.parseInt(crime[9]);
				} catch(NumberFormatException ex) {
					element9 = 0;
				}

				try {
					element10 = Integer.parseInt(crime[10]);
				} catch(NumberFormatException ex) {
					element10 = 0;
				}

				try {
					element12 = Integer.parseInt(crime[12]);
				} catch(NumberFormatException ex) {
					element12 = 0;
				}

				try {
					element13 = Integer.parseInt(crime[13]);
				} catch(NumberFormatException ex) {
					element13 = 0;
				}


				try {
					element14 = Float.parseFloat(crime[14]);
				} catch(NumberFormatException ex) {
					element14 = 0;
				}

				try {
					element15 = Float.parseFloat(crime[15]);
				} catch(NumberFormatException ex) {
					element15 = 0;
				}

				Calendar calendar = new GregorianCalendar();
				SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
				Date date = null;

				try {
					date = dateFormat.parse(crime[1]);
				} catch (ParseException e) {
					date = null;
				}

				if (date != null) {
					calendar.setTime(date);
				}
				CrimeRecord aCrimeRecord = new CrimeRecord(crime[0], calendar, crime[2], crime[3], crime[4],
															crime[5], crime[6], bool1, bool2, element9, element10,
															crime[11], element12, element13, element14, element15);
				crimeList.add(aCrimeRecord);
				loopCount++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return crimeList;
	}
}