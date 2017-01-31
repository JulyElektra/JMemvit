package org.innopolis.jmemvit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * The JsonReader class is used for extracting the information from JSON in list of states
 */
public class JsonReader {
	
	private ArrayList<State> listOfStates;
	private JSONObject json;

	/**
	 * The constructor
	 */
	public JsonReader(JSONObject json) {
		this.json = json;
		this.listOfStates = new ArrayList<State>();
	}
	
	/*
	 * Returns list of variables (their name, type, value) from JSONObject with the key
	 */
	private ArrayList<Variable> getVars (JSONObject jsonObj, String key) {
		ArrayList<Variable> varsList = new ArrayList<Variable>();
		JSONArray vars = (JSONArray)((JSONObject) jsonObj.get(key)).get(Global.VARIABLES);
		for (Object varObj: vars){
			JSONObject varJSObj = (JSONObject) varObj;
			String name = (String) varJSObj.get(Global.NAME);
			String type = (String) varJSObj.get(Global.TYPE);
			String value = (String) varJSObj.get(Global.VALUE);
			Variable var = new Variable(name,  type, value);
			varsList.add(var);
			return varsList;
		}
		return null;		
	}
	
	/*
	 * Returns list with all states of the debugging program (time, heap, stack) 
	 * extracted from the JSON
	 */
	public ArrayList<State> read() throws ParseException {
		
		Set<String> times =  json.keySet();		
		ArrayList<String> timesStringList = new ArrayList<String>(times);
		ArrayList<Date> timeList = new ArrayList<Date>();
		for (String time: timesStringList) {
			Date dateTime = getDateTime(time);
			timeList.add(dateTime);
		}
		Collections.sort(timeList);
		for (Date date: timeList) {
			String dateString = getDateTime(date);
			JSONObject heapAndStack = (JSONObject)((JSONArray) json.get(dateString)).get(0);

			
			ArrayList<Variable> heapVars = getVars ((JSONObject) heapAndStack, Global.HEAP);

			
			JSONObject stackJSObj = (JSONObject) heapAndStack.get(Global.STACK);
			Set<String> stackFramesStrings = stackJSObj.keySet();
			Map<Integer, String> stackFramesMap = new HashMap<Integer, String>();
			for (String fr: stackFramesStrings) {
				String[] s = fr.split(" ");
				stackFramesMap.put(Integer.parseInt(s[0]), s[1]);
			}
			ArrayList<StackFrame> stackFrames = new ArrayList<StackFrame>();
			for (Entry<Integer, String> entry: stackFramesMap.entrySet()) {
				int stackFrNumber = entry.getKey();
				String stackFrName = entry.getValue();
				String stackFrameString = stackFrNumber + " " + stackFrName;
				ArrayList<Variable> stackFrameVars = getVars ((JSONObject) stackJSObj, stackFrameString);
				StackFrame stackFrame = new StackFrame(stackFrNumber, stackFrName, stackFrameVars);
				stackFrames.add(stackFrame);
			}
			StackStrings stack = new StackStrings(stackFrames);
			HeapStrings heap = new HeapStrings(heapVars);
			State state = new State(dateString, stack, heap);
			listOfStates.add(state);		
		}
		
		return listOfStates;
	}
	
	/*
	 * Convert String date format into Date format
	 */
	private static Date getDateTime(String time) throws ParseException {
		SimpleDateFormat dateFormated = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss:SSS");
		Date date = dateFormated.parse(time);
		return date;		
	}
	
	/*
	 * Convert Date format into string date format
	 */
	private static String getDateTime(Date date) {
		SimpleDateFormat dateFormated = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss:SSS");
		String dateStr = dateFormated.format(date);
		return dateStr;		
	}
	
}
