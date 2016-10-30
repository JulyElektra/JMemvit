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
		VirtualMachine JVM = null;
		ILaunch launch = frame.getLaunch();	
		Object[] LaucnChildren = launch.getChildren();
		for (Object child : LaucnChildren){
			if (child instanceof org.eclipse.jdt.internal.debug.core.model.JDIDebugTarget){
				JDIDebugTarget DebugTarget = (JDIDebugTarget) child;
				JVM = DebugTarget.getVM();
				break;
			}						
		}			
		return JVM;
	}
	
	public static List<ReferenceType> getAllMyClasses(VirtualMachine jvm){
		List<ReferenceType> AllClasses = jvm.allClasses();
		List<ReferenceType> AllMyClasses = new  ArrayList<ReferenceType>();
		for (ReferenceType Class : AllClasses){
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
			  if (print){AllMyClasses.add(Class);}			  
		}	
		return AllMyClasses;
  }
	
	public static List<ReferenceType> sortByHashCode (List<ReferenceType>  ParentClasses){
		if (ParentClasses==null){return null;}
		if (ParentClasses.size()<2){return ParentClasses;}
		
		for (int i = 0; i < ParentClasses.size(); i++){
			
			for (int k = i; k<ParentClasses.size(); k++){
				if (ParentClasses.get(k).hashCode()<ParentClasses.get(i).hashCode()){
					ReferenceType temp = ParentClasses.get(i);
					ParentClasses.set(i, ParentClasses.get(k));
					ParentClasses.set(k, temp);					
				}
			}
		}
		return ParentClasses;
	}
}
