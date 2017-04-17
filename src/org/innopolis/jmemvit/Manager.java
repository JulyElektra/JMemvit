package org.innopolis.jmemvit;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.jdt.debug.core.IJavaThread;
import org.eclipse.swt.widgets.Display;
import org.innopolis.jmemvit.extractors.StackExtractor;
import org.innopolis.jmemvit.json.JsonBuilder;
import org.json.JSONObject;

/*
 * Controls how user actions modify data and if view shows an actual results
 */
public class Manager {
	private JsonStorage jsonStorage;
	private View view;
	private DebugEventListener jdiEventListener;
	
	
	public Manager(JsonStorage jsonStorage, View view,
			DebugEventListener jdiEventListener) {
		super();
		this.jsonStorage = jsonStorage;
		this.view = view;
		this.jdiEventListener = jdiEventListener;
	}

	
	/* 
	 * Method checks if there was any changes in events. Result will 
	 * be used for deciding if it is needed to refresh view or not.
	 */
	private boolean hasEventUpdates() {
		
		if(jdiEventListener != null && jdiEventListener.isItUpdatedThread()) {
			// Events exist and there is changers in event
			return true;
		}
		else {
			// There is no events or there is not changers in event
			return false;
		}		
	}	
	
	/* 
	 * This method returns stack frames of current thread
	 */
	private IStackFrame[] getActualStackFrames() {
		// Get current thread to extract data
		IJavaThread currentThread = jdiEventListener.getCurrentThread();
		StackExtractor stack = new StackExtractor(currentThread);
				
		// Get stack frames
		IStackFrame[] frames = stack.getStackFrames();
		return frames;
	}
	
	/*
	 * Method visualizes all elements in view
	 */
	private void manageUpdates() throws DebugException{
			
		// Check if there is any updates
		if (hasEventUpdates()) {			
			IStackFrame[] frames = getActualStackFrames();
			
			JsonBuilder jsonBuilder = new JsonBuilder();
			// Writing data in JSON
			JSONObject json = jsonBuilder.getJson(frames);
			jsonStorage.addToJson(json);
			
			
			jsonStorage.setCurrentStateNumber(1);
			view.visualizeCurrentState();
						
//			// TODO delete in the final version
//			String jsonString = json.toString();
//			org.innopolis.jmemvit.temporal.MyFileWriter.write(jsonString);
		}
		else {
			// There is no updates or nothing
			// to update in visualization
		}
	}


	public void manage() {
		DebugPlugin debugPlugin = DebugPlugin.getDefault();
		debugPlugin.addDebugEventListener(jdiEventListener);		
		
		Runnable runnable = new RunnableForThread();
		Thread thread = new Thread(runnable);
		thread.start();	
		
	}
	
	/*
	 * An inner class of class Manager. It checks if there are any updates 
	 * in obtained data or a new user action
	 */
	class RunnableForThread implements Runnable{
		public void run() {
			while (true) {
				try { 
					Thread.sleep(1000); 
				} catch (Exception e) { 
				}
				Runnable task = () -> { 
					try {
						manageUpdates();
					} catch (DebugException e) {						
						e.printStackTrace();
					}
				};
				Display.getDefault().asyncExec(task);
			}			
		}
	}

}
