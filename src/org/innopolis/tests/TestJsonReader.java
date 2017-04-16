package org.innopolis.tests;


import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import org.innopolis.jmemvit.*;
import org.innopolis.jmemvit.data.State;
import org.innopolis.jmemvit.json.JsonReader;
import org.json.JSONObject;
import org.junit.Test;


public class TestJsonReader {
	File file = new File("C:/WorkSpaces/Eclipse luna/luna projects/JMemvit/src/org/innopolis/jmemvit/mock/MockJsonFile");
	
	@Test
	public void testWrongDataFormat() throws Exception {
		Scanner scn = new Scanner(file);
		String s = "";
		while (scn.hasNext()) {
			s = s + scn.next();
		}
		JSONObject jObj = new JSONObject(s);
		JsonReader reader = new JsonReader(jObj);
		ArrayList<State> states = reader.read();		
	}
	
	@Test
	public void testRightDataFormat() throws Exception {
		
		Scanner scn = new Scanner(file);
		String s = "";
		while (scn.hasNext()) {
			s = s + scn.nextLine();
		}
		JSONObject jObj = new JSONObject(s);
		JsonReader reader = new JsonReader(jObj);
		ArrayList<State> states = reader.read();		
	}
}
