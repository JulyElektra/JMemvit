package org.innopolis.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.innopolis.jmemvit.data.StackFrame;
import org.innopolis.jmemvit.data.Variable;
import org.junit.Test;

public class TestStackFrame {
	
	@Test
	public void constructorAndGetters() {
		ArrayList<Variable> vars = new ArrayList<Variable>();
		String name = "main";
		int num = 1;
		StackFrame frame = new StackFrame(num, name , vars );
		assertTrue(frame.getName().equals(name));
		assertEquals(frame.getNumber(), num);
		assertTrue(frame.getVars().equals(vars));
	}
}
