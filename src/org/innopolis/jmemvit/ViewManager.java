package org.innopolis.jmemvit;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.jdt.debug.core.IJavaThread;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;
import org.innopolis.jmemvit.data.State;
import org.innopolis.jmemvit.extractors.StackExtractor;
import org.innopolis.jmemvit.json.JsonBuilder;
import org.json.JSONObject;

/**
 * The ViewManager class controls graphical interface of the program
 */
public class ViewManager extends ViewPart {

	private DebugEventListener jdiEventListener;
	private Browser browser;
	private JsonStorage jsonStorage;

	
	/**
	 * The constructor
	 */
	public ViewManager() {
		jsonStorage = new JsonStorage();
	}
	
	/**
	 * The constructor for JUnit Testing
	 */
	public ViewManager(DebugEventListener listener, Browser browser) {
		this.jdiEventListener = listener;
		this.browser = browser;
	}

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
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		parent.setLayout(gridLayout);	
		
		Composite buttonsLayout = new Composite(parent, SWT.NONE);
		
		
		RowLayout rowLayout = new RowLayout();
	    rowLayout.wrap = false;
	    rowLayout.pack = true;
	    rowLayout.type = SWT.HORIZONTAL;
	    buttonsLayout.setLayout(rowLayout);
	    
	    Button backButton = new Button(buttonsLayout, SWT.PUSH | SWT.TOP);
	    backButton.setText("Back");
	    backButton.setEnabled(true);
		
	    Button forwardButton = new Button(buttonsLayout, SWT.PUSH | SWT.TOP);
	    forwardButton.setText("Forward");
	    forwardButton.setEnabled(true);

	    
	    browser = new Browser(parent, SWT.NONE);
	    GridData gridData = new GridData();
	    gridData.grabExcessHorizontalSpace = true;
	    gridData.horizontalSpan = 100;
	    gridData.verticalSpan = 100;
	    gridData.horizontalAlignment = GridData.FILL;
	    gridData.verticalAlignment = GridData.FILL;
	    
	    
	    browser.setLayoutData(gridData);
			    
		browser.setText("<html><body>Stack and heap will appear here.Please, start debugging.</body></html>");

		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
       
        
        IAction backward = ActionFactory.BACKWARD_HISTORY.create( window );
        backward.setId( IWorkbenchCommandConstants.NAVIGATE_BACKWARD_HISTORY );
        
        IAction forward = ActionFactory.FORWARD_HISTORY.create( window );
        forward.setId( IWorkbenchCommandConstants.NAVIGATE_FORWARD_HISTORY );


		backButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
		        jsonStorage.back();
		        visualizeCurrentState();
		        backward.run();	         
	        }
	      });
	    
		forwardButton.addListener(SWT.Selection, new Listener() {
	         public void handleEvent(Event event) {
	        	 jsonStorage.forward();       
	        	 visualizeCurrentState();
		         forward.run();
	         }     
	      });

		
		jdiEventListener = new DebugEventListener();
		DebugPlugin debugPlugin = DebugPlugin.getDefault();
		debugPlugin.addDebugEventListener(jdiEventListener);		
		
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
			
			JsonBuilder jsonBuilder = new JsonBuilder();
			// Writing data in JSON
			JSONObject json = jsonBuilder.getJson(frames);
			jsonStorage.addToJson(json);
			
			
			jsonStorage.setCurrentStateNumber(1);
			visualizeCurrentState();
						
//			// TODO delete in the final version
//			String jsonString = json.toString();
//			org.innopolis.jmemvit.temporal.MyFileWriter.write(jsonString);
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
		HtmlBuilder htmlBuilder = new HtmlBuilder();		
		return htmlBuilder.getHtmlString(state);
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
	/*private IStackFrame getActualTopStackFrame() throws DebugException {
		// Get current thread to extract data
		IJavaThread currentThread = jdiEventListener.getCurrentThread();
		Stack stack = new Stack(currentThread);
				
		// Get top stack frame
		IStackFrame topFrame = stack.getTopStackFrame();
		return topFrame;
	}*/
	
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
	
	private void visualizeCurrentState() {
		// Reading from JSON information about the current state
		State currentState = jsonStorage.getCurrentState();
		
		if (currentState != null){	
			
			// Building HTML format string with data
			String currentStateHTML = getStateHTML(currentState);
					
			// Visualization
			visualize(currentStateHTML);
		}	
	}
	
}
