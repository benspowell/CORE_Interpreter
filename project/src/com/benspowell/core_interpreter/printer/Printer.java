package com.benspowell.core_interpreter.printer;
import com.benspowell.core_interpreter.parser.ParseTree;

import java.io.*;

public class Printer {
	
	PrettyPrintStream out;
	ParseTree p;
	
	/**
	 * PrintStream Wrapper with a little extra functionality in order to set 
	 * and maintain a given indentation size throughout the printing process.
	 */
	private class PrettyPrintStream{
		private int currentIndentationSize;
		private PrintStream ps;
		private boolean newLine;
		
		public PrettyPrintStream(PrintStream ps) {
			this.ps=ps;
			currentIndentationSize = 0;
			this.newLine=true;
		}
		
		/**
		 * Return an appropriately-sized indentation.
		 */
		private String indentStr(){
			String s = "";
			for (int i = 0;i<(currentIndentationSize*5);i++) {
				s+=" ";
			}
			return s;
		}
		
		/**
		 * Print a string, then a newline
		 */
		public void println(String str) {
			//print the string with an appropriately sized indent in front.
			String prefix = this.newLine? indentStr() : "";
			ps.print(prefix + prettify(str) + "\n");
			
			//record the fact that a new line has been started.
			this.newLine=true;
		}
		
		/**
		 * Print a new line
		 */
		public void println() {
			ps.print("\n");
			//record the fact that a new line has been started.
			this.newLine=true;
		}
		
		/**
		 * Maintain the indentation pattern in this string.
		 */
		private String prettify(String s) {
			//chop off any newline characters from the end of the string
			while (s.charAt(s.length()-1)=='\n') {
				s=s.substring(0, s.length()-1);
			}
			
			//prepend any new lines in the string with an appropriate indent
			return s.replace("\n", "\n"+indentStr());
		}
		
		/**
		 * Print a string
		 */
		public void print(String s) {
			
			//if you should have used println, use it.
			if (s.charAt(s.length()-1)=='\n') {
				println(s);
			}
			
			//print the string, starting with an indent if on a new line
			else {		
				String prefix = this.newLine? indentStr() : "";
				ps.print(prefix + prettify(s));
				this.newLine=false;
			}
		}
		
		/**
		 * Increase the current indentation size.
		 */
		public void increaseIndent() {
			this.currentIndentationSize++;
		}
		
		/**
		 * Decrease the current indentation size.
		 */
		public void decreaseIndent() {
			this.currentIndentationSize--;
		}
	}
	
	/**
	 * Default constructor if no Printstream is specified
	 */
	public Printer(ParseTree p) { 
		this(System.out, p); 
	}
	
	/**
	 * Constructor for the Printer class
	 */
	public Printer(PrintStream out, ParseTree fullyInitializedParseTree) {
		this.out = new PrettyPrintStream(out);
		this.p = fullyInitializedParseTree;
	}
	
	/**
	 * PrettyPrint a CORE program. 
	 * 
	 * This is the only publicly available class, as all other classes 
	 * are called by it as necessary (directly or indirectly).
	 */
	public void printCoreProgram() {
		out.println("program");
		
		out.increaseIndent();
		
		p.goDownLeftBranch();
		printDeclSeq();
		p.goUp();
		
		out.decreaseIndent();
		out.println("begin");
		out.increaseIndent();
		
		p.goDownMiddleBranch();
		printStmtSeq();
		p.goUp();
		
		out.decreaseIndent();
		out.println("end");		
	}
	

	/**
	 * print a declaration sequence.
	 */
	private void printDeclSeq() {
		
		p.goDownLeftBranch();
		printDecl();
		p.goUp();
		
		if (p.currentAlternative()==2) {
			p.goDownMiddleBranch();
			printDeclSeq();
			p.goUp();
		}
	}
	
	/**
	 * print a statement sequence.
	 */
	private void printStmtSeq() {
		
		p.goDownLeftBranch();
		printStmt();
		p.goUp();
		
		if (p.currentAlternative()==2) {
			p.goDownMiddleBranch();
			printStmtSeq();
			p.goUp();
		}
	}
	
	
	
	/**
	 * print a declaration.
	 */
	private void printDecl() {
		out.print("int ");
		
		p.goDownLeftBranch();
		printIdList();
		p.goUp();
		
		out.println(";");
	}
	
	/**
	 * print an ID list.
	 */
	private void printIdList() {
		
		p.goDownLeftBranch();
		printId();
		p.goUp();
		
		if (p.currentAlternative()==2) {
			out.print(", ");
			
			p.goDownMiddleBranch();
			printIdList();
			p.goUp();
		}
		
	}
	
	/**
	 * print a statement.
	 */
	private void printStmt() {
		
		
		switch (p.currentAlternative()) {
		case 1:
			p.goDownLeftBranch();
			printAss();
			break;
		case 2:
			p.goDownLeftBranch();
			printIf();
			break;
		case 3:
			p.goDownLeftBranch();
			printLoop();
			break;
		case 4:
			p.goDownLeftBranch();
			printIn();
			break;
		case 5:
			p.goDownLeftBranch();
			printOut();
			break;
		}
		
		p.goUp();
	}
	
