package org.innopolis.jmemvit;

import static org.innopolis.jmemvit.Global.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
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
	 * Returns list with all states of the debugging program (time, heap, stack) 
	 * extracted from the JSON
	 */
	public ArrayList<State> read() {
		ArrayList<String> timeList = getSortedTimeList();
		for (String time: timeList) {
			State state = getState(time);
			if (state != null) {
				listOfStates.add(state);
			}
		}
		setJustInitializedVars();
		return listOfStates;
	}
	
	private void setJustInitializedVars() {
		for (int stateNum = 0; stateNum < listOfStates.size(); stateNum ++) {
			State stateCurrent = listOfStates.get(stateNum);
			ArrayList<Variable> variablesJustInitialized = new ArrayList<Variable>();
			if (stateNum == 0) {
				variablesJustInitialized = getAllVars(stateCurrent);
			} else {
				State statePrivious = listOfStates.get(stateNum - 1);
				variablesJustInitialized =  getVariablesJustInitialized(statePrivious, stateCurrent);
			}
			setVarsJustInitialized(variablesJustInitialized);
		}
	}

	private void setVarsJustInitialized(
		ArrayList<Variable> vars) {
		for (Variable var: vars) {
			var.setHasJustInitialized(true);
		}
		
	}

	private ArrayList<Variable> getVariablesJustInitialized(
		State statePrivious, State stateCurrent) {
		ArrayList<Variable> variablesJustInitialized = new ArrayList<Variable>();
		variablesJustInitialized.addAll(getVarsJustInitialized(
				getHeapVars(stateCurrent), getHeapVars(statePrivious)));
		variablesJustInitialized.addAll(getStackVarsJustInitialized(
				statePrivious.getStack().getStackFrames(),
				stateCurrent.getStack().getStackFrames()));
		return variablesJustInitialized;
	}

	private ArrayList<Variable> getStackVarsJustInitialized(
			ArrayList<StackFrame> framesPrivious,
			ArrayList<StackFrame> framesCurrent) {
		ArrayList<Variable> stackVarsJustInitialized = new ArrayList<Variable>();
		for (StackFrame frameCurrent: framesCurrent) {
			if (!StackFrame.containsName(frameCurrent, framesPrivious)) {
				stackVarsJustInitialized.addAll(frameCurrent.getVars());
			} else {			
				for (StackFrame framePrivious: framesPrivious) {
					if (frameCurrent.getName().equals(framePrivious.getName())) {
						stackVarsJustInitialized.addAll(getVarsJustInitialized(
								frameCurrent.getVars(), framePrivious.getVars()));
					}
				}
			}
		}
		return stackVarsJustInitialized;
	}

	private ArrayList<Variable>  getVarsJustInitialized(ArrayList<Variable> currentVars,
				ArrayList<Variable> priviousVars) {
		
		ArrayList<Variable> varsJustInitialized = new ArrayList<Variable>();
		for (Variable currentVar: currentVars) {
			boolean has = false;
			for (Variable priviousVar: priviousVars){
				if (currentVar.equalsNameAndType(priviousVar)) {
					has = true;
				}
			}
			if (!has) {
				varsJustInitialized.add(currentVar);
			}
		}		
		return varsJustInitialized;
	}

	private ArrayList<Variable> getAllVars(State state) {
		ArrayList<Variable> vars = new ArrayList<Variable>();		
		vars.addAll(getHeapVars(state));
		vars.addAll(getStackVars(state));
		return vars;
	}

	private ArrayList<Variable> getStackVars(State state) {
		ArrayList<Variable> vars = new ArrayList<Variable>();
		ArrayList<StackFrame> frames = state.getStack().getStackFrames();
		for (StackFrame frame: frames) {
			vars.addAll(frame.getVars());
		}
		return vars;
	}

	private ArrayList<Variable> getHeapVars(State state) {		
		return state.getHeap().getVariables();
	}

	/*
	 * Returns list of variables (their name, type, value) from JSONObject with the key
	 */
	private ArrayList<Variable> getVars (JSONObject jsonObj, String key) {
		ArrayList<Variable> varsList = new ArrayList<Variable>();
		JSONArray vars = (JSONArray)((JSONObject) jsonObj.get(key)).get(VARIABLES);
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
		String name = (String) varJSObj.get(KEY + NAME.toUpperCase());
		String type = (String) varJSObj.get(KEY + TYPE.toUpperCase());
		String value = (String) varJSObj.get(KEY + VALUE.toUpperCase());
		String fields = (String) varJSObj.get(KEY + FIELDS.toUpperCase());
		String hasValueChanged  = (String) varJSObj.get(KEY + HAS_VALUE_CHANGED.toUpperCase());
		Variable var = new Variable(name,  type, value, fields, hasValueChanged);
		return var;
	}


	/*
	 * The method gets gets stack and heap data and returns a state
	 */
	private State getState(String time) {
		String jsonString = json.toString();
		JSONObject jObj = new JSONObject(jsonString);
		try {
			JSONArray jArr = (JSONArray)jObj.get(time);
			JSONObject heapAndStack = (JSONObject) jArr.get(0);
			StackFrameStrings stack = getStack(time, heapAndStack);
			HeapStrings heap = getHeap(time, heapAndStack);
			State state = new State(time, stack, heap);	
			return state;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * This method returns a heap from JSON
	 */
	private HeapStrings getHeap(String time, JSONObject heapAndStack) {				
		ArrayList<Variable> heapVars = getVars ((JSONObject) heapAndStack, HEAP);
		HeapStrings heap = new HeapStrings(heapVars);
		return heap;
	}

	/*
	 * This method returns a stack from JSON
	 */
	private StackFrameStrings getStack(String time, JSONObject heapAndStack) {
		JSONObject stackJSObj = (JSONObject) heapAndStack.get(STACK);
		ArrayList<StackFrame> stackFrames = getStackFrames(stackJSObj);
		StackFrameStrings stack = new StackFrameStrings(stackFrames);
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
	private ArrayList<String> getSortedTimeList() {
		Set<String> times =  json.keySet();		
		ArrayList<String> timesList = new ArrayList<String>(times);
		ArrayList<String> sortedTimesList = DateTime.sortByTime(timesList);	
		return sortedTimesList;
	}

}
