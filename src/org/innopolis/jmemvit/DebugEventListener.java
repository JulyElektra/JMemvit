package org.innopolis.jmemvit;

import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.jdt.debug.core.IJavaThread;

/**
 * The DebugEventListener class is checking new events through debugger
 */
public class DebugEventListener implements IDebugEventSetListener{

	private IJavaThread currentThread; 
	private boolean itIsUpdatedThread;
	
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
}
