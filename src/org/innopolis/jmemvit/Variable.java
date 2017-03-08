package org.innopolis.jmemvit;

import static org.innopolis.jmemvit.Global.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IVariable;

/**
 * The Variable class stores information about variables 
 * and does some operation with variables
 */
public class Variable implements Comparable<Variable>{
	
	private String name;
	private String type;
	private String value;
	private String hasValueChanged;
	private String hasJustInitialized;
	private ArrayList<Variable> fields = new ArrayList<Variable>();
	
	
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
		varMap.put(KEY + FIELDS.toUpperCase(), getVarFields(var));
	
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
	

	private static String getVarFields(IVariable var) {
		String fields = "";
		if (isString(var)) {
			return fields;
		}
		IVariable[] varVariables;

		try {
			varVariables = var.getValue().getVariables();

// TODO collections we want to see their elements			
//			
//			if (isCollection(var)) {
//				IVariable[] vars = var.getValue().getVariables();
//				for (IVariable varColl: vars) {
//					if (varColl.getName().equals("elementData") &&
//							varColl.getReferenceTypeName().equals("java.lang.Object[]")) {
//						varVariables = varColl.getValue().getVariables();
//					}
//				}
//			}
//			
			
			
		
			if (varVariables != null && varVariables.length > 0 ) {
			ArrayList<IVariable> fieldsList = new ArrayList<IVariable>(Arrays.asList(varVariables));

	
			for (IVariable fieldVar: fieldsList) {
				Map<String, String> varMap = getVarNameValueType(fieldVar);
				for (java.util.Map.Entry<String, String> entry: varMap.entrySet()) {
					fields = fields + entry.getKey() + " " + entry.getValue() + " ";
				}
				fields = fields + " ; ";
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
			varMap.put(KEY + NAME.toUpperCase(), varName);
			String varValue = var.getValue().toString();
			varMap.put(KEY + VALUE.toUpperCase(), varValue);
			String varType = var.getReferenceTypeName();
			varMap.put(KEY + TYPE.toUpperCase(), varType);
			String hasValueChanged  = var.hasValueChanged() + "";
			varMap.put(KEY + HAS_VALUE_CHANGED.toUpperCase(), hasValueChanged);
		} catch (DebugException e) {
			e.printStackTrace();
		}
		return varMap;
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
	
}
