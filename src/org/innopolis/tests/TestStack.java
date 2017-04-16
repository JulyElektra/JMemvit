package org.innopolis.tests;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IStackFrame;
import org.innopolis.jmemvit.*;
import org.innopolis.jmemvit.extractors.StackExtractor;
import org.innopolis.tests.mock.MockIJavaThread;
import org.junit.Test;

public class TestStack {
	
	@Test
	public void testInit() throws DebugException {
		MockIJavaThread thread = new MockIJavaThread();
		StackExtractor stack = new StackExtractor (thread);
		IStackFrame frame = stack.getTopStackFrame();
		stack.getStackFrames();
		stack.getStackFrameLineNumber(frame);
	}
	
	@Test
	public void testThreadNull() throws DebugException {
		MockIJavaThread thread = null;
		StackExtractor stack = new StackExtractor (thread);
		IStackFrame frame = stack.getTopStackFrame();
		stack.getStackFrames();
		stack.getStackFrameLineNumber(null);
		stack.getStackFrameVariables(null);
		stack.getStackFrameName(null);
	}
}
