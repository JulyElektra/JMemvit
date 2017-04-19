package org.innopolis.tests;


import org.innopolis.jmemvit.plugin.Activator;
import org.innopolis.tests.mock.MockBundleContext;
import org.junit.Test;
import org.osgi.framework.BundleContext;


public class TestActivator {
	
	@Test
	public void testInit() throws Exception {
		Activator act = new Activator();
		BundleContext context = new MockBundleContext();
		act.start(context);
	}
}
