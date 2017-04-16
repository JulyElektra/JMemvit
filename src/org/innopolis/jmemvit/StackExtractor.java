package org.innopolis.jmemvit;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.debug.core.IJavaThread;

/**
 * The Stack class is used for deriving information from the threads
 */
public class StackExtractor {
	
	private IStackFrame[] stackFrames;
	private IJavaThread thread;
		
	/**
	 * The constructor
	 */
	public StackExtractor(IJavaThread thread) {
		this.thread = thread;
		this.stackFrames = deriveStackFrames(thread);
	}
	
	/**
	 * The constructor
	 */
	public StackExtractor(IStackFrame[] stackFrames) {
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
	public String getStackFrameName(IStackFrame frame) {
		String frameName = "";
		if (frame != null){
			try {
				frameName = frame.getName();
				if (frameName.contains("<") || frameName.contains(">")) {
					frameName = frameName.replaceAll("<", "");
					frameName = frameName.replaceAll(">", "");
				}
			} catch (DebugException e) {
				e.printStackTrace();
			}
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
	public IVariable[] getStackFrameVariables(IStackFrame frame){
		IVariable[] vars = null;
		if (frame != null){
			try {
				vars = frame.getVariables();
			} catch (DebugException e) {
				e.printStackTrace();
			}
			}	
		return vars;
	}
	

	/*
	 * This method derives stack frames from the thread and
	 * returns the array of these frames
	 */
	private IStackFrame[] deriveStackFrames(IJavaThread thread){
		if (thread == null){return null;}
		IStackFrame[] frames = null;
		try {
			frames = thread.getStackFrames();
		} catch (DebugException e) {
			}		
		return frames;
	}

}
