package org.innopolis.jmemvit;

import java.util.ArrayList;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.debug.core.IJavaThread;

/**
 * The Stack class is used for deriving information from the threads
 */
public class Stack {
	
	private IStackFrame[] stackFrames;
	private IJavaThread thread;
		
	/**
	 * The constructor
	 */
	public Stack(IJavaThread thread) {
		this.thread = thread;
		this.stackFrames = deriveStackFrames(thread);
	}
	
	/**
	 * The constructor
	 */
	public Stack(IStackFrame[] stackFrames) {
		this.stackFrames = stackFrames;
	}
	
	/*
	 * Returns all stack frames
	 */
	public IStackFrame[] getStackFrames(){
		return stackFrames;
	}
	
	/*
	 * Returns top stack frame
	 */
	public IStackFrame getTopStackFrame() throws DebugException{
		IStackFrame frame = null;
		if (thread != null){		
			frame = thread.getTopStackFrame();	
			}
		return frame;
	}

	/*
	 * Returns name of particular stack frame
	 */
	public String getStackFrameName(IStackFrame frame) throws DebugException{
		String frameName = "";
		if (frame != null){
			frameName = frame.getName();
			}			
		return frameName;
	}
	
	/*
	 * Returns number of particular stack frame
	 */
	public int getStackFrameLineNumber(IStackFrame frame) throws DebugException{
		int lineNumber = 0;
		if (frame != null){
			lineNumber = frame.getLineNumber();
			}	
		return lineNumber;
	}	
	
	/*
	 * Returns array of variables of particular stack frame
	 */
	public IVariable[] getStackFrameVariables(IStackFrame frame) throws DebugException{
		IVariable[] vars = null;
		if (frame != null){
			vars = frame.getVariables();
			}	
		return vars;
	}
	
	/*
	 * This method returns array of strings, that consists data from
	 * stack frame. Result will be used for visualization of stack data
	 */
	public ArrayList<String> getStackFrameStrings(IStackFrame frame) throws DebugException {
	
		// If stack frame is not empty
		if (frame != null){
						
			// Get name of the stack item
			String frameName = getStackFrameName(frame);
			
			// Get line number of the stack item
			int lineNumber = getStackFrameLineNumber(frame);
			
			ArrayList<String> stackStrings = new ArrayList<>();
			stackStrings.add(Global.STACK + " number of vars: " + getStackFrameVariables(frame).length);
			
			// Add frame name and line number to the array
			stackStrings.add(Global.PROGRAM_COUNTER + ": " + frameName + " " + lineNumber);
			
			// Get Variables of the stack item
			IVariable[] vars = getStackFrameVariables(frame);
			ArrayList<String> varStrings = Variable.toStrings(vars);
			
			// Add variables to the array
			for (String varString: varStrings){
				stackStrings.add(varString);
			}
			
			return stackStrings;
		}
		else {
			// Stack frame is empty
			return null;
		}
	}

	/*
	 * This method derives stack frames from the thread and
	 * returns the array of these frames
	 */
	private IStackFrame[] deriveStackFrames(IJavaThread thread){
		if (thread == null){return null;}
		IStackFrame[] frames = null;
		try {frames = thread.getStackFrames();} catch (DebugException e) {}		
		return frames;
	}

}
