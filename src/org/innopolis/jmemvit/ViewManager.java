package org.innopolis.jmemvit;

import java.util.ArrayList;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.jdt.debug.core.IJavaThread;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;
import org.json.JSONObject;

/**
 * The ViewManager class controls graphical interface of the program
 */
public class ViewManager extends ViewPart {

	private DebugEventListener jdiEventListener;
	private Tree tree;
	private JsonBuilder jsonBuilder = new JsonBuilder();
	
	class RunnableForThread2 implements Runnable{
		public void run() {
			while (true) {
				try { Thread.sleep(1000); } catch (Exception e) { }
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
	 * Method visualizes all elements in view
	 */
	public void vizualizateView() throws DebugException{
			
		// Check if there is any updates
		if (hasEventUpdates()) {			
			for (TreeItem item : tree.getItems()){
				item.dispose();
			}			
			
			IStackFrame topFrame = getActualTopStackFrame();
			IStackFrame[] frames = getActualStackFrames();
			Stack stack = new Stack(frames);
			Heap heap = new Heap(stack);
			
			// Visualization
			//TODO visualize all frames, not only TOP
			ArrayList<String> stackStrings = stack.getStackFrameStrings(topFrame);
			visualize(stackStrings);
			
			ArrayList<String> heapStrings = heap.getHeapStrings();		
			visualize(heapStrings);
			
			JSONObject json = jsonBuilder.getJson(frames);
			String jsonString = json.toString();
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
