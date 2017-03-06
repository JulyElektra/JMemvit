package org.innopolis.jmemvit;

import java.util.ArrayList;

public class HtmlBuilder {
	private State state;
	private static final String space = "&nbsp;";
	private static final String separator = "<p>";
	private static final String buttonTemplate = "<a class=\"button\" href=\"#\">%s</a>"
			+ "<style type=\"text/css\">.button {display: inline-block;text-align: center; vertical-align: middle;  padding: 12px 24px;	    border: 1px solid #273ba1;border-radius: 8px;background: #534aff;    background: -webkit-gradient(linear, left top, left bottom, from(#534aff), to(#394dde));	    background: -moz-linear-gradient(top, #534aff, #394dde);	    background: linear-gradient(to bottom, #534aff, #394dde);	    text-shadow: #591717 1px 1px 1px;	    font: normal normal bold 16px verdana;	    color: #ffffff;	    text-decoration: none;	}	.button:hover,	.button:focus {	    border: 1px solid #3e5eff;	    background: #6459ff;	    background: -webkit-gradient(linear, left top, left bottom, from(#6459ff), to(#445cff));	    background: -moz-linear-gradient(top, #6459ff, #445cff);	    background: linear-gradient(to bottom, #6459ff, #445cff);	    color: #ffffff;	    text-decoration: none;	}	.button:active {	    background: #322c99;	    background: -webkit-gradient(linear, left top, left bottom, from(#322c99), to(#394dde));	    background: -moz-linear-gradient(top, #322c99, #394dde);	    background: linear-gradient(to bottom, #322c99, #394dde);	}</style>";

	//private static final String htmlHeader = "";//<html>";
	//private static final String htmlFooter = "";//</html>";
	
	
	// Parameter "Stack" or "Heap"
	private static final String heapHeader = "<td width=\"50%\" style=\"vertical-align: top;\"><div><strong>Heap:</strong></div>";
	private static final String stackHeader = "<td width=\"50%\" style=\"vertical-align: top;\"><div><strong>Stack:</strong></div>";
	private static final String heapOrStackFooter = "</td>";
	
	private static final String subTableHeader = "<table width=\"95%\"><tbody>";
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
	
	private static final int width = 1600;
	private static final String tableHeader = "<table width=\"" + width + "\"><tbody> <tr>";
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
	
	// Template for building HTML string for variables. Arguments: type, name, value	
	private static final String varFieldHtmlTemplate =  ""
			+ "<tr style=\"vertical-align: top;\">"
			+ "<td width = \"139\" style=\"word-break: break-all;\">%s</td>"
			+ "<td width = \"139\" style=\"word-break: break-all;\">%s</td>"
			+ "<td width = \"139\" style=\"word-break: break-all;\">%s</td>";

	// Template for building HTML string for Stack variables. Arguments: type, name, value
	private static final String varStackHtmlTemplate =  
			varHtmlTemplate +  "</tr>";	
	
	private static final String varHighlightStackHtmlTemplate =  
			varHighlightHtmlTemplate +  "</tr>";	
	
	// Template for building HTML string for Heap variables. Arguments: type, name, value, fields
	private static final String varHeapHtmlTemplate = 
			//varHtmlTemplate 
			"<td >"
			+ "<table width=\"100%\"><tbody>";
	
	
	private static final String varHighlightHeapHtmlTemplate = 
			//varHighlightHtmlTemplate 
			"<td >"
			+ "<table width=\"100%\"><tbody>";

	
	private static final String tableFieldsFooter = "</tbody></table></td></tr>";
	private static final String colomnRowFooter = "</td></tr>";
	
	
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
		StackStrings stack = state.getStack();
		HeapStrings heap = state.getHeap();
		String htmlString = build(stack, heap);
		return htmlString;
	}

	/*
	 * The method build HTML string for the stack and heap
	 */
	private String build(StackStrings stack, HeapStrings heap) {
		String stackHtml = getStackHtml(stack);
		String heapHtml = getHeapHtml(heap);
		String htmlString = tableHeader + stackHtml + heapHtml + tableFooter;
		return htmlString;
	}
	
	/*
	 * The method build HTML string for the stack
	 */
	private String getStackHtml(StackStrings stack) {
		String stackFramesHtml = getStackFramesHtml(stack);
		//String stackHtml = stackHeader +  getButtonsHtml() + stackFramesHtml;
		String stackHtml = stackHeader + stackFramesHtml + heapOrStackFooter;
		return stackHtml;
	}
	
	/*
	 * The method build HTML string for buttons
	 */
	private String getButtonsHtml() {
		String buttonsHtml = getButtonHtml(Global.BACK)+ space + getButtonHtml(Global.FORWARD) + separator;
		return buttonsHtml;
	}
	
	/*
	 * The method build HTML string for the button
	 */
	private String getButtonHtml(String buttonName) {
		String buttonHtml = String.format(buttonTemplate, buttonName);
		return buttonHtml;
	}

	/*
	 * The method build HTML string for the heap
	 */
	private String getHeapHtml(HeapStrings heap) {
		ArrayList<Variable> vars = heap.getVariables();
		String heapHtml = heapHeader + subTableHeader + getVarsHtml(vars, Global.HEAP) + subTableFooter;
		return heapHtml;
	}

	
	/*
	 * The method build HTML string for variables
	 */
	private String getVarsHtml(ArrayList<Variable> vars, String partOfVizualization) {
		String varsHtml; 
		if (partOfVizualization.equals(Global.HEAP)) {
			varsHtml = varsHeapHTML;
		} else {
			varsHtml = varsStackHTML;
		}		
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
		
		String varHtml;
		
		if (hasChanged.equals("true")) {
			if (partOfVizualization.equals(Global.HEAP)) {
				varHtml = String.format(varHighlightHtmlTemplate, type, name, value, fields)+ varHighlightHeapHtmlTemplate + getFieldsHtml(fields) + tableFieldsFooter;
			} else if (partOfVizualization.equals(Global.STACK)){
				varHtml = String.format(varHighlightStackHtmlTemplate, type, name, value);
			} else {
				varHtml = String.format(varHighlightStackHtmlTemplate, type, name, value);
			}
		} else {			
			if (partOfVizualization.equals(Global.HEAP)) {
				varHtml = String.format(varHtmlTemplate, type, name, value, fields)+ varHeapHtmlTemplate + getFieldsHtml(fields) + tableFieldsFooter;
			} else if (partOfVizualization.equals(Global.STACK)){
				varHtml = String.format(varStackHtmlTemplate, type, name, value);
			} else {
				varHtml = String.format(varFieldHtmlTemplate, type, name, value);	
			}
		}
		return varHtml;
	}

	private String getFieldsHtml(ArrayList<Variable> fields) {
		String fieldsHtml = "";
		if (fields.isEmpty() || fields == null) {
			return fieldsHtml;
		}
		
		for (Variable v: fields) {
			fieldsHtml = fieldsHtml + getVarHtml(v, Global.FIELDS);
		}
		return fieldsHtml;
	}

	/*
	 * The method build HTML string for stack frames
	 */
	private String getStackFramesHtml(StackStrings stack) {
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
		String varsHtml = getVarsHtml(vars, Global.STACK);
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
