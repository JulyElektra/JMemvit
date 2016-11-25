package org.innopolis.jmemvit;

import java.lang.reflect.Array;
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
	
	// TODO refactoring
	public static ArrayList<String> variablesToStrings (Object[] vars) throws DebugException{
		ArrayList<String> variables = new ArrayList<>();
		for (Object varObj: vars){
			IVariable var = (IVariable) varObj;
			IValue varValue = var.getValue();
			String varName = var.getName();
			String varRef = var.getReferenceTypeName();
			variables.add(varName + " = " + varValue.toString() + "; " + Global.TYPE + ": " + varRef);
			if (!varRef.contains(".") || varRef.contains("[]")) {
				if (varValue.hasVariables()) {
					IVariable[] subVariables = varValue.getVariables();
					for (IVariable subVaraible: subVariables) {
						IValue subVarValue = subVaraible.getValue();
						String subVarName = subVaraible.getName();
						String subVarRef = subVaraible.getReferenceTypeName();
						variables.add("      " + subVarName + 
								" = " + subVarValue.toString() + 
								"; " + Global.TYPE + ": " + subVarRef);
					}
				}
			}
				
		}
		return variables;
			
	}
	
	/*
	 * This method returns array of strings, that consists data from
	 * stack. Result will be used for visualization of stack data
	 */
	public static ArrayList<String> getStackFrameStrings(IStackFrame frame) throws DebugException {
	
		// If stack is not empty
		if (frame != null){
						
			// Get name of top stack item
			String frameName = getStackFrameName(frame);
			
			// Get line number of top stack item
			int lineNumber = getStackFrameLineNumber(frame);
			
			ArrayList<String> stackStrings = new ArrayList<>();
			stackStrings.add(Global.STACK + " number of vars: " + Stack.getStackFrameVariables(frame).length);
			
			// Add frame name and line number to the array
			stackStrings.add(Global.PROGRAM_COUNTER + ": " + frameName + " " + lineNumber);
			
			// Get Variables of top stack item
			IVariable[] vars = getStackFrameVariables(frame);
			ArrayList<String> varStrings = variablesToStrings(vars);
			
			// Add variables to the array
			for (String varString: varStrings){
				stackStrings.add(varString);
			}
			
			return stackStrings;
		}
		else {
			// Stack is empty
			return null;
		}
	}



}
