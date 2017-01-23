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
	private JSONObject json = new JSONObject();
	private Stack stack;
	private Heap heap;	
	
	public JsonBuilder(IStackFrame[] frames) {
		this.stack = new Stack(frames) ;
		this.heap = new Heap(stack);
	}

	public JSONObject getJson() {
		return json;
	}
	
	public JSONObject addHeapToJson() throws DebugException {
		String date = getDateTime();
		ArrayList<IVariable> vars = heap.getHeap();
		Map<String, Object> varsMap = new HashMap<String, Object>();
		ArrayList<Map<String, String>> varsList = Variable.getVarsList(vars);
		varsMap.put(Global.VARIABLES, varsList);	
		Map<String, Object> heapMap = new HashMap<String, Object>();
		heapMap.put(Global.HEAP, varsMap);
		json.put(date, heapMap);
		return json;		
	}
	
	public JSONObject addStackToJson() throws DebugException {
		IStackFrame[] frames = stack.getStackFrames();
		String date = getDateTime();	
		Map<String, Object> frameMap = new HashMap<String, Object>();
		for (int frameNum = 0; frameNum < frames.length; frameNum++) {
			String frameName = stack.getStackFrameName(frames[frameNum]);
			String frameCalssName = frames[frameNum].getVariables()[0].getReferenceTypeName().toString();//
			IVariable[] vars = stack.getStackFrameVariables(frames[frameNum]);
			ArrayList<IVariable> varsArrList = new ArrayList<IVariable>(Arrays.asList(vars));
			Map<String, Object> varsMap = new HashMap<String, Object>();
			ArrayList<Map<String, String>> varsList = Variable.getVarsList(varsArrList);
			varsMap.put(Global.VARIABLES, varsList);			
			frameMap.put((frameNum + " " + frameCalssName + "." + frameName).toString(), varsMap);
		}
		Map<String, Object> stackMap = new HashMap<String, Object>();
		stackMap.put(Global.STACK, frameMap);
		json.put(date, stackMap);
		return json;		
	}
	
	private static String getDateTime() {
		Date date = new Date();
		SimpleDateFormat dateFormated = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss:SSS");
		String dateStr = dateFormated.format(date);
		return dateStr;		
	}
		
}
