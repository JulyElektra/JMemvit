package org.innopolis.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.innopolis.jmemvit.*;
import org.innopolis.jmemvit.data.Stack;
import org.innopolis.jmemvit.data.StackFrame;
import org.junit.Test;

public class TestStackStrings {
	
	@Test
	public void stackStringsCheckingNull() {
		Stack stack1 = new Stack(null);
		assertEquals(stack1.getStackFrames().size(),0);
		Stack stack2 = new Stack();
		assertEquals(stack2.getStackFrames().size(),0);
	}
	
	@Test
	public void stackStringsChecking() {
		ArrayList<StackFrame> list = new ArrayList<StackFrame>();
		Stack stack = new Stack(list);
		assertTrue(stack.getStackFrames().equals(list));
	}
}
