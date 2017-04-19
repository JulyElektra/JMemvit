package org.innopolis.jmemvit.model;

import static org.innopolis.jmemvit.utils.Global.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.swt.SWTException;

import com.sun.jdi.Field;
import com.sun.jdi.ObjectReference;


/**
 * The Variable class stores information about variables 
 */
public class Variable implements Comparable<Variable>{
	
	private String name;
	private String type;
	private String value;
	private String hasValueChanged;
	private String hasJustInitialized;
	private ArrayList<Variable> fields = new ArrayList<Variable>();;
	
	
	/**
	 * The constructor
	 * @param hasValueChanged 
	 */
	public Variable(String name, String type, String value, String fields, String hasValueChanged) {
		this.name = name;
		this.type = type;
		this.value = value;
		this.fields = parseFields(fields); 
		this.hasValueChanged = hasValueChanged;
		this.hasJustInitialized = "false";
	}
	
	private ArrayList<Variable> parseFields(String fieldsString) {
		if (fieldsString == null || fieldsString.length() == 0 || !fieldsString.contains(";")) {
			return this.fields;
		}
		
		String[] allVariablesData = fieldsString.split(";");
		
		
		ArrayList<String> keys = new ArrayList<String>();
		keys.add(KEY + NAME.toUpperCase());
		keys.add(KEY + TYPE.toUpperCase());
		keys.add(KEY + VALUE.toUpperCase());
		keys.add(KEY + HAS_VALUE_CHANGED.toUpperCase());
		
		
		
		for (String allVariableData: allVariablesData) {
			Scanner scn = new Scanner(allVariableData);			
			
			String name = "";
			String type = "";
			String value = "";
			String hasValueChanged = "";
			
			String key = null;
			while (scn.hasNext()) {				
				String currentString = scn.next();
				
				if (keys.contains(currentString)) {
					key = currentString;
					if (scn.hasNext()) {
						currentString = scn.next();
					} else {
						break;
					}
				}
				while (!keys.contains(currentString)) {
					if (key == null) {
						break;
					}
					if (key.equals(KEY + NAME.toUpperCase())) {
						name = name + currentString;
					}
					if (key.equals(KEY + TYPE.toUpperCase())) {
						type = type + currentString;
					}
					if (key.equals(KEY + VALUE.toUpperCase()))  {
						value = value + currentString;
					}
					if (key.equals(KEY + HAS_VALUE_CHANGED.toUpperCase())) {
						hasValueChanged = hasValueChanged + currentString;
					}
					if (scn.hasNext()) {
						currentString = scn.next();
					} else {
						break;
					}
				}
				if (keys.contains(currentString)) {
					key = currentString;
				}
	
			}
			
			if (name != "" && type != "") {
					Variable v = new Variable(name, type, value, null, hasValueChanged);
					this.fields.add(v);
			}
			
			scn.close();
		}			
		return this.fields;
	}
	

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getValue() {
		return value;
	}
	

	public String getHasValueChanged() {
		return hasValueChanged;
	}



	public ArrayList<Variable> getFields() {
		return fields;
	}
	
	public static void sortVariablesByType (ArrayList<Variable> vars) {
		Collections.sort(vars);
	}

	@Override
	public int compareTo(Variable v) { 
		String thisType = this.getType();
		String varType = v.getType();
		if (thisType.equals(varType)) {
			String thisName = this.getName();
			String varName = v.getName();
			return thisName.compareTo(varName);
		}
		return thisType.compareTo(varType);
	}
	
	public boolean equalsNameAndType(Variable v) {
		if (this.getName().equals(v.getName()) 
				&& this.getType().equals(v.getType())) {
			return true;
		}
		return false;
	}

	public String getHasJustInitialized() {
		return hasJustInitialized;
	}

	public void setHasJustInitialized(boolean b) {
		this.hasJustInitialized = b + "";
	}
	

}
