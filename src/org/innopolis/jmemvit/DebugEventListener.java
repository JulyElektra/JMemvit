package org.innopolis.jmemvit;

import java.util.Date;
import java.util.List;

import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.jdt.debug.core.IJavaThread;
import org.eclipse.jdt.internal.debug.core.model.JDIDebugTarget;

import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;

/**
 * The DebugEventListener class is checking new events through debugger
 */
public class DebugEventListener implements IDebugEventSetListener{

	private IJavaThread currentThread; 
	private boolean itIsUpdatedThread;
//	private static HashMap<Long, ObjectReference> objects = new HashMap<Long, ObjectReference>();
	private static ObjectReference[] objects = new ObjectReference[10000];
	
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
				
				
//				Date start = new Date();
//				System.out.println(start.getTime());
				
				
				List<ReferenceType> classes = JVM.allClasses();

				

				
				for (ReferenceType Class: classes) {
					String className = Class.name();
					boolean print = true;
					print = Variable.isNotSkippedClasses(className);

									
//					if (.contains(className)){print = false;}
					
					if (!print){continue;}	
					
					List<ObjectReference> classObjects = Class.instances(0);
						if (objects == null) {
							break;
						} else {
							for (ObjectReference o: classObjects) {
								objects[(int) o.uniqueID()] = o;
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
				
//				Date finish = new Date();
//				System.out.println(finish.getTime());
//				Long dif = finish.getTime() - start.getTime();
//				System.out.println("dif: " + dif + "; ");

				break;
			}						
		}			
		return JVM;
	}
	
	public static ObjectReference getObjectRef(long id) {
//		return objects.get(id);
		int id_ = (int) id;
		return objects[id_];
	}
}
