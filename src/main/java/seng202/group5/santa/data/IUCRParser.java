package seng202.group5.santa.data;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Contains a method that runs through a .csv file and creates a CrimeRecord for each entry
 * @author Group5
 */
public class IUCRParser {

	/**
	 * Function that parses the .csv file
	 * @return an ArrayList containing CrimeRecords
	 */
	public static ArrayList<String[]> Parsefile(){
		int first = 0;
		ArrayList<String[]> codeList = new ArrayList<String[]>();
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream in = classloader.getResourceAsStream("IUCRCodeCsv/IUCR.csv");
		String text = new Scanner(in, "UTF-8").useDelimiter("\\A").next();
		String[] lines = text.split("\n", -1);
		for(String line : lines) {
			if (first == 0) {
				first = 1;
				continue;
			}
			// Regex is used to make sure the .split method splits only were is should
			String[] crimeCode = line.split(",", -1);
			if (crimeCode.length < 3) {
				continue;
			}
			String[] tempList = new String[3];
				tempList[0] = crimeCode[0];
				tempList[1] = crimeCode[1];
				tempList[2] = crimeCode[2];

				codeList.add(tempList);
			}
		return codeList;
	}
}
