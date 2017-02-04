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
			Variable var = getVariable(varJSObj);			
			varsList.add(var);
		}
		return varsList;		
	}
	
	/*
	 * This Method gets name, type and value from JSONObject 
	 * and creates variable with these data
	 */
	private Variable getVariable(JSONObject varJSObj) {
		String name = (String) varJSObj.get(Global.NAME);
		String type = (String) varJSObj.get(Global.TYPE);
		String value = (String) varJSObj.get(Global.VALUE);
		Variable var = new Variable(name,  type, value);
		return var;
	}

	/*
	 * Returns list with all states of the debugging program (time, heap, stack) 
	 * extracted from the JSON
	 */
	public ArrayList<State> read() throws ParseException {
		ArrayList<String> timeList = getSortedTimeList();
		for (String time: timeList) {
			State state = getState(time);
			listOfStates.add(state);
		}
		return listOfStates;
	}
	
	/*
	 * The method gets gets stack and heap data and returns a state
	 */
	private State getState(String time) {
		String jsonString = json.toString();
		JSONObject jObj = new JSONObject(jsonString);
		JSONArray jArr = (JSONArray)jObj.get(time);
		JSONObject heapAndStack = (JSONObject) jArr.get(0);
		StackStrings stack = getStack(time, heapAndStack);
		HeapStrings heap = getHeap(time, heapAndStack);
		State state = new State(time, stack, heap);
		return state;
	}

	/*
	 * This method returns a heap from JSON
	 */
	private HeapStrings getHeap(String time, JSONObject heapAndStack) {				
		ArrayList<Variable> heapVars = getVars ((JSONObject) heapAndStack, Global.HEAP);
		HeapStrings heap = new HeapStrings(heapVars);
		return heap;
	}

	/*
	 * This method returns a stack from JSON
	 */
	private StackStrings getStack(String time, JSONObject heapAndStack) {
		JSONObject stackJSObj = (JSONObject) heapAndStack.get(Global.STACK);
		ArrayList<StackFrame> stackFrames = getStackFrames(stackJSObj);
		StackStrings stack = new StackStrings(stackFrames);
		return stack;
	}

	/*
	 * This method returns stack frames from JSON
	 */
	private ArrayList<StackFrame> getStackFrames(JSONObject stackJSObj) {
		Map<Integer, String> stackFramesMap  = getStackFramesMap(stackJSObj);		
		ArrayList<StackFrame> stackFrames = new ArrayList<StackFrame>();
		for (Entry<Integer, String> entry: stackFramesMap.entrySet()) {
			int stackFrNumber = entry.getKey();
			String stackFrName = entry.getValue();
			String stackFrameString = stackFrNumber + " " + stackFrName;
			ArrayList<Variable> stackFrameVars = getVars ((JSONObject) stackJSObj, stackFrameString);			
			StackFrame stackFrame = new StackFrame(stackFrNumber, stackFrName, stackFrameVars);
			stackFrames.add(stackFrame);
		}
		return stackFrames;
	}

	/*
	 * This method gets Map with stack frame number and name from JSON
	 */
	private Map<Integer, String> getStackFramesMap(JSONObject stackJSObj) {
		Set<String> stackFramesStrings = stackJSObj.keySet();
		Map<Integer, String> stackFramesMap = new HashMap<Integer, String>();
		for (String fr: stackFramesStrings) {
			String[] s = fr.split(" ");
			stackFramesMap.put(Integer.parseInt(s[0]), s[1]);
		}
		return stackFramesMap;
	}

	/*
	 * The method gets sorted ArrayList of date and time
	 */
	private ArrayList<String> getSortedTimeList() throws ParseException {
		Set<String> times =  json.keySet();		
		ArrayList<String> timesList = new ArrayList<String>(times);
		ArrayList<String> sortedTimesList = sortByTime(timesList);	
		return sortedTimesList;
	}

	/*
	 * Sorting the ArrayList of Strings by date and time
	 */
	private ArrayList<String> sortByTime(ArrayList<String> stringsTimesList) throws ParseException {
		ArrayList<Date> dateTimeList = convertStringsToDates(stringsTimesList);
		Collections.sort(dateTimeList);
		ArrayList<String> resultList = convertDatesToStrings(dateTimeList);		
		return resultList;
	}

	/*
	 * Convert String date format into date format in ArrayLists
	 */
	private ArrayList<String> convertDatesToStrings(ArrayList<Date> datesList) {
		ArrayList<String> stringList = new ArrayList<String>();
		for (Date date: datesList) {
			String stringDate = getDateTime(date);
			stringList.add(stringDate);
		}
		return stringList;
	}

	/*
	 * Convert Date format into string date format in ArrayLists
	 */
	private ArrayList<Date> convertStringsToDates(ArrayList<String> stringList) throws ParseException {
		ArrayList<Date> datesList = new ArrayList<Date>();
		for (String stringDate: stringList) {
			Date date = getDateTime(stringDate);
			datesList.add(date);
		}
		return datesList;
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
