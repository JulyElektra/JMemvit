package org.innopolis.jmemvit;

import java.util.ArrayList;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IVariable;

/**
 * The Heap class is used for deriving information from the StackFrames
 */
public class Heap {
	
	// The stack, which associates with this heap
	private Stack stack;
	
	/**
	 * The constructor
	 */
	public Heap(Stack stack) {
		this.stack = stack;
	}
	
	/*
	 * The method extracts all object types from the stack 
	 * and returns the list of all objects in the stack
	 */
	public ArrayList<IVariable> getHeap() throws DebugException {
		IStackFrame[] frames = stack.getStackFrames();
		ArrayList<IVariable> heapObjects = new ArrayList<>();
		for (IStackFrame frame: frames) {
			ArrayList<IVariable> frameObjects = findObjects(frame);
			heapObjects.addAll(frameObjects);
		}
		return heapObjects;
	}
	
	/*
	 * This method method returns all objects of the heap into array of strings
	 */
	public ArrayList<String> getHeapStrings() throws DebugException {
		ArrayList<IVariable> heap = getHeap();
		ArrayList<String> heapStrings = Variable.toStrings(heap.toArray());
		if (heapStrings != null && heapStrings.size() != 0) {
			heapStrings.add(0, Global.HEAP);				
		}
		return heapStrings;
	}
	
	/*
	 * This method finds all objects in the stack frame
	 */
	private ArrayList<IVariable> findObjects(IStackFrame frame) throws DebugException {
		IVariable[] vars = stack.getStackFrameVariables(frame);
		ArrayList<IVariable> objects = new ArrayList<>();
		for (IVariable var: vars) {
			if (Variable.isObjectType(var)) {
				objects.add(var);
			}
			else {
				// Variable is primitive type
			}
		}
		return objects;
	}

}
