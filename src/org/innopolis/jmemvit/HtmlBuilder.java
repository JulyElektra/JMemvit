package org.innopolis.jmemvit;

import static org.innopolis.jmemvit.Global.*;

import java.util.ArrayList;

public class HtmlBuilder {
	private State state;

	
	// Parameter "Stack" or "Heap"
	private static final String heapHeader = "<td width=\"50%\" style=\"vertical-align: top;\"><div><strong>Heap:</strong></div>";
	private static final String stackHeader = "<td width=\"50%\" style=\"vertical-align: top;\"><div><strong>Stack:</strong></div>";
	private static final String heapOrStackFooter = "</td>";
	
	private static final String subTableHeader = "<table width=\"95%\" border=\"1\" style =\"border: 1px solid #000000;border-collapse: collapse; border-color:#000000;\" ><tbody>";
	private static final String subTableFooter = "</tbody></table>";
	
	// Template for building HTML string for stack frame title. Arguments: frameNumber, frameName
	private static final String stackFrameHeaderFooter = "<tr  style=\"text-align: left;background-color: #00CED1;\"><th >%s</th><th colspan=\"2\">%s</th></tr>";
	
	private static final String varsHeader = "<tr  style=\"text-align: left;background-color: #AFEEEE;\">";

	private static final String varsStackHTML = varsHeader +  "<td width=\"33%\">Type</td>"
			+ "	<td width=\"33%\">Name</td>"
			+ "	<td width=\"33%\">Value</td></tr>";
	private static final String varsHeapHTML = varsHeader 
			+ "<td width=\"15%\">Type</td>"
			+ "	<td width=\"15%\">Name</td>"
			+ "	<td width=\"15%\">Value</td>"
	// Field header
			+ "	<td width=\"55%\">"
			+ "<table width=\"100%\"> "
			+ "<tbody>"
			+ "<tr style=\"text-align: left;background-color: #00CED1;\">"
			+ "<td></td>"
			+ "<td>Fields</td>"
			+ "<td></td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td>Type</td>"
			+ "<td>Name</td>"
			+ "<td>Value</td>"
			+ "</tr>"
			+ "</tbody>"
			+ "</table >"
			+ "</td></tr>";
	
	private static final int pageWidth = 1200;
	private static final String tableHeader = "<style>td{border-color:#000000;}div{height:auto;} </style><table width=\"" + pageWidth + "\"><tbody> <tr>";
	private static final String tableFooter = "</tr></tbody></table>";
	
	private static final String highlightCell = "<td style=\"background-color: #FFFF00; word-break: break-all;\" \"> ";
	
	private static final String varHighlightHtmlTemplate =  ""
			+ "<tr style=\"vertical-align: top;\">"
			+ "<td  style=\"word-break: break-all;\">%s</td>"
			+ "<td  style=\"word-break: break-all;\">%s</td>"
			+ highlightCell + "%s</td>";
	
	// Template for building HTML string for variables. Arguments: type, name, value	
	private static final String varHtmlTemplate =  ""
			+ "<tr style=\"vertical-align: top;\">"
			+ "<td  style=\"word-break: break-all;\">%s</td>"
			+ "<td  style=\"word-break: break-all;\">%s</td>"
			+ "<td  style=\"word-break: break-all;\">%s</td>";
	
	private static final int colomnWidth =  (int) (pageWidth * 0.50 * 0.95 * 0.55 / 3);
	
	// Template for building HTML string for variables. Arguments: type, name, value	
	private static final String varFieldHtmlTemplate =  ""
			+ "<tr style=\"vertical-align: top;\">"
			+ "<td width = \"" + colomnWidth + "\" style=\"word-break: break-all;\">%s</td>"
			+ "<td width = \"" + colomnWidth + "\" style=\"word-break: break-all;\">%s</td>"
			+ "<td width = \"" + colomnWidth + "\" style=\"word-break: break-all;\">%s</td>";
	
	private static final String highlightCellField = "<td width = \"" + colomnWidth + "\" style=\"background-color: #FFFF00; word-break: break-all;\" \"> ";
	
	private static final String varFieldHighlightHtmlTemplate =  ""
			+ "<tr style=\"vertical-align: top;\">"
			+ "<td width = \"" + colomnWidth + "\"  style=\"word-break: break-all;\">%s</td>"
			+ "<td width = \"" + colomnWidth + "\"  style=\"word-break: break-all;\">%s</td>"
			+ highlightCellField + "%s</td>";
	
	private static final String varHighlightFieldHtmlTemplate =  
			varFieldHighlightHtmlTemplate +  "</tr>";	

	// Template for building HTML string for Stack variables. Arguments: type, name, value
	private static final String varStackHtmlTemplate =  
			varHtmlTemplate +  "</tr>";	
	
	private static final String varHighlightStackHtmlTemplate =  
			varHighlightHtmlTemplate +  "</tr>";	
	
	// Template for building HTML string for Heap variables. Arguments: type, name, value, fields
	private static final String varHeapHtmlTemplate = 
			//varHtmlTemplate 
			"<td >"
			+ "<table width=\"100%\" border=\"1\" style =\"border: 1px solid #000000;border-collapse: collapse;border-color:#FFFFFF; \"><tbody>";
	
	
	private static final String varHighlightHeapHtmlTemplate = 
			//varHighlightHtmlTemplate 
			"<td >"
			+ "<table width=\"100%\"  border=\"1\" style =\"border: 1px solid #000000;border-collapse: collapse;border-color:#FFFFFF;\"><tbody>";

	
	private static final String tableFieldsFooter = "</tbody></table></td></tr>";

	
	
