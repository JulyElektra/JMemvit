package org.innopolis.tests.mock;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.jdt.internal.debug.core.model.JDIDebugTarget;

import com.sun.jdi.VirtualMachine;

public class MockJDIDebugTarget extends JDIDebugTarget{

	private static VirtualMachine jvm = new MockVirtualMachine();
	private static IProcess process = new MockIProcess();

	public MockJDIDebugTarget(ILaunch launch, VirtualMachine jvm, String name,
			boolean supportTerminate, boolean supportDisconnect,
			IProcess process, boolean resume) {
		super(launch, jvm, name, supportTerminate, supportDisconnect, process, resume);
	}
	
	public MockJDIDebugTarget() {
		super(new MockILaunch(), jvm , "a", false, true, process, false);
	}

}
