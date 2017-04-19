package org.innopolis.jmemvit.view;

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
import org.innopolis.jmemvit.controller.DebugEventListener;
import org.innopolis.jmemvit.controller.Manager;
import org.innopolis.jmemvit.model.Storage;
import org.innopolis.jmemvit.model.State;

/**
 * The View class visualizes graphical interface of the program
 */
public class View extends ViewPart {

	private Browser browser;
	private Storage jsonStorage;

	
	/**
	 * The constructor
	 */
	public View() {
		jsonStorage = new Storage();
	}
	
	/**
	 * The constructor for JUnit Testing
	 */
	public View(DebugEventListener listener, Browser browser) {
//		this.jdiEventListener = listener;
		this.browser = browser;
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

		
		DebugEventListener jdiEventListener = new DebugEventListener();
		Manager manager = new Manager(jsonStorage, this, jdiEventListener);
		manager.manage();
	}
	



	@Override
	public void setFocus() {
	}

	/*
	 * Method visualizes all elements in view
	 */

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
	

	
	public void visualizeCurrentState() {
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
