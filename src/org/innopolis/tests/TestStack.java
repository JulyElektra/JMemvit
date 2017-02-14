package org.innopolis.tests;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IStackFrame;
import org.innopolis.jmemvit.*;
import org.innopolis.jmemvit.mock.MockIJavaThread;
import org.junit.Test;

public class TestStack {
	
	@Test
	public void testInit() throws DebugException {
		MockIJavaThread thread = new MockIJavaThread();
		Stack stack = new Stack (thread);
		IStackFrame frame = stack.getTopStackFrame();
		stack.getStackFrames();
		stack.getStackFrameLineNumber(frame);
	}
	
	@Test
	public void testThreadNull() throws DebugException {
		MockIJavaThread thread = null;
		Stack stack = new Stack (thread);
		IStackFrame frame = stack.getTopStackFrame();
		stack.getStackFrames();
		stack.getStackFrameLineNumber(null);
		stack.getStackFrameVariables(null);
		stack.getStackFrameName(null);
	}
}
