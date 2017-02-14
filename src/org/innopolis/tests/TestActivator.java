package org.innopolis.tests;


import org.innopolis.jmemvit.Activator;
import org.innopolis.jmemvit.mock.MockBundleContext;
import org.junit.Test;


public class TestActivator {
	
	@Test
	public void testInit() throws Exception {
		Activator act = new Activator();
		/*act.stop(new MockBundleContext());
		act.start(new MockBundleContext());
		Activator.getDefault();*/
		//act.getImageDescriptor(path);
	}
}
