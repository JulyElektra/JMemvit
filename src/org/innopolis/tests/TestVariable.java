package org.innopolis.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.innopolis.jmemvit.model.Heap;
import org.innopolis.jmemvit.model.Stack;
import org.innopolis.jmemvit.model.State;
import org.innopolis.jmemvit.model.Variable;
import org.innopolis.jmemvit.plugin.*;
import org.junit.Test;

public class TestVariable {

	@Test
	public void test() {
		Variable v = new Variable("obj", "Object", "obj 123", "KEY_CHANGED false KEY_TYPE char[] KEY_VALUE char[36] (id=3856) KEY_NAME digits  ; ", "true");
		Variable v1 = new Variable("obj", "Object", "obj 123", "KEY_CHANGED false KEY_TYPE char[] KEY_VALUE char[36] (id=3856) KEY_NAME digits  ; ", "true");
		Variable v2 = new Variable("i", "Integer", "123", "KEY_CHANGED false KEY_TYPE char[] KEY_VALUE char[36] (id=3856) KEY_NAME digits  ; ", "true");
		assertEquals(0, v1.compareTo(v));
		assertTrue(v.compareTo(v2) > 0);
		assertTrue(v.equalsNameAndType(v1));
		assertFalse(v.equalsNameAndType(v2));
		v.setHasJustInitialized(false);
		assertEquals("false", v.getHasJustInitialized());
		assertTrue(v.getFields().size() > 0);
		assertEquals("false", v1.getHasJustInitialized());
		assertEquals("true", v1.getHasValueChanged());
		assertEquals("obj", v.getName());
		assertEquals("Object",v.getType());
		assertEquals("obj 123", v.getValue());
		ArrayList<Variable> arr = new ArrayList<Variable>();
		arr.add(v2);
		arr.add(v1);
		arr.add(v);
		Variable.sortVariablesByType(arr);
		assertEquals(v2, arr.get(0));
	}
}
