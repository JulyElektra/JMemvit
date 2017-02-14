package org.innopolis.jmemvit;

import java.util.ArrayList;

/**
 * The HeapStrings class contains of textual information of heap and
 * it is used for extracting information from JSON and storing this information
 */
public class HeapStrings {
	
	private ArrayList<Variable> vars;

	/**
	 * The constructor
	 */	
	public HeapStrings() {
		this.vars = new ArrayList<Variable>();
	}
	
	public HeapStrings(ArrayList<Variable> vars) {
		if (vars != null){
			this.vars = vars;
		} else {
			this.vars = new ArrayList<Variable>();
		}
	}


	public ArrayList<Variable> getVariables() {
		return vars;
	}
}
