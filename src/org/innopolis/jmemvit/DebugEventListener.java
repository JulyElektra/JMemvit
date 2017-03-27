package org.innopolis.jmemvit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.debug.core.IJavaThread;
import org.eclipse.jdt.internal.debug.core.model.JDIDebugTarget;

import com.sun.jdi.ClassLoaderReference;
import com.sun.jdi.Field;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.Value;
import com.sun.jdi.VirtualMachine;

/**
 * The DebugEventListener class is checking new events through debugger
 */
public class DebugEventListener implements IDebugEventSetListener{

	private IJavaThread currentThread; 
	private boolean itIsUpdatedThread;
	private static HashMap<Long, ObjectReference> objects = new HashMap<Long, ObjectReference>();
	
	/**
	 * The constructor
	 */
	public DebugEventListener() {
	}

	@Override
	public void handleDebugEvents(DebugEvent[] events) {
		for (DebugEvent event : events){	
			if (event.getSource() instanceof IJavaThread){
				IJavaThread thread = (IJavaThread) event.getSource();
				setCurrentThread(thread);
				setItIsUpdatedThread(true);
			}
		}	
	}

	private void setCurrentThread(IJavaThread thread) {
		currentThread = thread;
	}
	
	private void setItIsUpdatedThread(boolean value) {
		itIsUpdatedThread = value;
	}	
	
	public IJavaThread getCurrentThread() {
		setItIsUpdatedThread(false);
		return currentThread;		
	}		

	public boolean isItUpdatedThread(){
		return itIsUpdatedThread;
	}
	
	public static VirtualMachine getJVM(IStackFrame frame) {//, ArrayList<IVariable> heapFromStack){
		if (frame == null){
			return null;
		}
		VirtualMachine JVM = null;
		ILaunch launch = frame.getLaunch();	
		Object[] LaucnChildren = launch.getChildren();
		for (Object child : LaucnChildren){
			if (child instanceof org.eclipse.jdt.internal.debug.core.model.JDIDebugTarget){
				JDIDebugTarget DebugTarget = (JDIDebugTarget) child;
				JVM = DebugTarget.getVM();
				
				

				
				List<ReferenceType> classes = JVM.allClasses();
				for (ReferenceType Class: classes) {
					String className = Class.name();
					boolean print = true;
//					if (className.contains("java.")){print = false;}
//					if (className.contains("sun.")){print = false;}
//					if (className.contains("short[]")){print = false;}
//					if (className.contains("long[]")){print = false;}
//					if (className.contains("boolean[]")){print = false;}
//					if (className.contains("byte[]")){print = false;}
//					if (className.contains("byte[][]")){print = false;}
//					if (className.contains("char[]")){print = false;}
//					if (className.contains("double[]")){print = false;}
//					if (className.contains("float[]")){print = false;}
//					if (className.contains("int[]")){print = false;}
									
//					if (.contains(className)){print = false;}
					
					if (!print){continue;}	
					
					List<ObjectReference> classObjects = Class.instances(0);
						if (objects == null) {
							break;
						} else {
							for (ObjectReference o: classObjects) {
								objects.put(o.uniqueID(), o);
							}
							
						}
//						for (ObjectReference obj: objects) {
//							List<Field> fields = class_.allFields();
//							for (Field field: fields) {
//								if ((field.name().contains("mapB") || field.name().contains("mapC")) &&
//										obj.getClass().getName().contains("ObjectReferenceImpl")) {
//									System.out.println("obj class: " + obj.getClass().getName() + " // name: " +  class_.name() + "// id: " +  obj.uniqueID());
//									Value value = obj.getValue(field); 
//									System.out.println("  field: " + field.declaringType() + " " + field.name() + " " +
//											value);
//							
//								}
//							}
//						}
					
					

					
				} 

				break;
			}						
		}			
		return JVM;
	}
	
	public static ObjectReference getObjectRef(Long id) {
		return objects.get(id);
	}
}
