package org.innopolis.jmemvit;

import java.text.ParseException;
import java.util.ArrayList;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.jdt.debug.core.IJavaThread;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;
import org.json.JSONObject;

/**
 * The ViewManager class controls graphical interface of the program
 */
public class ViewManager extends ViewPart {

	private DebugEventListener jdiEventListener;
	private Browser browser;
	private JsonBuilder jsonBuilder = new JsonBuilder();
	
	class RunnableForThread implements Runnable{
		public void run() {
			while (true) {
				try { 
					Thread.sleep(1000); 
				} catch (Exception e) { 
				}
				Runnable task = () -> { 
					try {
						vizualizateView();
					} catch (DebugException e) {						
						e.printStackTrace();
					}
				};
				Display.getDefault().asyncExec(task);
			}			
		}
	}
	
	@Override
	public void createPartControl(Composite parent) {
		
		browser = new Browser(parent, SWT.NONE);
		browser.setText("<html><body>Stack and heap will appear here.Please, start debugging.</body></html>");
		
		jdiEventListener = new DebugEventListener();
		DebugPlugin.getDefault().addDebugEventListener(jdiEventListener);		
		
		Runnable runnable = new RunnableForThread();
		Thread thread = new Thread(runnable);
		thread.start();	
	}
	

	@Override
	public void setFocus() {
	}

	/*
	 * Method visualizes all elements in view
	 */
	public void vizualizateView() throws DebugException{
			
		// Check if there is any updates
		if (hasEventUpdates()) {			
			IStackFrame[] frames = getActualStackFrames();
			
			// Writing data in JSON
			JSONObject json = jsonBuilder.getJson(frames);
			
			// Reading from JSON information about the current state
			State currentState = getCurrentState(json);
			
			// Building HTML format string with data
			String currentStateHTML = getStateHTML(currentState);
			
			// Visualization
			visualize(currentStateHTML);
			
			// TODO delete in the final version
			String jsonString = json.toString();
			MyFileWriter.write(jsonString);
		}
		else {
			// There is no updates or nothing
			// to update in visualization
		}
	}

	/*
	 * Method gets HTML string from the state
	 */	
	private String getStateHTML(State state) {
		HtmlBuilder htmlBuilder = new HtmlBuilder(state);		
		return htmlBuilder.getHtmlString();
	}

	/*
	 * Method gets the last (current) state of stack and heap from JSON
	 */
	private State getCurrentState(JSONObject json) {
		JsonReader jsonReader = new JsonReader(json);
		try {
			ArrayList<State> states = jsonReader.read();
			int current = states.size() - 1;
			return states.get(current);
		} catch (ParseException e) {
			e.printStackTrace();
		}		
		return null;
	}

	/*
	 * Method visualizes particular element
	 */
	private void visualize(String htmlString) {
		if (htmlString != null) {
			browser.setText(htmlString);
		}
		else {
			// String is null
			// Nothing to be visualized
		}			
	}
	
	/* 
	 * This method returns top stack frame of current thread
	 */
	private IStackFrame getActualTopStackFrame() throws DebugException {
		// Get current thread to extract data
		IJavaThread currentThread = jdiEventListener.getCurrentThread();
		Stack stack = new Stack(currentThread);
				
		// Get top stack frame
		IStackFrame topFrame = stack.getTopStackFrame();
		return topFrame;
	}
	
	/* 
	 * This method returns stack frames of current thread
	 */
	private IStackFrame[] getActualStackFrames() {
		// Get current thread to extract data
		IJavaThread currentThread = jdiEventListener.getCurrentThread();
		Stack stack = new Stack(currentThread);
				
		// Get stack frames
		IStackFrame[] frames = stack.getStackFrames();
		return frames;
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
	
}
