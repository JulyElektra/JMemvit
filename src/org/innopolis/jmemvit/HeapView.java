package org.innopolis.jmemvit;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;

import com.sun.jdi.Field;
import com.sun.jdi.Method;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.Value;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.jdt.debug.core.IJavaThread;
import org.eclipse.swt.SWT;


public class HeapView extends ViewPart{
	private DebugEventListener jdiEventListener = null;
	private Tree treeOne;
	private Tree treeTwo;

	class RunnableForThread2 implements Runnable{
		public void run() {
			while (true) {
				try { Thread.sleep(1000); } catch (Exception e) { }
				Runnable task = () -> { VizualizateHeapJava();};
				Display.getDefault().asyncExec(task);
			}			
		}
	}
		
	@Override
	public void createPartControl(Composite parent) {
		
		createTreeOne(parent);
		createTreeTwo(parent);

		jdiEventListener = new DebugEventListener();
		DebugPlugin.getDefault().addDebugEventListener(jdiEventListener);
		
		Runnable runnable = new RunnableForThread2();
		Thread thread2 = new Thread(runnable);
		thread2.start();	
	}

	@Override
	public void setFocus() {		
	}
	

	private void createTreeOne(Composite parent){
		treeOne = new Tree(parent, SWT.MIN);
		treeOne.setHeaderVisible(true);
		treeOne.setLinesVisible(true);		
		treeOne.setVisible(true);

		
		TreeColumn columnName = new TreeColumn(treeOne, SWT.LEFT);
		columnName.setText("classes");
		columnName.setWidth(300);
	}
	
	private void createTreeTwo(Composite parent){
		treeTwo = new Tree(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		treeTwo.setHeaderVisible(true);
		treeTwo.setLinesVisible(true);		
		treeTwo.setVisible(true);
		
		TreeColumn columnName = new TreeColumn(treeTwo, SWT.LEFT);
		columnName.setText("instances");
		columnName.setWidth(300);		
	}
	
	
	private void VizualizateHeapJava(){
		if(jdiEventListener == null){return;}		
		if (!jdiEventListener.isItUpdatedThread()){return;}
		
		IJavaThread currentThread =  jdiEventListener.getCurrentThread();	
		IStackFrame topFrame = Stack.getTopStackFrame(currentThread);		
		
		VirtualMachine jvm = Heap.getJVM(topFrame);	
		if (jvm == null){return;}
	
		for (TreeItem item : treeOne.getItems()){item.dispose();}
		for (TreeItem item : treeTwo.getItems()){item.dispose();}
		
		List<ReferenceType> allMyClasses = Heap.getAllMyClasses(jvm);
		allMyClasses = Heap.sortByHashCode(allMyClasses);
		for(ReferenceType Class : allMyClasses){
			VizualizateClass(Class);
			List<ObjectReference> instances = Class.instances(0);
			for (ObjectReference instance : instances){VizualizateClassInstance(instance);}	
		}

	}
	
	private void VizualizateClass(ReferenceType Class){

		int hashCode = Class.hashCode();
		
		TreeItem item = new TreeItem(treeOne, SWT.LEFT);
		item.setText(0, Class.toString() + " : @" + hashCode);	
		
		TreeItem subItem = new TreeItem(item, SWT.LEFT);
		subItem.setText(0, "this : @" + hashCode);
					
		List<Field> fields = Class.fields();
		for (Field field : fields){
			if (!field.isStatic()){continue;}
			String stringValue = "";
			Value value = Class.getValue(field);
			if (value != null){stringValue = value.toString();}
			if (stringValue.contains("id")){stringValue = "@"+Class.getValue(field).hashCode();}
			if (field.typeName() != null && value !=null && field.typeName().equals("java.lang.String") ){stringValue = "@"+Class.getValue(field).hashCode();}			
			
			subItem = new TreeItem(item, SWT.LEFT);
			subItem.setText(0, field.typeName() + " " + field.toString() + " : " + stringValue);		
		}	
	}
	
	private void VizualizateClassInstance(ObjectReference instance){

		TreeItem item = new TreeItem(treeTwo, SWT.LEFT);
		item.setText(0, instance.toString());

		List<ReferenceType> parentClasses = new ArrayList<ReferenceType>();
		List<Method> methods = instance.referenceType().allMethods();
		for (Method method : methods){
			ReferenceType type =  method.declaringType();
			boolean isExist = false;
			for (ReferenceType parentClass : parentClasses){if (parentClass.equals(type)){isExist = true;}}
			if (!isExist){parentClasses.add(type);}
		}		
		parentClasses = Heap.sortByHashCode(parentClasses);
		
		//the first item placed to the end 
		ReferenceType temp = parentClasses.get(0);
		parentClasses.remove(0);
		parentClasses.add(temp);
		
		for (ReferenceType parentClass : parentClasses){
			
			TreeItem subItem = new TreeItem(item, SWT.LEFT);
			subItem.setText(0, parentClass.toString());
			
			TreeItem subsubItem = new TreeItem(subItem, SWT.LEFT);
			if (instance.type() != null){subsubItem.setText(0, instance.type().name());}
			subsubItem.setText(0, "this : @" + instance.hashCode());

			
			subsubItem = new TreeItem(subItem, SWT.LEFT);
			subsubItem.setText(0, "class : @" + parentClass.hashCode());
		

			List<Field> parentfields = parentClass.fields();
			for (Field field : parentfields){
				if (field.isStatic()){continue;}
					String valueString = "";
					Value value = instance.getValue(field);
					if(value == null){valueString = "null";}else{valueString = value.toString();}
					if (valueString.contains("id")){valueString = "@"+instance.getValue(field).hashCode();}
					if (field.typeName() != null && value !=null && field.typeName().equals("java.lang.String")){valueString = "@"+instance.getValue(field).hashCode();}	
				
					subsubItem = new TreeItem(subItem, SWT.LEFT);
					subsubItem.setText(0, ""+field.typeName() + " " + field + " : " + valueString);			
			}	
		}		
	}

}
