package org.innopolis.jmemvit;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonStorage {
	
	private State currentState;
	private int currentStateNumber;
	private JSONObject json;

	public JsonStorage() {
		this.json = new JSONObject();	
	}
		

	public int getCurrentStateNumber() {
		return currentStateNumber;
	}


	public void setCurrentStateNumber(int currentStateNumber) {
		this.currentStateNumber = currentStateNumber;
	}


	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}


	public void setJson(JSONObject json) {
		this.json = json;
	}


	public JSONObject getJson() {
		return json;
	}

	public void addToJson(JSONObject jsonToAdd) {
		json = mergeJSONObjects(json, jsonToAdd);
	}
	
	private static JSONObject mergeJSONObjects(JSONObject json1, JSONObject json2) {
		JSONObject mergedJSON = new JSONObject();
		try {
			String[] j = JSONObject.getNames(json1);
			if (j == null) {
				return json2;
			}
			mergedJSON = new JSONObject(json1, j);
			for (String crunchifyKey : JSONObject.getNames(json2)) {
				mergedJSON.put(crunchifyKey, json2.get(crunchifyKey));
			}
 
		} catch (JSONException e) {
			throw new RuntimeException("JSON Exception" + e);
		}
		return mergedJSON;
	}
	
	public State forward() {
		if (currentStateNumber > 1) {
			currentStateNumber--;
		}
		return currentState; 
	}

	public State back() {
		currentStateNumber++;
		return currentState; 
	}


	
	/*
	 * Method gets the last (current) state of stack and heap from JSON
	 */
	public State getCurrentState() {
		JsonReader jsonReader = new JsonReader(json);
		ArrayList<State> states = jsonReader.read();	
		if (states.isEmpty()) {
			return null;
		}
		int current = states.size() - currentStateNumber;
		if (current < 0) {
			currentStateNumber--;
			current++;
		}
		State currentState = states.get(current);
		return currentState;	
	}
	
}
