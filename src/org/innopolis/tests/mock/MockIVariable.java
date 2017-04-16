package org.innopolis.tests.mock;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;

public class MockIVariable implements IVariable{
	private static int n;
	
	

	public MockIVariable() {
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
	public void setValue(String expression) throws DebugException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(IValue value) throws DebugException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean supportsValueModification() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyValue(String expression) throws DebugException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyValue(IValue value) throws DebugException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IValue getValue() throws DebugException {
		if (n % 3 == 0 ) {
			return new MockIValue(n - 1);
		}
		return new MockIValue(n);
	}

	@Override
	public String getName() throws DebugException {
		if (n % 3 == 0 ) {
			return "this";
		}
		return "name" + n;
	}

	@Override
	public String getReferenceTypeName() throws DebugException {
		if (n % 3 == 0 ) {
			int k = n - 1;
			return "type" + k;
		}
		if (n % 5 == 0 ) {
			return "int";
		}		
		if (n % 4 == 0 ) {
			return "short";
		}
		if (n % 2 == 0 ) {
			return "byte";
		}
		return "type" + n;
	}

	@Override
	public boolean hasValueChanged() throws DebugException {
		// TODO Auto-generated method stub
		return false;
	}

}
