package org.innopolis.jmemvit;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IStackFrame;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.Method;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VirtualMachine;


/**
 * The StackFrame class is used storing information about stack frames content
 */
public class StackFrame {
	
	private int number;
	private String name;
	private ArrayList<Variable> vars;
	
	/**
	 * The constructor
	 */
	public StackFrame(int number, String name, ArrayList<Variable> vars) {
		this.number = number;
		this.name = name;
		this.vars = vars;
	}

	public int getNumber() {
		return number;
	}

	public String getName() {
		return name;
	}

	public ArrayList<Variable> getVars() {
		return vars;
	}	
	
	public static String getMethodClassName (IStackFrame frame, int frameNum){
		String frameName;
		try {
			frameName = frame.getName();
		} catch (DebugException e1) {
			e1.printStackTrace();
			return null;
		}
		String className = null;
		VirtualMachine jvm = DebugEventListener.getJVM(frame);
		List<ThreadReference> threads = jvm.allThreads();
		for (ThreadReference thread: threads) {
			try {
				thread.suspend();
				List<com.sun.jdi.StackFrame> frames = thread.frames();

				if (frames.size() > frameNum) {
					com.sun.jdi.StackFrame fr = frames.get(frameNum);
					Method method = fr.location().method();
					if (frameName.equals(method.name())) {
						className = method.declaringType().name();
					}
				}
				thread.resume();
			} catch (IncompatibleThreadStateException e) {
				e.printStackTrace();
				return null;
			}
		}
		return className;
	}

	public static boolean containsName(StackFrame frame,
		ArrayList<StackFrame> frames) {
		for (StackFrame frameCurrent: frames) {
			if (frameCurrent.getName().equals(frame.getName())) {
				return true;
			}
		}
		return false;
	}
}
