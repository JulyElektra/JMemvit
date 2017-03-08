package org.innopolis.jmemvit;

import java.util.Date;

/**
 * The State class storing the information about time and actual stack and heap at that time
 */
public class State implements Comparable<Object>{
	
	private String date;	
	private StackFrameStrings stack;
	private HeapStrings heap;
		
	/**
	 * The constructor
	 */
	public State(String date, StackFrameStrings stack, HeapStrings heap) {
		this.date = date;
		this.stack = stack;
		this.heap = heap;
	}

	public StackFrameStrings getStack() {
		return stack;
	}

	public void setStack(StackFrameStrings stack) {
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
