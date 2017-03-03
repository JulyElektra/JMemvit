package org.innopolis.jmemvit;

import java.util.ArrayList;

public class HtmlBuilder {
	private State state;
	private static final String space = "&nbsp;";
	private static final String separator = "<p>";
	private static final String buttonTemplate = "<a class=\"button\" href=\"#\">%s</a>"
			+ "<style type=\"text/css\">.button {display: inline-block;text-align: center; vertical-align: middle;  padding: 12px 24px;	    border: 1px solid #273ba1;border-radius: 8px;background: #534aff;    background: -webkit-gradient(linear, left top, left bottom, from(#534aff), to(#394dde));	    background: -moz-linear-gradient(top, #534aff, #394dde);	    background: linear-gradient(to bottom, #534aff, #394dde);	    text-shadow: #591717 1px 1px 1px;	    font: normal normal bold 16px verdana;	    color: #ffffff;	    text-decoration: none;	}	.button:hover,	.button:focus {	    border: 1px solid #3e5eff;	    background: #6459ff;	    background: -webkit-gradient(linear, left top, left bottom, from(#6459ff), to(#445cff));	    background: -moz-linear-gradient(top, #6459ff, #445cff);	    background: linear-gradient(to bottom, #6459ff, #445cff);	    color: #ffffff;	    text-decoration: none;	}	.button:active {	    background: #322c99;	    background: -webkit-gradient(linear, left top, left bottom, from(#322c99), to(#394dde));	    background: -moz-linear-gradient(top, #322c99, #394dde);	    background: linear-gradient(to bottom, #322c99, #394dde);	}</style>";

	private static final String heapHeader = "</div><div class=\"heap\"><b>Heap:</b>";
	private static final String stackHeader = "<html><head><title>Stack</title><style type=\"text/css\">" + "body{background-color: white;}*{font-family: monospace; font-size:10pt;}div.ar{background-color: #FDF1FA; padding: 6px; margin-bottom: 12px; border: 1px solid #bbb;}div.ar_title{font-size: small; color: #669999;}.ar_info, .ar_info td{border: 1px solid #FDF1FA; border-collapse: collapse; padding: 4px;}.ar_vars, .ar_vars td{border: 1px solid #ccc; border-collapse: collapse; padding: 6px;}.ar_info .n, .ar_vars .title td{font-size: 10pt; color: #669999;}.ar_info{font-size: small; border-color: #FDF1FA;}.gr{color: grey; font-size: 8pt;} td.arg { background-color: #d9ffb3; } .collapsibleList li > input + *{display: none;}.collapsibleList li > input:checked + *{display: block;}.collapsibleList{list-style-type: none;}.collapsibleList li > input{display: none;}.collapsibleList label{cursor: pointer; text-decoration: underline;}.fixed{position: fixed; top: 0; left: 6;}.container{width: 100%; margin: auto;}.stack{width: 48%; float: left; overflow-y: auto;}.heap{margin-left: 2%; width: 46%; float: left; padding: 2px; overflow-y: auto;} .heap table { width: 100%; background-color: #FDF1FA; } .clear{clear: both;} b { margin-bottom: 10px; display: block; } </style></head><body><div class=\"container\"><div class=\"stack\"><b>Stack:</b>";
	private static final String htmlFooter = "</div><div class=\"clear\"></div></div></body><script>window.onload=function(){var avatarElem=document.getElementById('ff'); var avatarSourceBottom=avatarElem.getBoundingClientRect().bottom + window.pageYOffset; window.onscroll=function(){if (avatarElem.classList.contains('fixed') && window.pageYOffset < avatarSourceBottom){avatarElem.classList.remove('fixed');}else if (window.pageYOffset > avatarSourceBottom){avatarElem.classList.add('fixed');}};};</script></html>";
	
	
	
	private static final String varsHeader = " <tr>   "
			+ " <td class=\"tg-d67s\">Type</td>"
			+ "<td class=\"tg-d67s\">Name</td>"
			+ "<td class=\"tg-d67s\">Value</td>"
			//+ "<td class=\"tg-d67s\">Changed</td>"
			+ "</tr>";
	