	/**
	 * The constructor
	 */
	public HtmlBuilder(State state) {
		this.state = state;
	}

	/*
	 * The method returns HTML string for the state
	 */
	public String getHtmlString() {
		StackFrameStrings stack = state.getStack();
		HeapStrings heap = state.getHeap();
		String htmlString = build(stack, heap);
		return htmlString;
	}

	/*
	 * The method build HTML string for the stack and heap
	 */
	private String build(StackFrameStrings stack, HeapStrings heap) {
		String stackHtml = getStackHtml(stack);
		String heapHtml = getHeapHtml(heap);
		String htmlString = tableHeader + stackHtml + heapHtml + tableFooter;
		return htmlString;
	}
	
	/*
	 * The method build HTML string for the stack
	 */
	private String getStackHtml(StackFrameStrings stack) {
		String stackFramesHtml = getStackFramesHtml(stack);
		//String stackHtml = stackHeader +  getButtonsHtml() + stackFramesHtml;
		String stackHtml = stackHeader + stackFramesHtml + heapOrStackFooter;
		return stackHtml;
	}
	

	/*
	 * The method build HTML string for the heap
	 */
	private String getHeapHtml(HeapStrings heap) {
		ArrayList<Variable> vars = heap.getVariables();
		String heapHtml = heapHeader + subTableHeader + getVarsHtml(vars, HEAP) + subTableFooter;
		return heapHtml;
	}

	
	/*
	 * The method build HTML string for variables
	 */
	private String getVarsHtml(ArrayList<Variable> vars, String partOfVizualization) {
		String varsHtml; 
		if (partOfVizualization.equals(HEAP)) {
			varsHtml = varsHeapHTML;
		} else {
			varsHtml = varsStackHTML;
		}	
		Variable.sortVariablesByType(vars); 
		for (Variable var: vars){ 
			String varHtml = getVarHtml(var, partOfVizualization);
			varsHtml = varsHtml + varHtml;
		}
		return varsHtml;
	}
	
	/*
	 * The method build HTML string for the variable
	 */
	private String getVarHtml(Variable var, String partOfVizualization) {
		String name = var.getName();
		String type = var.getType();
		String value = var.getValue();
		ArrayList<Variable> fields = var.getFields();
		
		String hasChanged = var.getHasValueChanged();
		
		String varHtml = "";
		
		if (hasChanged.equals("true")) {
			if (partOfVizualization.equals(HEAP)) {
				varHtml = String.format(varHighlightHtmlTemplate, type, name, value, fields)+ varHighlightHeapHtmlTemplate + getFieldsHtml(fields) + tableFieldsFooter;
			} else if (partOfVizualization.equals(STACK)){
				varHtml = String.format(varHighlightStackHtmlTemplate, type, name, value);
			} else {
				varHtml = String.format(varHighlightFieldHtmlTemplate, type, name, value);
			}
		} else if (hasChanged.equals("false")) {			
			if (partOfVizualization.equals(HEAP)) {
				varHtml = String.format(varHtmlTemplate, type, name, value, fields)+ varHeapHtmlTemplate + getFieldsHtml(fields) + tableFieldsFooter;
			} else if (partOfVizualization.equals(STACK)){
				varHtml = String.format(varStackHtmlTemplate, type, name, value);
			} else {
				varHtml = String.format(varFieldHtmlTemplate, type, name, value);	
			}
		} else {
// TODO			
		}	
		
		
		return varHtml;
	}

	private String getFieldsHtml(ArrayList<Variable> fields) {
		String fieldsHtml = "";
		if (fields.isEmpty() || fields == null) {
			return fieldsHtml;
		}
		Variable.sortVariablesByType(fields); 
		for (Variable v: fields) {
			fieldsHtml = fieldsHtml + getVarHtml(v, FIELDS);
		}
		return fieldsHtml;
	}

	/*
	 * The method build HTML string for stack frames
	 */
	private String getStackFramesHtml(StackFrameStrings stack) {
		String stackFramesHtml = "";
		ArrayList<StackFrame> stackFrames = stack.getStackFrames();
		for (StackFrame frame: stackFrames) {
			String stackFrameHtml = getStackFrameHtml(frame);
			stackFramesHtml = stackFramesHtml + stackFrameHtml;
		}
		return stackFramesHtml;
	}
	
	/*
	 * The method build HTML string for stack frame
	 */
	private String getStackFrameHtml(StackFrame frame) {
		int number = frame.getNumber();
		String frameName = frame.getName();
		String frameTitleHtml = getFrameTitleHtml(number, frameName);
		ArrayList<Variable> vars = frame.getVars();
		String varsHtml = getVarsHtml(vars, STACK);
		String stackFrameHtml = subTableHeader + frameTitleHtml + varsHtml + subTableFooter;
		return stackFrameHtml;
	}

	/*
	 * The method build HTML string for stack frame title: number and name
	 */
	private String getFrameTitleHtml(int number, String frameName) {
		String frameTitleHtml = String.format(stackFrameHeaderFooter, number, frameName);;
		return frameTitleHtml;
	}
	
		
}
