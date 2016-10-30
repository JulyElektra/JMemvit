package org.innopolis.jmemvit;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.jdt.internal.debug.core.model.JDIDebugTarget;

import com.sun.jdi.ReferenceType;
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
}
