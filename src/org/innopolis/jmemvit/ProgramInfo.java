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
	 * Method visualizes all elements in ProgramView
	 */
	public void vizualizateProgramInfoJava() throws DebugException{
			
		// Check if there is any updates
		if (hasEventUpdates()) {			
			for (TreeItem item : tree.getItems()){
				item.dispose();
			}			
			
			//TODO visualize all frames, not only TOP
			IStackFrame topFrame = getActualTopStackFrame();
			IStackFrame[] frames = getActualStackFrames();
			// Visualization
			ArrayList<String> stack = Stack.getStackFrameStrings(topFrame);
			visualize(stack);
			JsonBuilder jsonBuilder = new JsonBuilder();
			jsonBuilder.addStackToJson(frames).toString();			
			
			ArrayList<String> heap = Heap.getHeapStrings(frames);
			jsonBuilder.addHeapToJson(frames);
			visualize(heap);
			
			String jsonString = jsonBuilder.getJson().toString();
			MyFileWriter.write(jsonString);
		}
		else {
			// There is no updates or nothing
			// to update in visualization
		}
	}

	/*
	 * Method visualizes particular element
	 */
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
	
	/* 
	 * This method returns top stack frame of current thread
	 */
	private IStackFrame getActualTopStackFrame() {
		// Get current thread to extract data
		IJavaThread currentThread = jdiEventListener.getCurrentThread();
				
		// Get top stack frame
		IStackFrame topFrame = Stack.getTopStackFrame(currentThread);
		return topFrame;
	}
	
	/* 
	 * This method returns stack frames of current thread
	 */
	private IStackFrame[] getActualStackFrames() {
		// Get current thread to extract data
		IJavaThread currentThread = jdiEventListener.getCurrentThread();
				
		// Get stack frames
		IStackFrame[] frames = Stack.getStackFrames(currentThread);
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
