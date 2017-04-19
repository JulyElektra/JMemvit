package org.innopolis.tests.mock;

import java.io.IOException;
import java.util.List;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.ISourceLocator;
import org.eclipse.debug.core.model.IStreamsProxy;
import org.eclipse.jdt.internal.debug.core.model.JDIDebugTarget;

import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import com.sun.tools.attach.spi.AttachProvider;

public class MockILaunch implements ILaunch {

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
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] getChildren() {
		// TODO Auto-generated method stub
		Object[] objs = new Object[2];
		AttachProvider provider = new AttachProvider() {
			
			@Override
			public String type() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String name() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<VirtualMachineDescriptor> listVirtualMachines() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public VirtualMachine attachVirtualMachine(String arg0)
					throws AttachNotSupportedException, IOException {
				// TODO Auto-generated method stub
				return null;
			}
		};
		String id = "1";
		MockVirtualMachine vm = new MockVirtualMachine( );
		
		objs[0] = new MockJDIDebugTarget();
		objs[1] = new MockJDIDebugTarget();
		return objs;
	}

	@Override
	public IDebugTarget getDebugTarget() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IProcess[] getProcesses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDebugTarget[] getDebugTargets() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addDebugTarget(IDebugTarget target) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeDebugTarget(IDebugTarget target) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addProcess(IProcess process) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeProcess(IProcess process) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ISourceLocator getSourceLocator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSourceLocator(ISourceLocator sourceLocator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getLaunchMode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ILaunchConfiguration getLaunchConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAttribute(String key, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getAttribute(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren() {
		// TODO Auto-generated method stub
		return false;
	}

}
