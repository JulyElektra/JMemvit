package org.innopolis.jmemvit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IVariable;

/**
 * The Variable class stores information about variables 
 * and does some operation with variables
 */
public class Variable {
	
	private String name;
	private String type;
	private String value;
	
	/**
	 * The constructor
	 */
	public Variable(String name, String type, String value) {
		this.name = name;
		this.type = type;
		this.value = value;
	}
	
	/**
	 * The constructor
	 */
	public Variable(Map<String, Object> varData) {
		this.name = (String) varData.get(Global.NAME);
		this.type = (String) varData.get(Global.TYPE);
		this.value = (String) varData.get(Global.VALUE);
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

	/*
	 * Checks if variable is object type or not
	 */
	public static boolean isObjectType(IVariable var) throws DebugException {
		String varType = var.getReferenceTypeName();
		if ((varType.equals("byte")) || 
				(varType.equals("short")) || 
				(varType.equals("int")) || 
				(varType.equals("long")) || 
				(varType.equals("float")) || 
				(varType.equals("double")) || 
				(varType.equals("char")) || 
				(varType.equals("boolean"))) {
			// Variable is primitive type
			return false;
		}
		// Variable is object type
		return true;
	}
	
	/*
	 * Convert list of variables into list of each variable data in MAP: name, type, value
	 */
	public static ArrayList<Map<String, String>> getVarsList (ArrayList<IVariable> vars) throws DebugException {
		ArrayList<Map<String, String>> varsList = new ArrayList<Map<String, String>>();
		for (IVariable var: vars) {
			Map<String, String> varMap = new HashMap<String, String>();
			String varName = var.getName().toString();
			varMap.put(Global.NAME, varName);
			String varValue = var.getValue().toString();
			varMap.put(Global.VALUE, varValue);
			String varType = var.getReferenceTypeName();
			varMap.put(Global.TYPE, varType);				
			varsList.add(varMap);
		}
		return varsList;
	}
}
