package org.innopolis.tests;


import org.innopolis.jmemvit.*;
import org.innopolis.jmemvit.json.JsonBuilder;
import org.innopolis.jmemvit.mock.MockIStackFrame;
import org.json.JSONObject;
import org.junit.Test;

public class TestJsonBuilder {
	@Test
	public void test1() {
		JsonBuilder builder = new JsonBuilder();
		MockIStackFrame[] frames = {new MockIStackFrame(), new MockIStackFrame(), new MockIStackFrame(), new MockIStackFrame(),new MockIStackFrame(),new MockIStackFrame()};
		JSONObject json = builder.getJson(frames);		
	}
}
