package org.innopolis.jmemvit;

import java.util.ArrayList;


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
import org.innopolis.jmemvit.temporal.MyFileWriter;
import org.json.JSONObject;

/**
 * The ViewManager class controls graphical interface of the program
 */
public class ViewManager extends ViewPart {

	private DebugEventListener jdiEventListener;
	private Browser browser;
	private JsonBuilder jsonBuilder;
	private State currentState;
	private int currentStateNumber;

	
	/**
	 * The constructor
	 */
	public ViewManager() {
		this.jsonBuilder = new JsonBuilder();
		this.currentStateNumber = 1;
	}
	
	/**
	 * The constructor for JUnit Testing
	 */
	public ViewManager(DebugEventListener listener, Browser browser) {
		this.jdiEventListener = listener;
		this.jsonBuilder = new JsonBuilder();
		this.browser = browser;
		this.currentStateNumber = 1;
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
//        NavigationHistoryAction a = new NavigationHistoryAction(window , true);
//
//        
//        IWorkbenchPage page = a.getActivePage();
        //IEditorPart editor = (IEditorPart) page.getNavigationHistory();
        //( input, MyEditor.ID );
        //page.getNavigationHistory().markLocation( editor );
        
//        IEditorPart editor = window.getActivePage().getActiveEditor();
//        INavigationHistory nh = window.getActivePage().getNavigationHistory();
//        nh.markLocation(editor);
//        INavigationLocation location =  nh.getCurrentLocation();
//        locations.add(location);
//        currentLocationID = locations.size() - 1;
        
        
        IAction backward = ActionFactory.BACKWARD_HISTORY.create( window );
        backward.setId( IWorkbenchCommandConstants.NAVIGATE_BACKWARD_HISTORY );
        
        IAction forward = ActionFactory.FORWARD_HISTORY.create( window );
        forward.setId( IWorkbenchCommandConstants.NAVIGATE_FORWARD_HISTORY );


        //IToolBarManager navigation = new ToolBarManager( SWT.FLAT );
        //navigation.add(backward);
        //navigation.add(forward);

		backButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
		        back();	
//		        int priviousLocation = currentLocationID - 1;
//		        int firstLocation = 0;
//		        if (priviousLocation < firstLocation) {
//		        	 priviousLocation = firstLocation;
//		        }
//		        locations.get(priviousLocation).restoreLocation();
		        
		        backward.run();	         
	        }
	      });
	    
		forwardButton.addListener(SWT.Selection, new Listener() {
	         public void handleEvent(Event event) {
		         forward();
//		         int nextLocation = currentLocationID + 1;
//		         int lastLocation = locations.size() - 1;
//		         if (nextLocation > lastLocation) {
//		        	 nextLocation = lastLocation;
//		         }
//		         locations.get(nextLocation).restoreLocation();
		         
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
	

	protected void forward() {
		if (currentStateNumber > 1) {
			currentStateNumber--;
		}
		visualizeCurrentState(jsonBuilder.getJson());
	}

	protected void back() {
		currentStateNumber++;
		visualizeCurrentState(jsonBuilder.getJson());
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
			JSONObject json = jsonBuilder.addInJson(frames);
			
			currentStateNumber = 1;
			visualizeCurrentState(json);
						
//			// TODO delete in the final version
//			String jsonString = json.toString();
//			MyFileWriter.write(jsonString);
		}
		else {
			// There is no updates or nothing
			// to update in visualization
		}
	}

	private void visualizeCurrentState(JSONObject json) {
		// Reading from JSON information about the current state
		currentState = getCurrentState(json);
		
		if (currentState != null){	
			
			// Building HTML format string with data
			String currentStateHTML = getStateHTML(currentState);
					
			// Visualization
			visualize(currentStateHTML);
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
		ArrayList<State> states = jsonReader.read();	
		if (states.isEmpty()) {
			return null;
		}
		int current = states.size() - currentStateNumber;
		if (current < 0) {
			currentStateNumber--;
			current++;
		}
		State currentState = states.get(current);
		return currentState;	
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
