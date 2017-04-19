package org.innopolis.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.eclipse.debug.core.model.IStackFrame;
import org.innopolis.jmemvit.model.StackFrame;
import org.innopolis.jmemvit.model.Variable;
import org.innopolis.tests.mock.MockIStackFrame;
import org.junit.Test;

public class TestStackFrame {
	
	@Test
	public void constructorAndGetters() {
		ArrayList<Variable> vars = new ArrayList<Variable>();
		String name = "main";
		int num = 1;
		StackFrame frame = new StackFrame(num, name , vars );
		int frameNum = 0;
//		IStackFrame iframe = new MockIStackFrame();
//		StackFrame.getMethodClassName(iframe , frameNum );
		assertTrue(frame.getName().equals(name));
		assertEquals(frame.getNumber(), num);
		assertTrue(frame.getVars().equals(vars));
	}
}