	/**
	 * print an assignment.
	 */
	private void printAss() {
		p.goDownLeftBranch();
		printId();
		p.goUp();
		
		out.print(" = ");
		
		p.goDownMiddleBranch();
		printExp();
		p.goUp();
		
		out.println(";");
	}
	
	/**
	 * Print an if.
	 */
	private void printIf() {
		out.print("if ");
		
		p.goDownLeftBranch();
		printCond();
		p.goUp();
		
		out.println(" then");
		
		out.increaseIndent();
		
		p.goDownMiddleBranch();
		printStmtSeq();
		p.goUp();
		
		out.decreaseIndent();
		
		if (p.currentAlternative()==2) {
			out.println("else");
			
			out.increaseIndent();
			
			p.goDownRightBranch();
			printStmtSeq();
			p.goUp();
			
			out.decreaseIndent();
		}
		
		out.println("end;");
	}
	
	/**
	 * Print a loop.
	 */
	private void printLoop() {
		out.print("while ");
		
		p.goDownLeftBranch();
		printCond();
		p.goUp();
		
		out.println(" loop");
		
		out.increaseIndent();
		
		p.goDownMiddleBranch();
		printStmtSeq();
		p.goUp();
		
		out.decreaseIndent();
		
		out.println("end;");
	}
	
	/**
	 * Print an in statement.
	 */
	private void printIn() {
		out.print("read ");
		
		p.goDownLeftBranch();
		printIdList();
		p.goUp();
		
		out.println(";");
	}
	
	/**
	 * Print an out statement.
	 */
	private void printOut() {
		out.print("write ");
		
		p.goDownLeftBranch();
		printIdList();
		p.goUp();
		
		out.println(";");
	}
	
	/**
	 * Print a condition.
	 */
	private void printCond() {
		p.goDownLeftBranch();
		
		switch (p.currentAlternative()) {
		case 1:
			printComp();
			break;
		case 2:
			out.print("!");
			printCond();
			break;
		case 3:
			out.print("[");
			
			printCond();
			p.goUp();
			
			out.print(" && ");
			
			p.goDownMiddleBranch();
			printCond();
			
			out.print("]");
			break;
		case 4:
			out.print("[");
			
			printCond();
			p.goUp();
			
			out.print(" || ");
			
			p.goDownMiddleBranch();
			printCond();

			out.print("]");
			break;
		}
		
		p.goUp();
	}
	/**
	 * Print a comparison. 
	 */
	private void printComp() {
		out.print("(");
		
		p.goDownLeftBranch();
		printOp();
		p.goUp();
		
		p.goDownMiddleBranch();
		printCompOp();
		p.goUp();
		
		p.goDownRightBranch();
		printOp();
		p.goUp();
		
		out.print(")");
	}
	
	/**
	 * Print an expression.
	 */
	private void printExp() {
		p.goDownLeftBranch();
		printTrm();
		p.goUp();
		
		switch (p.currentAlternative()) {
		case 2:
			out.print(" + ");
			
			p.goDownMiddleBranch();
			printExp();
			p.goUp();
			
			break;
		case 3:
			out.print(" - ");
			
			p.goDownMiddleBranch();
			printExp();
			p.goUp();
			
			break;
		}
	}
	
	/**
	 * Print a term.
	 */
	private void printTrm() {
		p.goDownLeftBranch();
		printOp();
		p.goUp();
		
		if (p.currentAlternative()==2) {
			out.print(" * ");
			
			p.goDownMiddleBranch();
			printTrm();
			p.goUp();
		}
	}
	
	/**
	 * Print an operator.
	 */
	private void printOp() {
		
		switch (p.currentAlternative()) {
		case 1:
			p.goDownLeftBranch();
			printNo();
			break;
		case 2:
			p.goDownLeftBranch();
			printId();
			break;
		case 3: 
			p.goDownLeftBranch();
			out.print("(");
			printExp();
			out.print(")");
		}
		
		p.goUp();
	}
	
	/**
	 * Print a comparison operator. 
	 */
	private void printCompOp() {
		switch(p.currentAlternative()) {
		case 1:
			out.print(" != ");
			break;
		case 2:
			out.print(" == ");
			break;
		case 3: 
			out.print(" < ");
			break;
		case 4:
			out.print(" > ");
			break;
		case 5:
			out.print(" <= ");
			break;
		case 6:
			out.print(" >= ");
			break;
		}
	}
	
	/**
	 * Print an ID. 
	 */
	private void printId() {
		out.print(p.getCurrentIdName());
	}
	
	/**
	 * Print a number. 
	 */
	private void printNo() {
		out.print(Integer.toString(p.getCurrentIntVal()));
	}

	
}
