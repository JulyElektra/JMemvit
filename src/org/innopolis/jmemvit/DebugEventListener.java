package org.innopolis.jmemvit;

import java.util.List;

import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.jdt.debug.core.IJavaThread;
import org.eclipse.jdt.internal.debug.core.model.JDIDebugTarget;
import org.innopolis.jmemvit.extractors.VariableExtractor;

import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;

/**
 * The DebugEventListener class is checking new events through debugger
 */
public class DebugEventListener implements IDebugEventSetListener{

	private IJavaThread currentThread; 
	private boolean itIsUpdatedThread;
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
	
	public static VirtualMachine getJVM(IStackFrame frame) {
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
					print = VariableExtractor.isNotSkippedClasses(className);

				
					if (!print){continue;}	
					
					List<ObjectReference> classObjects = Class.instances(0);
						if (objects == null) {
							break;
						} else {
							for (ObjectReference o: classObjects) {
								objects[(int) o.uniqueID()] = o;
							}
							
						}
	
				} 

				break;
			}						
		}			
		return JVM;
	}
	
	public static ObjectReference getObjectRef(long id) {
		int id_ = (int) id;
		return objects[id_];
	}
}
