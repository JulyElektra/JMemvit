package org.innopolis.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.innopolis.jmemvit.*;
import org.junit.Test;

public class TestStackStrings {
	
	@Test
	public void stackStringsCheckingNull() {
		StackFrameStrings stack1 = new StackFrameStrings(null);
		assertEquals(stack1.getStackFrames().size(),0);
		StackFrameStrings stack2 = new StackFrameStrings();
		assertEquals(stack2.getStackFrames().size(),0);
	}
	
	@Test
	public void stackStringsChecking() {
		ArrayList<StackFrame> list = new ArrayList<StackFrame>();
		StackFrameStrings stack = new StackFrameStrings(list);
		assertTrue(stack.getStackFrames().equals(list));
	}
}
