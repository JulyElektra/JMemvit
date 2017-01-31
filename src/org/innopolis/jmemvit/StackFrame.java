package org.innopolis.jmemvit;

import java.util.ArrayList;

/**
 * The StackFrame class is used storing information about stack frames content
 */
public class StackFrame {
	
	private int number;
	private String name;
	private ArrayList<Variable> vars;
	
	/**
	 * The constructor
	 */
	public StackFrame(int number, String name, ArrayList<Variable> vars) {
		this.number = number;
		this.name = name;
		this.vars = vars;
	}	
}