	private static final String varsHeapHeader = " <tr>   "
			+ " <td class=\"tg-d67s\">Type</td>"
			+ "<td class=\"tg-d67s\">Name</td>"
			+ "<td class=\"tg-d67s\">Value</td>"
			//+ "<td class=\"tg-d67s\">Changed</td>"
			+ "<td class=\"tg-d67s\">Fields</td>"
			+ "</tr>";
	
	
	
	
	private static final String tableFooter = "</table>";
	private static final String tableHeader = ""
			+ "<style type=\"text/css\">"
			+ ".tg  {border-collapse:collapse;border-spacing:0;border-color:#aaa;}"
			+ ".tg td{font-family:Verdana, sans-serif;font-size:14px;padding:8px 20px;border-style:solid;border-width:0px;overflow:hidden;word-break:normal;border-color:#aaa;color:#333;background-color:#fff;}"
			+ ".tg th{font-family:Verdana, sans-serif;font-size:14px;padding:8px 20px;border-style:solid;border-width:0px;overflow:hidden;word-break:normal;border-color:#aaa;color:#fff;background-color:#f38630;}"
			+ ".tg .tg-219y{font-family:Verdana, Geneva, sans-serif !important;background-color:#bbdaff;color:#000000;text-align:left;vertical-align:top;font-size:14px}" // change to 14
			+ ".tg .tg-d67s{font-family:Verdana, Geneva, sans-serif !important;background-color:#ecf4ff;color:#000000;text-align:left;vertical-align:top;font-size:14px}"
			+ ".tg .tg-yw4l{font-family:Verdana, Geneva, sans-serif !important;text-align:left;vertical-align:top;font-size:14px}"
			+ "</style>"
			+ "<table class=\"tg\" width=600 style='table-layout:fixed'> "
			+ "<col width=\"200px\" />"
			+ "<col width=\"200px\" />"
			+ "<col width=\"200px\" /> ";
	
	// Template for building HTML string for stack frame title. Arguments: frameNumber, frameName
	private static final String stackFrameHeaderTemplate = ""
			+ "<tr>"
			+ "<th class=\"tg-219y\">%s</th>"
			+ "<th class=\"tg-219y\" colspan=\"2\">%s</th>"
			+ "</tr>";
	
	
	
	
	
	// Template for building HTML string for variables. Arguments: type, name, value
	private static final String varHtmlTemplate =  ""
			+ "<tr>"
			+ "<td class=\"tg-yw4l\">%s</td>"
			+ "<td class=\"tg-yw4l\">%s</td>"
			+ "<td class=\"tg-yw4l\">%s</td>"
			+ "</tr>";	
	
	private static final String varHeapHtmlTemplate = ""
			+ "<tr>"
			+ "<td class=\"tg-yw4l\">%s</td>"
			+ "<td class=\"tg-yw4l\">%s</td>"
			+ "<td class=\"tg-yw4l\">%s</td>"
			+ "<td class=\"tg-yw4l\">%s</td>"
			+ "</tr>";	
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
		String htmlString = stackHtml + heapHtml + htmlFooter;
		return htmlString;
	}
	
	/*
	 * The method build HTML string for the stack
	 */
	private String getStackHtml(StackStrings stack) {
		String stackFramesHtml = getStackFramesHtml(stack);
		//String stackHtml = stackHeader +  getButtonsHtml() + stackFramesHtml;
		String stackHtml = stackHeader +  stackFramesHtml;
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
		String heapHtml = heapHeader + tableHeader + getVarsHtml(vars, Global.HEAP) + tableFooter;
		return heapHtml;
	}

	
	/*
	 * The method build HTML string for variables
	 */
	private String getVarsHtml(ArrayList<Variable> vars, String partOfVizualization) {
		String varsHtml; 
		if (partOfVizualization.equals(Global.HEAP)) {
			varsHtml = varsHeapHeader;
		} else {
			varsHtml = varsHeader;
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
		String fields = var.getFields();
		String hasChanged = var.getHasValueChanged();
		
		if (hasChanged.equals("true")) {
		//TODO	
		}
		
		String varHtml;
		if (partOfVizualization.equals(Global.HEAP)) {
			varHtml = String.format(varHeapHtmlTemplate, type, name, value, fields);
		} else {
			varHtml = String.format(varHtmlTemplate, type, name, value);
		}

		return varHtml;
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
		String stackFrameHtml = tableHeader + frameTitleHtml + varsHtml + tableFooter;
		return stackFrameHtml;
	}

	/*
	 * The method build HTML string for stack frame title: number and name
	 */
	private String getFrameTitleHtml(int number, String frameName) {
		String frameTitleHtml = String.format(stackFrameHeaderTemplate, number, frameName);;
		return frameTitleHtml;
	}
	
		
}
