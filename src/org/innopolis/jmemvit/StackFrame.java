package org.innopolis.jmemvit;

import java.util.ArrayList;

public class StackFrame {
	private int number;
	private String name;
	private ArrayList<Variable> vars;
	
	public StackFrame(int number, String name, ArrayList<Variable> vars) {
		this.number = number;
		this.name = name;
		this.vars = vars;
	}	
}
