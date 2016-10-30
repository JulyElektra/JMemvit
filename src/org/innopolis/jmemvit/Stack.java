package org.innopolis.jmemvit;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.debug.core.IJavaThread;


public class Stack {
	
	public static IStackFrame[] getStackFrames(IJavaThread thread){
		if (thread == null){return null;}
		IStackFrame[] Frames = null;
		try {Frames = thread.getStackFrames();} catch (DebugException e) {}		
		return Frames;
	}
	
	public static IStackFrame getTopStackFrame(IJavaThread thread){
		if (thread == null){return null;}
		IStackFrame Frame = null;
		try {Frame = thread.getTopStackFrame();} catch (DebugException e) {}		
		return Frame;
	}

	public static String getStackFrameName(IStackFrame frame){
		if (frame == null){return "";}
		String FrameName = "";
		try {FrameName = frame.getName();} catch (DebugException e) {}	
		return FrameName;
	}
	
	public static int getStackFrameLineNumber(IStackFrame frame){
		if (frame == null){return 0;}
		int LineNumber = 0;
		try {LineNumber = frame.getLineNumber();} catch (DebugException e) {}	
		return LineNumber;
	}	
	
	public static IVariable[] getStackFrameVariables(IStackFrame frame){
		if (frame == null){return null;}
		IVariable[] vars = null;
		try {vars = frame.getVariables();} catch (DebugException e) {}
		return vars;
	}	
}
