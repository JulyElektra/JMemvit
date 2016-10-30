package org.innopolis.jmemvit;

import java.util.ArrayList;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.debug.core.IJavaThread;


public class Stack {
	
	public static IStackFrame[] getStackFrames(IJavaThread thread){
		if (thread == null){return null;}
		IStackFrame[] frames = null;
		try {frames = thread.getStackFrames();} catch (DebugException e) {}		
		return frames;
	}
	
	public static IStackFrame getTopStackFrame(IJavaThread thread){
		if (thread == null){return null;}
		IStackFrame frame = null;
		try {frame = thread.getTopStackFrame();} catch (DebugException e) {}		
		return frame;
	}

	public static String getStackFrameName(IStackFrame frame){
		if (frame == null){return "";}
		String frameName = "";
		try {frameName = frame.getName();} catch (DebugException e) {}	
		return frameName;
	}
	
	public static int getStackFrameLineNumber(IStackFrame frame){
		if (frame == null){return 0;}
		int lineNumber = 0;
		try {lineNumber = frame.getLineNumber();} catch (DebugException e) {}	
		return lineNumber;
	}	
	
	public static IVariable[] getStackFrameVariables(IStackFrame frame){
		if (frame == null){return null;}
		IVariable[] vars = null;
		try {vars = frame.getVariables();} catch (DebugException e) {}
		return vars;
	}
	
	public static ArrayList<String> stackFrameVariablesToStrings (IVariable[] vars) throws DebugException{
		for (IVariable var: vars){
			ArrayList<String> stackFrameVariables = new ArrayList<>();
			IValue varValue = var.getValue();
			String varName = var.getName();
			String varRef = var.getReferenceTypeName();
			stackFrameVariables.add("varName = " + varName + "; value = " + 
					varValue.toString() + "; varRef = " + varRef);
			if (varValue.hasVariables()) {
				IVariable[] subVariables = varValue.getVariables();
				for (IVariable subVaraible: subVariables) {
					IValue subVarValue = subVaraible.getValue();
					String subVarName = subVaraible.getName();
					String subVarRef = subVaraible.getReferenceTypeName();
					stackFrameVariables.add("subvarName = " + subVarName + 
							"; subvalue = " + subVarValue.toString() + 
							"; subVarRef = " + subVarRef);
				}
			}
			return stackFrameVariables;	
		}
		return null;
			
	}
}
