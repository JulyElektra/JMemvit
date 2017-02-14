package org.innopolis.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.innopolis.jmemvit.*;
import org.junit.Test;

public class TestHeapStrings {
	
	@Test
	public void heapStringsCheckingNull() {
		HeapStrings heap1 = new HeapStrings(null);
		assertEquals(heap1.getVariables().size(),0);
		HeapStrings heap2 = new HeapStrings();
		assertEquals(heap2.getVariables().size(),0);
	}
	
	@Test
	public void heapStringsChecking() {
		ArrayList<Variable> list = new ArrayList<Variable>();
		HeapStrings heap = new HeapStrings(list);
		assertTrue(heap.getVariables().equals(list));
	}
}
