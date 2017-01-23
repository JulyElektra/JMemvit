package org.innopolis.jmemvit;

import java.util.ArrayList;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IVariable;


public class Heap {
	
	private Stack stack;
	
	public Heap(Stack stack) {
		this.stack = stack;
	}
	
	public ArrayList<IVariable> getHeap() throws DebugException {
		IStackFrame[] frames = stack.getStackFrames();
		ArrayList<IVariable> heapObjects = new ArrayList<>();
		for (IStackFrame frame: frames) {
			ArrayList<IVariable> frameObjects = findObjects(frame);
			heapObjects.addAll(frameObjects);
		}
		return heapObjects;
	}
	
	public ArrayList<String> getHeapStrings() throws DebugException {
		ArrayList<IVariable> heap = getHeap();
		ArrayList<String> heapStrings = Variable.toStrings(heap.toArray());
		if (heapStrings != null && heapStrings.size() != 0) {
			heapStrings.add(0, Global.HEAP);				
		}
		return heapStrings;
	}
	
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
