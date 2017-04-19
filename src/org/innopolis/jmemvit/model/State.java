package org.innopolis.jmemvit.model;

import java.util.Date;

import org.innopolis.jmemvit.utils.DateTime;

/**
 * The State class storing the information about time and actual stack and heap at that time
 */
public class State implements Comparable<Object>{
	
	private String date;	
	private Stack stack;
	private Heap heap;
		
	/**
	 * The constructor
	 */
	public State(String date, Stack stack, Heap heap) {
		this.date = date;
		this.stack = stack;
		this.heap = heap;
	}

	public Stack getStack() {
		return stack;
	}

	public void setStack(Stack stack) {
		this.stack = stack;
	}

	public Heap getHeap() {
		return heap;
	}

	public void setHeap(Heap heap) {
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
			Date thisDate = DateTime.stringTimeToDateTime(date);
			State os = (State) o;
			Date anotherDate = DateTime.stringTimeToDateTime(os.getDate());	
			comparison = thisDate.compareTo(anotherDate);
		} else {
			try {
			throw new TypeNotPresentException ("Different types to compare", null);
			} catch (TypeNotPresentException e) {
				e.printStackTrace();
			}
		}
		return comparison;
	}
	
}
