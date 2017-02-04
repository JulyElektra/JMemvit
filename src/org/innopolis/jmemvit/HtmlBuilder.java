package org.innopolis.jmemvit;

import java.util.ArrayList;

public class HtmlBuilder {
	// Heap and Stack state
	private State state;
	private String htmlSplitter= "";
	private String varSplitter = "";
	private String heapHeader = "</div><div class=\"heap\"><b>Heap:</b>";
	private String stackHeader = "<html><head><title>Stack</title>" + "<style type=\"text/css\">" + "body{background-color: white;}*{font-family: monospace; font-size:10pt;}div.ar{background-color: #FDF1FA; padding: 6px; margin-bottom: 12px; border: 1px solid #bbb;}div.ar_title{font-size: small; color: #669999;}.ar_info, .ar_info td{border: 1px solid #FDF1FA; border-collapse: collapse; padding: 4px;}.ar_vars, .ar_vars td{border: 1px solid #ccc; border-collapse: collapse; padding: 6px;}.ar_info .n, .ar_vars .title td{font-size: 10pt; color: #669999;}.ar_info{font-size: small; border-color: #FDF1FA;}.gr{color: grey; font-size: 8pt;} td.arg { background-color: #d9ffb3; } .collapsibleList li > input + *{display: none;}.collapsibleList li > input:checked + *{display: block;}.collapsibleList{list-style-type: none;}.collapsibleList li > input{display: none;}.collapsibleList label{cursor: pointer; text-decoration: underline;}.fixed{position: fixed; top: 0; left: 6;}.container{width: 100%; margin: auto;}.stack{width: 48%; float: left; overflow-y: auto;}.heap{margin-left: 2%; width: 46%; float: left; padding: 2px; overflow-y: auto;} .heap table { width: 100%; background-color: #FDF1FA; } .clear{clear: both;} b { margin-bottom: 10px; display: block; } </style></head><body><div class=\"container\"><div class=\"stack\"><b>Stack:</b>";
	private String framesSplitter = "";
	private String varDataSplitter = "";
	private String htmlFooter = "</div><div class=\"clear\"></div></div></body><script>window.onload=function(){var avatarElem=document.getElementById('ff'); var avatarSourceBottom=avatarElem.getBoundingClientRect().bottom + window.pageYOffset; window.onscroll=function(){if (avatarElem.classList.contains('fixed') && window.pageYOffset < avatarSourceBottom){avatarElem.classList.remove('fixed');}else if (window.pageYOffset > avatarSourceBottom){avatarElem.classList.add('fixed');}};};</script></html>";
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
		String htmlString = stackHtml + htmlSplitter + heapHtml + htmlFooter;
		return htmlString;
	}
	
	/*
	 * The method build HTML string for the stack
	 */
	private String getStackHtml(StackStrings stack) {
		String stackFramesHtml = getStackFramesHtml(stack);
		String stackHtml = stackHeader + stackFramesHtml;
		return stackHtml;
	}

	/*
	 * The method build HTML string for the heap
	 */
	private String getHeapHtml(HeapStrings heap) {
		ArrayList<Variable> vars = heap.getVariables();
		String heapHtml = heapHeader + getVarsHtml(vars);
		return heapHtml;
	}
	
	/*
	 * The method build HTML string for variables
	 */
	private String getVarsHtml(ArrayList<Variable> vars) {
		String varsHtml = "";
		for (Variable var: vars){
			String varHtml = getVarHtml(var);
			varsHtml = varsHtml + varHtml + varSplitter;
		}
		return varsHtml;
	}

	/*
	 * The method build HTML string for the variable
	 */
	private String getVarHtml(Variable var) {
		String name = var.getName();
		String type = var.getType();
		String value = var.getValue();
		String varHtml = type + varDataSplitter + name + varDataSplitter + value;
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
			stackFramesHtml = stackFramesHtml + stackFrameHtml + framesSplitter;
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
		String varsHtml = getVarsHtml(vars);
		String stackFrameHtml = frameTitleHtml + varsHtml;
		return stackFrameHtml;
	}

	/*
	 * The method build HTML string for stack frame title: number and name
	 */
	private String getFrameTitleHtml(int number, String frameName) {
		String frameTitleHtml = number + " " + frameName;
		return frameTitleHtml;
	}
	
		
}
