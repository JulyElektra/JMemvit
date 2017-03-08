package org.innopolis.jmemvit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class DateTime {
	private static final String DATE_FORMAT = "dd.MM.yyyy hh:mm:ss:SSS";
	
	/*
	 * Returns data string format of current time and date
	 */
	public static String getCurrentDateTime() {
		Date date = new Date();
		SimpleDateFormat dateFormated = new SimpleDateFormat(DATE_FORMAT);
		String dateStr = dateFormated.format(date);
		return dateStr;		
	}
	
	/*
	 * Sorting the ArrayList of Strings by date and time
	 */
	public static ArrayList<String> sortByTime(ArrayList<String> stringsTimesList) {
		ArrayList<Date> dateTimeList = convertStringsToDates(stringsTimesList);
		Collections.sort(dateTimeList);
		ArrayList<String> resultList = convertDatesToStrings(dateTimeList);		
		return resultList;
	}

	/*
	 * Convert String date format into date format in ArrayLists
	 */
	public static ArrayList<String> convertDatesToStrings(ArrayList<Date> datesList) {
		ArrayList<String> stringList = new ArrayList<String>();
		for (Date date: datesList) {
			String stringDate = dateTimeToStringTime(date);
			stringList.add(stringDate);
		}
		return stringList;
	}

	/*
	 * Convert Date format into string date format in ArrayLists
	 */
	public static ArrayList<Date> convertStringsToDates(ArrayList<String> stringList){
		ArrayList<Date> datesList = new ArrayList<Date>();
		for (String stringDate: stringList) {
			Date date = stringTimeToDateTime(stringDate);
			datesList.add(date);
		}
		return datesList;
	}

	/*
	 * Convert String date format into Date format
	 */
	public static Date stringTimeToDateTime(String time)  {
		SimpleDateFormat dateFormated = new SimpleDateFormat(DATE_FORMAT);
		Date date = null;
		try {
			date = dateFormated.parse("01.01.1990 00:00:00:000"); // the default value in case of wrong format of data
			date = dateFormated.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;		
	}
	
	/*
	 * Convert Date format into string date format
	 */
	public static String dateTimeToStringTime(Date date) {
		SimpleDateFormat dateFormated = new SimpleDateFormat(DATE_FORMAT);
		String dateStr = dateFormated.format(date);
		return dateStr;		
	}
	
}
