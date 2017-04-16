package org.innopolis.tests;


import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.innopolis.jmemvit.DebugEventListener;
import org.innopolis.jmemvit.ViewManager;
import org.innopolis.tests.mock.MockIJavaThread;
import org.junit.Test;

public class TestViewManager {

	@Test
	public void test() throws DebugException {
		DebugEventListener listener = new DebugEventListener();
		MockIJavaThread[] thead = {new MockIJavaThread()};
		DebugEvent event = new DebugEvent(new MockIJavaThread(), 3);
		DebugEvent event2 = new DebugEvent(new MockIJavaThread(), 5);
		DebugEvent[] events1 = {event, event, event2};
		listener.handleDebugEvents(events1);

		Display display = new Display();
		Shell shell = new Shell(display);
		Browser browser = new Browser(shell, SWT.NONE);
		
		ViewManager view = new ViewManager(listener, browser);
		
		//view.vizualizateView();
		DebugEvent[] events2 = {event, event, event2, event2};
		listener.handleDebugEvents(events2);
		//view.createPartControl(shell);
		view.vizualizateView();
		view.vizualizateView();
		view.setFocus();
		ViewManager view2 = new ViewManager();
	}
}
