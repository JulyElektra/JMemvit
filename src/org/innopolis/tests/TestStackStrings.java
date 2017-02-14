package org.innopolis.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.innopolis.jmemvit.*;
import org.junit.Test;

public class TestStackStrings {
	
	@Test
	public void stackStringsCheckingNull() {
		StackStrings stack1 = new StackStrings(null);
		assertEquals(stack1.getStackFrames().size(),0);
		StackStrings stack2 = new StackStrings();
		assertEquals(stack2.getStackFrames().size(),0);
	}
	
	@Test
	public void stackStringsChecking() {
		ArrayList<StackFrame> list = new ArrayList<StackFrame>();
		StackStrings stack = new StackStrings(list);
		assertTrue(stack.getStackFrames().equals(list));
	}
}
