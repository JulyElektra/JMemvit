package org.innopolis.jmemvit;

import static org.innopolis.jmemvit.Global.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IVariable;
import org.json.*;


/**
 * The JsonBuilder class is used for creating JSON format of Stack and Heap
 */
public class JsonBuilder {
	
	private JSONObject json;
	
	/**
	 * The constructor
	 */
	public JsonBuilder() {
		this.json = new JSONObject();
	}
	
	
	public JSONObject getJson() {
		return json;
	}


	/*
	 * This method adds information about stack and heap at specific time
	 * and returns updated JSON
	 */
	public JSONObject addInJson(IStackFrame[] frames){		
		Stack stack = new Stack(frames) ;
		Heap heap = new Heap(stack);
		String date = DateTime.getCurrentDateTime();
		Map<String, Object> heapAndStack = new HashMap<String, Object>();
		Map<String, Object> stackMap = getStackMap(stack);
		Map<String, Object> heapMap = getHeapMap(heap);
		heapAndStack.putAll(stackMap);
		heapAndStack.putAll(heapMap);		
		json.append(date, heapAndStack);
		return json;
	}
	
	/*
	 * Returns Map list with heap information: all objects in the heap
	 */
	private Map<String, Object> getHeapMap(Heap heap) {
		ArrayList<IVariable> vars = heap.getHeap();
		ArrayList<IVariable> fields = Variable.getFields(vars);
		ArrayList<IVariable> fieldsObjects = Variable.getObjectsFromVars(fields);
		vars.addAll(fieldsObjects);
		Map<String, Object> varsMap = new HashMap<String, Object>();
		ArrayList<Map<String, String>> varsList = Variable.getVarsList(vars);		
		varsMap.put(VARIABLES, varsList);	
		Map<String, Object> heapMap = new HashMap<String, Object>();
		heapMap.put(HEAP, varsMap);
		return heapMap;		
	}
	
	/*
	 * Returns Map list with stack information: all stack frames, 
	 * stack frame number and variables at each stack frame
	 */
	private Map<String, Object> getStackMap(Stack stack) {
		IStackFrame[] frames = stack.getStackFrames();
		

		Map<String, Object> frameMap = new HashMap<String, Object>();
		for (int frameNum = 0; frameNum < frames.length; frameNum++) {
			String frameName = stack.getStackFrameName(frames[frameNum]);
			String frameCalssName = "";
//			try {
				
//				IVariable[] vars = frames[frameNum].getVariables();
//				frameCalssName = vars[0].getReferenceTypeName().toString();
				
				frameCalssName = StackFrame.getMethodClassName(frames[frameNum], frameNum);
				
//				frames[frameNum]
				//frameCalssName = 
				
//				String f = frames[frameNum].getClass().getName();
//				Location ct = new Location();
//				Method m = 	ClassType.concreteMethodByName("", "");
//				
				
//				System.out.println(f);
				
//				Thread.sleep(1);
//				IThread thread = frames[frameNum].getThread();
//				StackTraceElement[] stackTraces = thread.getStackTrace();
//				frameCalssName =  [frameNum].getClassName();
//			} catch (DebugException e) {
//				e.printStackTrace();
//			}
			IVariable[] vars = stack.getStackFrameVariables(frames[frameNum]);
			ArrayList<IVariable> varsArrList = new ArrayList<IVariable>(Arrays.asList(vars));
			Map<String, Object> varsMap = new HashMap<String, Object>();
			ArrayList<Map<String, String>> varsList = Variable.getVarsList(varsArrList);
			varsMap.put(VARIABLES, varsList);			
			frameMap.put((frameNum + " " + frameCalssName + "." + frameName  + "(...)").toString(), varsMap);
		}
		Map<String, Object> stackMap = new HashMap<String, Object>();
		stackMap.put(STACK, frameMap);
		return stackMap;		
	}
	


		
}
