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

public class JsonBuilder {
	private JSONObject json;
	
	public JsonBuilder() {
		this.json = new JSONObject();
	}
	
	public JSONObject getJson(IStackFrame[] frames) throws DebugException {		
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
	
	private Map<String, Object> getHeapMap(Heap heap) throws DebugException {
		ArrayList<IVariable> vars = heap.getHeap();
		Map<String, Object> varsMap = new HashMap<String, Object>();
		ArrayList<Map<String, String>> varsList = Variable.getVarsList(vars);
		varsMap.put(Global.VARIABLES, varsList);	
		Map<String, Object> heapMap = new HashMap<String, Object>();
		heapMap.put(Global.HEAP, varsMap);
		return heapMap;		
	}
	
	private Map<String, Object> getStackMap(Stack stack) throws DebugException {
		IStackFrame[] frames = stack.getStackFrames();

		Map<String, Object> frameMap = new HashMap<String, Object>();
		for (int frameNum = 0; frameNum < frames.length; frameNum++) {
			String frameName = stack.getStackFrameName(frames[frameNum]);
			String frameCalssName = frames[frameNum].getVariables()[0].getReferenceTypeName().toString();
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
	
	private static String getDateTime() {
		Date date = new Date();
		SimpleDateFormat dateFormated = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss:SSS");
		String dateStr = dateFormated.format(date);
		return dateStr;		
	}

		
}
