package org.innopolis.jmemvit.model;

import java.util.ArrayList;

/**
 * The Heap class contains of textual information of heap and
 * it is used for extracting information from JSON and storing this information
 */
public class Heap {
	
	private ArrayList<Variable> vars;

	/**
	 * The constructor
	 */	
	public Heap() {
		this.vars = new ArrayList<Variable>();
	}
	
	public Heap(ArrayList<Variable> vars) {
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
