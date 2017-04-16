package org.innopolis.tests.mock;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.jdt.debug.core.IEvaluationRunnable;
import org.eclipse.jdt.debug.core.IJavaObject;
import org.eclipse.jdt.debug.core.IJavaThread;
import org.eclipse.jdt.debug.core.IJavaThreadGroup;
import org.eclipse.jdt.debug.core.IJavaVariable;

public class MockIJavaThread implements IJavaThread {
	IStackFrame[] frames = {new MockIStackFrame(),new MockIStackFrame(),new MockIStackFrame()};
	
	@Override
	public IStackFrame[] getStackFrames() throws DebugException {		
		return frames;
	}

	@Override
	public boolean hasStackFrames() throws DebugException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getPriority() throws DebugException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IStackFrame getTopStackFrame() throws DebugException {
		// TODO Auto-generated method stub
		return frames[frames.length - 1];
	}

	@Override
	public String getName() throws DebugException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBreakpoint[] getBreakpoints() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getModelIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDebugTarget getDebugTarget() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ILaunch getLaunch() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canResume() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canSuspend() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSuspended() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void resume() throws DebugException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void suspend() throws DebugException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canStepInto() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canStepOver() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canStepReturn() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isStepping() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void stepInto() throws DebugException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stepOver() throws DebugException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stepReturn() throws DebugException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canTerminate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTerminated() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void terminate() throws DebugException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canStepWithFilters() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void stepWithFilters() throws DebugException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canTerminateEvaluation() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IJavaVariable findVariable(String arg0) throws DebugException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IJavaObject getContendedMonitor() throws DebugException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFrameCount() throws DebugException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IJavaObject[] getOwnedMonitors() throws DebugException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IJavaThreadGroup getThreadGroup() throws DebugException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getThreadGroupName() throws DebugException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IJavaObject getThreadObject() throws DebugException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasOwnedMonitors() throws DebugException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDaemon() throws DebugException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOutOfSynch() throws DebugException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPerformingEvaluation() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSystemThread() throws DebugException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mayBeOutOfSynch() throws DebugException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void queueRunnable(Runnable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void runEvaluation(IEvaluationRunnable arg0, IProgressMonitor arg1,
			int arg2, boolean arg3) throws DebugException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop(IJavaObject arg0) throws DebugException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void terminateEvaluation() throws DebugException {
		// TODO Auto-generated method stub
		
	}

}
