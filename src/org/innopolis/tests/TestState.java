package org.innopolis.tests;

import static org.junit.Assert.*;

import org.innopolis.jmemvit.*;
import org.innopolis.jmemvit.data.Heap;
import org.innopolis.jmemvit.data.Stack;
import org.innopolis.jmemvit.data.State;
import org.junit.Test;

public class TestState {

	//Test checks method compareTo in class State. State with a later date is bigger.
	@Test
	public void comparison() {
		State state1 = new State("01.01.2017 01:00:32:234", null, null );
		State state2 = new State("01.01.2017 01:03:32:234", null, null );
		State state3 = new State("01.01.2017 01:00:32:234", null, null );
		assertEquals("state1 must be smaller than state2", -1, state1.compareTo(state2));
		assertEquals("state2 must be equal state3", 0, state1.compareTo(state3));
		assertEquals("state2 must be bigger than state1", 1, state2.compareTo(state1));	
	}
	
	//Test checks method compareTo in class State if we have wrong data
	@Test 
	public void comparisonWrongInput() {
		State state1 = new State("01.01.2017 01:00:32:234", new Stack(),new Heap() );
		State state4 = new State("1234", null, null );
		state4.compareTo(state1);
		state1.compareTo(new Object());
	}
	
	@Test
	public void gettersAndSetters() {
		State state1 = new State("01.01.2017 01:00:32:234", null, null );
		String date = "13.02.2017 12:00:32:234";
		state1.setDate(date);
		Heap heap = new Heap();
		Stack stack = new Stack();
		state1.setHeap(heap);
		state1.setStack(stack);
		assertTrue(state1.getHeap().equals(heap));
		assertTrue(state1.getStack().equals(stack));
		assertTrue(state1.getDate().equals(date));
	}

}
