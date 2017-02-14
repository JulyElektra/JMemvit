package org.innopolis.jmemvit.mock;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IRegisterGroup;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.debug.core.model.IVariable;

public class MockIStackFrame implements IStackFrame{
	public static int n;

	public MockIStackFrame() {
		n++;
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
	public IThread getThread() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVariable[] getVariables() throws DebugException {
		MockIVariable[] vars = {new MockIVariable(),new MockIVariable(),new MockIVariable(),new MockIVariable(), new MockIVariable(),new MockIVariable(),new MockIVariable(),new MockIVariable(),new MockIVariable(),new MockIVariable()};
		return vars;
	}

	@Override
	public boolean hasVariables() throws DebugException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getLineNumber() throws DebugException {
		// TODO Auto-generated method stub
		return n + 33;
	}

	@Override
	public int getCharStart() throws DebugException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCharEnd() throws DebugException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() throws DebugException {
		if (n % 2 == 0) {
			return "<StackFrameName>" + n;
		}
		return "StackFrameName" + n;
	}

	@Override
	public IRegisterGroup[] getRegisterGroups() throws DebugException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasRegisterGroups() throws DebugException {
		// TODO Auto-generated method stub
		return false;
	}

}
