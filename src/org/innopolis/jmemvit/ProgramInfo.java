package org.innopolis.jmemvit;

import java.util.ArrayList;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.debug.core.IJavaThread;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;


public class ProgramInfo extends ViewPart {

	private DebugEventListener jdiEventListener = null;
	private Tree tree;
	
	class RunnableForThread2 implements Runnable{
		public void run() {
			while (true) {
				try { Thread.sleep(1000); } catch (Exception e) { }
				Runnable task = () -> { 
					try {
						vizualizateProgramInfoJava();
					} catch (DebugException e) {						
						e.printStackTrace();
					}
				};
				Display.getDefault().asyncExec(task);
			}			
		}
	}
	
	
	public ProgramInfo() {
	}

	@Override
	public void createPartControl(Composite parent) {
		tree = new Tree(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);		
		tree.setVisible(true);
		
		TreeColumn columnName = new TreeColumn(tree, SWT.LEFT);
		columnName.setText("");
		columnName.setWidth(300);
		
		jdiEventListener = new DebugEventListener();
		DebugPlugin.getDefault().addDebugEventListener(jdiEventListener);
		
		
		Runnable runnable = new RunnableForThread2();
		Thread thread2 = new Thread(runnable);
		thread2.start();	
	}
	

	@Override
	public void setFocus() {
	}

	/*
	 * Method visualizes information in ProgramView
	 */
	public void vizualizateProgramInfoJava() throws DebugException{
			
		// Check if there is any updates
		if (hasEventUpdates()) {
			for (TreeItem item : tree.getItems()){
				item.dispose();
			}
			
			
			// Visualization
			visualize(StackToString());
			visualize(HeapToString());			
		}
		else {
			// There is no updates or nothing
			// to update in visualization
		}
	}

	private void visualize(ArrayList<String> stringsToVisualize) {
		if (stringsToVisualize != null) {
			for (String stringToVisualize: stringsToVisualize){
				TreeItem item = new TreeItem(tree, SWT.LEFT);
				item.setText(0, stringToVisualize);
			}			
		}
		else {
			// String is null
			// Nothing to be visualized
		}
			
	}

	private ArrayList<String> HeapToString() {
		// TODO Auto-generated method stub
		// Get current thread to extract data
		IJavaThread currentThread = jdiEventListener.getCurrentThread();	
		return null;
	}

	private ArrayList<String> StackToString() throws DebugException {
		
		// Get current thread to extract data
		IJavaThread currentThread = jdiEventListener.getCurrentThread();
		
		// Get top stack frame
		IStackFrame topFrame = Stack.getTopStackFrame(currentThread);
		
		// If stack is not empty
		if (topFrame != null){
						
			// Get name of top stack item
			String frameName = Stack.getStackFrameName(topFrame);
			
			// Get line number of top stack item
			int lineNumber = Stack.getStackFrameLineNumber(topFrame);
			
			ArrayList<String> stackStrings = new ArrayList<>();
			
			// Add frame name and line number to the array
			stackStrings.add("ProgramCounter: " + frameName + " " + lineNumber);
			
			// Get Variables of top stack item
			IVariable[] vars = Stack.getStackFrameVariables(topFrame);
			ArrayList<String> varStrings = Stack.stackFrameVariablesToStrings(vars);
			
			// Add variables to the array
			for (String varString: varStrings){
				stackStrings.add(varString);
			}
			
			return stackStrings;
		}
		else {
			// Stack is empty
			return null;
		}
	}

	/* 
	 * Method checks if there was any changes in events. Result will 
	 * be used for deciding if it is needed to refresh view or not.
	 */
	private boolean hasEventUpdates() {
		// if events are exists and there is changers in event
		if(jdiEventListener != null && jdiEventListener.isItUpdatedThread()) {
			return true;
		}
		else {
			return false;
		}
		
	}
	
	
}
