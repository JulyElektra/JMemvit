package org.innopolis.jmemvit;

import java.util.ArrayList;

/**
 * The StackStrings class contains of textual information of stack and
 * it is used for extracting information from JSON and storing this information
 */
public class StackStrings {
	
	private ArrayList<StackFrame> stackFrames;

	/**
	 * The constructor
	 */
	public StackStrings(ArrayList<StackFrame> stackFrames) {
		this.stackFrames = stackFrames;
	}
}




