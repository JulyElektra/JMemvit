package org.innopolis.jmemvit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.debug.core.DebugException;
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
		String date = getDateTime();
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
		Map<String, Object> varsMap = new HashMap<String, Object>();
		ArrayList<Map<String, String>> varsList = Variable.getVarsList(vars);
		varsMap.put(Global.VARIABLES, varsList);	
		Map<String, Object> heapMap = new HashMap<String, Object>();
		heapMap.put(Global.HEAP, varsMap);
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
			try {
				frameCalssName = frames[frameNum].getVariables()[0].getReferenceTypeName().toString();
			} catch (DebugException e) {
				e.printStackTrace();
			}
			IVariable[] vars = stack.getStackFrameVariables(frames[frameNum]);
			ArrayList<IVariable> varsArrList = new ArrayList<IVariable>(Arrays.asList(vars));
			Map<String, Object> varsMap = new HashMap<String, Object>();
			ArrayList<Map<String, String>> varsList = Variable.getVarsList(varsArrList);
			varsMap.put(Global.VARIABLES, varsList);			
			frameMap.put((frameNum + " " + frameCalssName + "." + frameName).toString(), varsMap);
		}
		Map<String, Object> stackMap = new HashMap<String, Object>();
		stackMap.put(Global.STACK, frameMap);
		return stackMap;		
	}
	
	/*
	 * Returns data string format of current time and date
	 */
	private static String getDateTime() {
		Date date = new Date();
		SimpleDateFormat dateFormated = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss:SSS");
		String dateStr = dateFormated.format(date);
		return dateStr;		
	}

		
}
