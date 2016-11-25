package org.innopolis.jmemvit;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.debug.core.IJavaThread;
import org.eclipse.jdt.internal.debug.core.model.JDIDebugTarget;

import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.Value;
import com.sun.jdi.VirtualMachine;


public class Heap {
	
	public static VirtualMachine getJVM(IStackFrame frame){
		if (frame == null){return null;}
		VirtualMachine jvm = null;
		ILaunch launch = frame.getLaunch();	
		Object[] laucnChildren = launch.getChildren();
		for (Object child : laucnChildren){
			if (child instanceof org.eclipse.jdt.internal.debug.core.model.JDIDebugTarget){
				JDIDebugTarget debugTarget = (JDIDebugTarget) child;
				jvm = debugTarget.getVM();
				break;
			}						
		}			
		return jvm;
	}
	
	public static List<ReferenceType> getAllMyClasses(VirtualMachine jvm){
		List<ReferenceType> allClasses = jvm.allClasses();
		List<ReferenceType> allMyClasses = new  ArrayList<ReferenceType>();
		for (ReferenceType Class : allClasses){
			  String className = Class.name();
			  boolean print = true;
			  if (className.contains("java.")){print = false;}
			  if (className.contains("sun.")){print = false;}
			  if (className.contains("short[]")){print = false;}
			  if (className.contains("long[]")){print = false;}
			  if (className.contains("boolean[]")){print = false;}
			  if (className.contains("byte[]")){print = false;}
			  if (className.contains("byte[][]")){print = false;}
			  if (className.contains("char[]")){print = false;}
			  if (className.contains("double[]")){print = false;}
			  if (className.contains("float[]")){print = false;}
			  if (className.contains("int[]")){print = false;}	
			  
			  //if (className.contains("java.lang.Integer")){print = true;}
			  //if (className.contains("java.lang.String")){print = true;}
			  //if (className.contains("java.lang.Integer[]")){print = false;}
			  //if (className.contains("java.lang.IntegerC")){print = false;}
			  //if (className.contains("java.lang.Integer&")){print = false;}
			  //if (className.contains("java.lang.StringB")){print = false;}
			  //if (className.contains("java.lang.String[")){print = false;}
			  //if (className.contains("java.lang.StringC")){print = false;}
			  //if (className.contains("java.lang.Integer$")){print = false;}
			  //if (className.contains("java.lang.String$")){print = false;}
			  if (print){allMyClasses.add(Class);}			  
		}	
		return allMyClasses;
  }
	
	public static List<ReferenceType> sortByHashCode (List<ReferenceType>  parentClasses){
		if (parentClasses==null){return null;}
		if (parentClasses.size()<2){return parentClasses;}
		
		for (int i = 0; i < parentClasses.size(); i++){
			
			for (int k = i; k<parentClasses.size(); k++){
				if (parentClasses.get(k).hashCode()<parentClasses.get(i).hashCode()){
					ReferenceType temp = parentClasses.get(i);
					parentClasses.set(i, parentClasses.get(k));
					parentClasses.set(k, temp);					
				}
			}
		}
		return parentClasses;
	}
	
	
	
	
	/*public static ArrayList<String> getHeapStrings(IStackFrame frame) {
		
		// TODO подумать, переменные из стека фильтровать в хипе
				
		VirtualMachine jvm = getJVM(frame);
		// Check if JVM is not null and class instances are available
		if ((jvm != null) && (jvm.canGetInstanceInfo())){
			ArrayList<String> heapStrings = new ArrayList<>();
			heapStrings.add(Global.HEAP);				
			List<ReferenceType> allClasses = jvm.allClasses();
			for (ReferenceType Class: allClasses){
				List<ObjectReference> objInstanceList = Class.instances(10000);
				for (ObjectReference objInstance: objInstanceList){
					String objString =  objInstance.type().name() + " " + objInstance.uniqueID();
					if (!objString.contains(".") && !objString.contains("[]")) {
						
						heapStrings.add(objInstance.getClass().toString() + " = " + objString);
												
						//MyFileWriter.write(objString);
						List<com.sun.jdi.Field> fields =  Class.allFields();
						for (com.sun.jdi.Field field: fields){
							if (!field.isStatic()){
								Value v = objInstance.getValue(field);
								String valueStr = null;
								if (v == null) {
									valueStr = "null";
								}
								else {
									valueStr = v.toString();
								}								
								heapStrings.add("      " + field.name() + " = " + valueStr);
							}
							
						}
					}
					
				}				
			}
			return heapStrings;
		}
		
		return null;
	}*/
	
	
	
	public static ArrayList<IVariable> getHeap(IStackFrame[] frames) throws DebugException {
		ArrayList<IVariable> heapObjects = new ArrayList<>();
		for (IStackFrame frame: frames) {
			ArrayList<IVariable> frameObjects = findObjects(frame);
			heapObjects.addAll(frameObjects);
		}
		return heapObjects;
	}
	
	private static ArrayList<IVariable> findObjects(IStackFrame frame) throws DebugException {
		IVariable[] vars = Stack.getStackFrameVariables(frame);
		ArrayList<IVariable> objects = new ArrayList<>();
		for (IVariable var: vars) {
			if (isObjectType(var)) {
				objects.add(var);
			}
			else {
				// Variable is primitive type
			}
		}
		return objects;
	}

	private static boolean isObjectType(IVariable var) throws DebugException {
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

	public static ArrayList<String> getHeapStrings(IStackFrame[] frames) throws DebugException {
		ArrayList<IVariable> heap = getHeap(frames);
		ArrayList<String> heapStrings = Stack.variablesToStrings(heap.toArray());
		if (heapStrings != null && heapStrings.size() != 0) {
			heapStrings.add(0, Global.HEAP);				
		}
		return heapStrings;
	}
}
