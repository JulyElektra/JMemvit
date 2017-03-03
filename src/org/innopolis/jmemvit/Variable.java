package org.innopolis.jmemvit;

import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElementDecl.GLOBAL;

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
	private String fields;
	private String hasValueChanged;
	
	
	/**
	 * The constructor
	 * @param hasValueChanged 
	 */
	public Variable(String name, String type, String value, String fields, String hasValueChanged) {
		this.name = name;
		this.type = type;
		this.value = value;
		this.fields = fields; 
		this.hasValueChanged = hasValueChanged;
	}
	
	/**
	 * The constructor
	 */
	/*public Variable(Map<String, Object> varData) {
		this.name = (String) varData.get(Global.NAME);
		this.type = (String) varData.get(Global.TYPE);
		this.value = (String) varData.get(Global.VALUE);
	}*/

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

	/*
	 * Checks if variable is object type or not
	 */
	public static boolean isObjectType(IVariable var) {
		String varType = "";
		try {
			varType = var.getReferenceTypeName();
		} catch (DebugException e) {
			e.printStackTrace();
		}
		return isObjectType(varType);
	}
	
	/*
	 * Checks if type is object type or not
	 */
	private static boolean isObjectType(String type) {
		if ((type.equals("byte")) || 
				(type.equals("short")) || 
				(type.equals("int")) || 
				(type.equals("long")) || 
				(type.equals("float")) || 
				(type.equals("double")) || 
				(type.equals("char")) || 
				(type.equals("boolean"))) {
			// Variable is primitive type
			return false;
		}
		// Variable is object type
		return true;
	}
	
	/*
	 * Convert list of variables into list of each variable data in MAP: name, type, value
	 */
	public static ArrayList<Map<String, String>> getVarsList (ArrayList<IVariable> vars)  {
		ArrayList<Map<String, String>> varsList = new ArrayList<Map<String, String>>();
		for (IVariable var: vars) {
			Map<String, String> varMap = getVarNameValueTypeFields(var);
			varsList.add(varMap);
		}
		return varsList;
	}
	
	
	
	private static Map<String, String> getVarNameValueTypeFields(IVariable var) {
		Map<String, String> varMap = getVarNameValueType(var);
		varMap.put(Global.FIELDS, getVarFields(var));
	
		return varMap;
	}

	
	private static boolean isString(IVariable var) {
		try {
			String type = var.getReferenceTypeName();
			if (type.contains("java.lang.String")) {
				return true;
			}
		} catch (DebugException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private static boolean isCollection(IVariable var) {
		try {
			String type = var.getReferenceTypeName();
			if (type.contains("List") || type.contains("Set") 
					|| type.contains("Map") || type.contains("[]")) {
				return true;
			}
		} catch (DebugException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static String getVarFields(IVariable var) {
		String fields = "";
		if (isString(var)) {
			return fields;
		}
		IVariable[] varVariables;

		try {
			varVariables = var.getValue().getVariables();
			if (varVariables != null && varVariables.length > 0 ) {
			ArrayList<IVariable> fieldsList = new ArrayList<IVariable>(Arrays.asList(varVariables));
			if (isCollection(var)) {
				fields = fields +  "{";
				for (IVariable subVar: fieldsList) {
					fields = fields + subVar.getValue() + ", ";
				}
				fields = fields.substring(0, fields.length() - 2) +  "}";
				return fields;
			}		
			for (IVariable fieldVar: fieldsList) {
				Map<String, String> varMap = getVarNameValueType(fieldVar);
				for (java.util.Map.Entry<String, String> entry: varMap.entrySet()) {
					fields = fields + entry.getKey() + ": " + entry.getValue() + " ";
				}
				fields = fields + ";";
			}
		}
		} catch (DebugException e) {
			e.printStackTrace();
		}
		
		return fields;	
	}

	private static Map<String, String> getVarNameValueType(IVariable var) {
		Map<String, String> varMap  = new HashMap<String, String>();		
		String varName;
		try {
			varName = var.getName().toString();
			varMap.put(Global.NAME, varName);
			String varValue = var.getValue().toString();
			varMap.put(Global.VALUE, varValue);
			String varType = var.getReferenceTypeName();
			varMap.put(Global.TYPE, varType);
			String hasValueChanged  = var.hasValueChanged() + "";
			varMap.put(Global.HAS_VALUE_CHANGED, hasValueChanged);
		} catch (DebugException e) {
			e.printStackTrace();
		}
		return varMap;
	}

	public String getFields() {
		return fields;
	}
}
