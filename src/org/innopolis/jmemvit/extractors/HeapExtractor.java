package org.innopolis.jmemvit.extractors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IVariable;

/**
 * The Heap class is used for deriving information from the StackFrames
 */
public class HeapExtractor {
	
	// The stack, which associates with this heap
	private StackExtractor stack;
	
	/**
	 * The constructor
	 */
	public HeapExtractor(StackExtractor stack) {
		this.stack = stack;
	}
	
	/*
	 * The method extracts all object types from the stack 
	 * and returns the list of all objects in the stack
	 */
	public ArrayList<IVariable> getHeap() {
		IStackFrame[] frames = stack.getStackFrames();
		ArrayList<IVariable> heapObjects = new ArrayList<>();
		for (IStackFrame frame: frames) {
			ArrayList<IVariable> frameObjects = findObjects(frame);
			heapObjects.addAll(frameObjects);			
		}
		ArrayList<IVariable> listNoDuplicates = removeDuplicates(heapObjects);
		return listNoDuplicates;
	}
	
	/*
	 * This method removes all duplicated objects from ArrayList. 
	 * It is used to remove duplicates in Heap
	 */	
	private ArrayList<IVariable> removeDuplicates(ArrayList<IVariable> listOfVariables){
		ArrayList<IVariable> listNoDuplicates = new ArrayList<IVariable>();
		listNoDuplicates = listOfVariables;
		Set<IVariable> hs = new HashSet<>();
		hs.addAll(listNoDuplicates);
		listNoDuplicates.clear();
		listNoDuplicates.addAll(hs);
		for (int j = 0; j < listNoDuplicates.size(); j++) {
			IVariable v = listNoDuplicates.get(j);
			try {
				//if (v.getName().toString() == "this") {
					for (int i = 0; i < listNoDuplicates.size(); i++) {
						IVariable vCompare = listNoDuplicates.get(i);
						if (!vCompare.equals(v)) {
							String vCompareValue = vCompare.getValue().toString();
							String vValue = v.getValue().toString();
							
							String vCompareType = vCompare.getReferenceTypeName();
							String vType = v.getReferenceTypeName();
							
							if (vCompareValue.equals(vValue) && vCompareType.equals(vType)) {
								listNoDuplicates.remove(v);
							}
						}
					}
				//}
			} catch (DebugException e) {
				e.printStackTrace();
			}
		}
		return listNoDuplicates;
	}

	/*
	 * This method finds all objects in the stack frame
	 */
	private ArrayList<IVariable> findObjects(IStackFrame frame)  {
		IVariable[] vars = stack.getStackFrameVariables(frame);
		ArrayList<IVariable> objects = new ArrayList<>();
		for (IVariable var: vars) {
			if (VariableExtractor.isObjectType(var)) {
				objects.add(var);
			}
			else {
				// Variable is primitive type
			}
		}
		return objects;
	}

}
