package org.innopolis.jmemvit.extractors;

import static org.innopolis.jmemvit.utils.Global.FALSE;
import static org.innopolis.jmemvit.utils.Global.FIELDS;
import static org.innopolis.jmemvit.utils.Global.HAS_VALUE_CHANGED;
import static org.innopolis.jmemvit.utils.Global.KEY;
import static org.innopolis.jmemvit.utils.Global.NAME;
import static org.innopolis.jmemvit.utils.Global.TYPE;
import static org.innopolis.jmemvit.utils.Global.VALUE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.swt.SWTException;
import org.innopolis.jmemvit.DebugEventListener;

import com.sun.jdi.Field;
import com.sun.jdi.ObjectReference;

/*
 * This class is used for deriving information about the the variable of the debugged program
 */
public class VariableExtractor {

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
//			e.printStackTrace();
		}
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
		HashMap<ObjectReference, List<Field>> varFields = getVarFieldsList(var);
		
		if (varFields == null) {
			return fields;
		}
		
		for (HashMap.Entry<ObjectReference, List<Field>> entry: varFields.entrySet()) {
			ObjectReference obj = entry.getKey();
			for (Field field: entry.getValue()) { 
				if(obj == null) {
					break;
				}
				Map<String, String> varMap  = new HashMap<String, String>();
				String declarType = field.declaringType() + "";
				String varName = "";
				try {
					if (declarType.equals(var.getReferenceTypeName()) ){
						varName = field.name();
					} else {
						varName = declarType + "." + field.name();
					}
				} catch (DebugException e1) {
					e1.printStackTrace();
				}
				varMap.put(KEY + NAME.toUpperCase(), varName);
				try {
					String varValue = obj.getValue(field) + "";
					varMap.put(KEY + VALUE.toUpperCase(), varValue);
				} catch (SWTException e) {
					e.printStackTrace();
				}
				
				String varType = field.typeName();
				varMap.put(KEY + TYPE.toUpperCase(), varType);
				String hasValueChanged  = FALSE;
				varMap.put(KEY + HAS_VALUE_CHANGED.toUpperCase(), hasValueChanged);
	
		
				for (java.util.Map.Entry<String, String> mapEntry: varMap.entrySet()) {
					fields = fields + mapEntry.getKey() + " " + mapEntry.getValue() + " ";
				}
				fields = fields + " ; ";
			}
		}
		return fields;	
	}

	private static HashMap<ObjectReference, List<Field>> getVarFieldsList(IVariable var) {
		HashMap<ObjectReference, List<Field>> varFields = new HashMap<ObjectReference, List<Field>>();
		try {
			String varValue = var.getValue().getValueString();
			Long id = parseID (varValue);
			if (id == null) {
				return null;
			}
			ObjectReference obj = DebugEventListener.getObjectRef(id);
			if (obj == null) {
				return null;
			}
			varFields.put(obj, obj.referenceType().allFields());
		} catch (DebugException e) {
			e.printStackTrace();
		}
		return varFields;
	}
	
	
	private static Long parseID(String value)  {
		Long id = null;
		try {
			id = Long.parseLong(value.replaceAll("[^0-9]", ""));
		} catch (NumberFormatException e) {
//			e.printStackTrace();
		}
		
		return id;
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
	
	
	public static ArrayList<IVariable> getObjectsFromVars(
			ArrayList<IVariable> vars) {
		ArrayList<IVariable> fieldsObjects = new ArrayList<IVariable>();
		for (IVariable var: vars) {		
			if (isObjectType(var)) {
				fieldsObjects.add(var);
			}
		}
		return fieldsObjects;
	}

	public static ArrayList<IVariable> getFieldsOfVars(ArrayList<IVariable> vars) {
		ArrayList<IVariable> fields = new ArrayList<IVariable>();
		for (IVariable var: vars) {
			IValue v;
			try {				
				v = var.getValue();
				if (!isNotSkippedClasses(v.getReferenceTypeName())) {
					continue;
				}
				fields.addAll(Arrays.asList(v.getVariables()));
				
			} catch (DebugException e) {
				e.printStackTrace();
			}
		}
		return fields;
	}
	
	
	public static boolean isNotSkippedClasses(String className) {
		if (className.contains("java.util.ArrayList")) {return true;}
		if (className.contains("java.")){return false;}
		if (className.contains("sun.")){return false;}
		if (className.contains("short[]")){return false;}
		if (className.contains("long[]")){return false;}
		if (className.contains("boolean[]")){return false;}
		if (className.contains("byte[]")){return false;}
		if (className.contains("byte[][]")){return false;}
		if (className.contains("char[]")){return false;}
		if (className.contains("double[]")){return false;}
		if (className.contains("float[]")){return false;}
		if (className.contains("int[]")){return false;}

		return true;
	}
	
}
