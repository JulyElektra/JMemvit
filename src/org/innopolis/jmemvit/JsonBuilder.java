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
	
	public JSONObject getJson() {
		return json;
	}
	
	public JSONObject addHeapToJson(IStackFrame[] frames) throws DebugException {
		String date = getDateTime();	
		ArrayList<IVariable> vars = Heap.getHeap(frames);
		Map<String, Object> varsMap = new HashMap<String, Object>();
		ArrayList<Map<String, String>> varsList = getVarsList(vars);
		varsMap.put(Global.VARIABLES, varsList);	
		Map<String, Object> heapMap = new HashMap<String, Object>();
		heapMap.put(Global.HEAP, varsMap);
		json.put(date, heapMap);
		return json;		
	}
	
	private ArrayList<Map<String, String>> getVarsList (ArrayList<IVariable> vars) throws DebugException {
		
		ArrayList<Map<String, String>> varsList = new ArrayList<Map<String, String>>();
		for (IVariable var: vars) {
			Map<String, String> varMap = new HashMap<String, String>();
			String varName = var.getName().toString();
			varMap.put(Global.NAME, varName);
			String varValue = var.getValue().toString();
			varMap.put(Global.VALUE, varValue);
			String varType = var.getReferenceTypeName();
			varMap.put(Global.TYPE, varType);				
			varsList.add(varMap);
		}
		return varsList;
		
	}
	
	public JSONObject addStackToJson(IStackFrame[] frames) throws DebugException {
		String date = getDateTime();	
		Map<String, Object> frameMap = new HashMap<String, Object>();
		for (int frameNum = 0; frameNum < frames.length; frameNum++) {
			String frameName = Stack.getStackFrameName(frames[frameNum]);
			String frameCalssName = frames[frameNum].getVariables()[0].getReferenceTypeName().toString();//
			IVariable[] vars = Stack.getStackFrameVariables(frames[frameNum]);
			ArrayList<IVariable> varsArrList = new ArrayList<IVariable>(Arrays.asList(vars));
			Map<String, Object> varsMap = new HashMap<String, Object>();
			ArrayList<Map<String, String>> varsList = getVarsList(varsArrList);
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
