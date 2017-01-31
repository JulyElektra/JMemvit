package org.innopolis.jmemvit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * The State class storing the information about time and actual stack and heap at that time
 */
public class State implements Comparable<Object>{
	
	private String date;	
	private StackStrings stack;
	private HeapStrings heap;
		
	/**
	 * The constructor
	 */
	public State(String date, StackStrings stack, HeapStrings heap) {
		this.date = date;
		this.stack = stack;
		this.heap = heap;
	}

	public StackStrings getStack() {
		return stack;
	}

	public void setStack(StackStrings stack) {
		this.stack = stack;
	}

	public HeapStrings getHeap() {
		return heap;
	}

	public void setHeap(HeapStrings heap) {
		this.heap = heap;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	/*
	 * States are compared by date and time
	 */
	@Override
	public int compareTo(Object o) {
		int comparison = 0;		
		if (o instanceof State) {
			try {
				Date thisDate = getDateTime(date);
				State os = (State) o;
				Date anotherDate = getDateTime(os.getDate());	
				comparison = thisDate.compareTo(anotherDate);
			}
			catch (ParseException e) {
				e.printStackTrace();
			}
		}
		else {
			throw new TypeNotPresentException ("Different types to compare", null);
		}
		return comparison;
	}
	
	/*
	 * Convert String date format into Date format
	 */
	private static Date getDateTime(String dateString) throws ParseException {		
		SimpleDateFormat dateFormated = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss:SSS");
		Date date = dateFormated.parse(dateString);
		return date;		
	}
}
