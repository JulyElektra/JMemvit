package org.innopolis.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import mockit.Mock;

import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.jdt.debug.core.IJavaThread;
import org.innopolis.jmemvit.*;
import org.innopolis.jmemvit.mock.MockIStackFrame;
import org.json.JSONObject;
import org.junit.Test;

public class TestJsonBuilder {
	@Test
	public void test1() {
		JsonBuilder builder = new JsonBuilder();
		MockIStackFrame[] frames = {new MockIStackFrame(), new MockIStackFrame(), new MockIStackFrame(), new MockIStackFrame(),new MockIStackFrame(),new MockIStackFrame()};
		JSONObject json = builder.addInJson(frames);		
	}
}
