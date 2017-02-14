package org.innopolis.tests;

import java.awt.Graphics;
import java.awt.Shape;

import javax.swing.text.BadLocationException;
import javax.swing.text.View;
import javax.swing.text.Position.Bias;

import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.widgets.Composite;
import org.innopolis.jmemvit.DebugEventListener;
import org.innopolis.jmemvit.ViewManager;
import org.innopolis.jmemvit.mock.MockIJavaThread;
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

		
		ViewManager view = new ViewManager(listener);
		view.vizualizateView();
		DebugEvent[] events2 = {event, event, event2, event2};
		listener.handleDebugEvents(events2);
		
	}
}
