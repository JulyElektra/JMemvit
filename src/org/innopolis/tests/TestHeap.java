package org.innopolis.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.innopolis.jmemvit.model.Heap;
import org.innopolis.jmemvit.model.Variable;
import org.innopolis.jmemvit.plugin.*;
import org.junit.Test;

public class TestHeap {
	
	@Test
	public void heapStringsCheckingNull() {
		Heap heap1 = new Heap(null);
		assertEquals(heap1.getVariables().size(),0);
		Heap heap2 = new Heap();
		assertEquals(heap2.getVariables().size(),0);
	}
	
	@Test
	public void heapStringsChecking() {
		ArrayList<Variable> list = new ArrayList<Variable>();
		Heap heap = new Heap(list);
		assertTrue(heap.getVariables().equals(list));
	}
}
