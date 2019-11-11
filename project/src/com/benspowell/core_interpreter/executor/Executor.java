package com.benspowell.core_interpreter.executor;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.benspowell.core_interpreter.error.ExecutorException;
import com.benspowell.core_interpreter.parser.ParseTree;

public class Executor {
	
	private ParseTree p;
	private PrintStream out;
	private Map<String,RuntimeVariable> runtimeVariables;
	private Scanner inputFile;
	
	private class RuntimeVariable {
		private int val;
		private boolean defined;
		
		public RuntimeVariable() {
			this.defined=false;
		}
		public RuntimeVariable(int x) {
			this.val=x;
			this.defined=true;
		}
		public boolean defined() {
			return this.defined;
		}
		public int get() {
			return this.val;
		}
		public void set(int x) {
			this.val=x;
			this.defined=true;
		}
		
	}
	
	/**
	 * Default constructor if no PrintStream is specified
	 */
	public Executor(ParseTree p, Scanner i) { 
		this(System.out, p, i); 
	}
	
	/**
	 * Constructor for the Executor class
	 */
	public Executor(PrintStream out, ParseTree fullyInitializedParseTree, Scanner inputFile) {
		this.out = out;
		this.p = fullyInitializedParseTree;
		p.goAllTheWayBackUp();
		this.inputFile = inputFile;
		this.runtimeVariables = new HashMap<String,RuntimeVariable>();
	}
	
	/**
	 * Execute a CORE program. 
	 * 
	 * This is the only publicly available class, as all other classes 
	 * are called by it as necessary (directly or indirectly).
	 * 
	 * @throws ExecutorException 
	 */
	public void executeCoreProgram() throws ExecutorException {
		p.goDownLeftBranch();
		executeDeclSeq();
		p.goUp();
		
		p.goDownMiddleBranch();
		executeStmtSeq();
		p.goUp();
	}
	
	/**
	 * Execute a declaration sequence.
	 * @throws ExecutorException 
	 */
	private void executeDeclSeq() throws ExecutorException {
		p.goDownLeftBranch();
		executeDecl();
		p.goUp();
		if (p.currentAlternative()==2) {
			p.goDownMiddleBranch();
			executeDeclSeq();
			p.goUp();
		}
	}
	
	/**
	 * Execute a statement sequence.
	 * @throws ExecutorException 
	 */
	private void executeStmtSeq() throws ExecutorException {
		p.goDownLeftBranch();
		executeStmt();
		p.goUp();
		
		if (p.currentAlternative() == 2) {
			p.goDownMiddleBranch();
			executeStmtSeq();
			p.goUp();
		}
	}
	
	
	
	/**
	 * Execute a declaration.
	 * @throws ExecutorException 
	 */
	private void executeDecl() throws ExecutorException {
		p.goDownLeftBranch();
		List<String> list = executeIdList();
		p.goUp();
		
		for (String id : list) {
			if (runtimeVariables.containsKey(id)) throw new ExecutorException("variable"+id+"has already been declared!");
			
			runtimeVariables.put(id, new RuntimeVariable() );
		}
	}
	
	/**
	 * Execute an ID list.
	 */
	private List<String> executeIdList() {
		List<String> list = new ArrayList<String>();
		
		p.goDownLeftBranch();
		list.add(executeId());
		p.goUp();
		
		if (p.currentAlternative()==2) {
			p.goDownMiddleBranch();
			list.addAll(executeIdList());
			p.goUp();
		}
		
		return list;
	}
	
	/**
	 * Execute a statement.
	 * @throws ExecutorException 
	 */
	private void executeStmt() throws ExecutorException {
		switch(p.currentAlternative()) {
		case 1:
			p.goDownLeftBranch();
			executeAss();
			p.goUp();
			break;
		case 2:
			p.goDownLeftBranch();
			executeIf();
			p.goUp();
			break;
		case 3:
			p.goDownLeftBranch();
			executeLoop();
			p.goUp();
			break;
		case 4:
			p.goDownLeftBranch();
			executeIn();
			p.goUp();
			break;
		case 5:
			p.goDownLeftBranch();
			executeOut();
			p.goUp();
			break;
		}
	}
	
	/**
	 * Execute an assignment.
	 * @throws ExecutorException 
	 */
	private void executeAss() throws ExecutorException {
		
		p.goDownLeftBranch();
		String id = p.getCurrentIdName();
		p.goUp();
		
		p.goDownMiddleBranch();
		int val = executeExp(); 
		p.goUp();
		
		if (id == null) throw new ExecutorException("no ID to assign to!");
		if (!runtimeVariables.containsKey(id)) throw new ExecutorException("ID ' " + id + " ' has not been declared!");
		
		runtimeVariables.get(id).set(val);
	}
	
	/**
	 * Execute an if.
	 * @throws ExecutorException 
	 */
	private void executeIf() throws ExecutorException {
		p.goDownLeftBranch();
		boolean cond = executeCond();
		p.goUp();
		
		if (cond) {
			p.goDownMiddleBranch();
			executeStmtSeq();
			p.goUp();
		}
		else if (p.currentAlternative()==2) {
			p.goDownRightBranch();
			executeStmtSeq();
			p.goUp();
		}
	}
	
	/**
	 * Execute a loop.
	 * @throws ExecutorException 
	 */
	private void executeLoop() throws ExecutorException {
		p.goDownLeftBranch();
		boolean cond = executeCond();
		p.goUp();
		
		while (cond) {
			p.goDownMiddleBranch();
			executeStmtSeq();
			p.goUp();
			
			p.goDownLeftBranch();
			cond = executeCond();
			p.goUp();
		}
	}
	
	/**
	 * Execute an in statement.
	 * @throws ExecutorException 
	 */
	private void executeIn() throws ExecutorException {
		p.goDownLeftBranch();
		List<String> list = executeIdList();
		p.goUp();
		
		for (String id : list) {
			if (!inputFile.hasNextInt()) throw new ExecutorException("input file is out of int's to read!");
			if (!runtimeVariables.containsKey(id)) throw new ExecutorException("can't assign value to " + id + " - it's undeclared!");
			runtimeVariables.get(id).set(inputFile.nextInt());
		}
	}
	
	/**
	 * Execute an out statement.
	 * @throws ExecutorException 
	 */
	private void executeOut() throws ExecutorException {
		p.goDownLeftBranch();
		List<String> list = executeIdList();
		p.goUp();
		
		for (String id : list) {
			if (!runtimeVariables.containsKey(id)) throw new ExecutorException("can't write value of " + id + " - it's undeclared!");
			if (!runtimeVariables.get(id).defined()) throw new ExecutorException("can't write value of " + id + " - it's undefined!");
			out.println(id+" = "+runtimeVariables.get(id).get());
		}
	}
	
	/**
	 * Execute a condition.
	 * @throws ExecutorException 
	 */
	private boolean executeCond() throws ExecutorException {
		boolean result = false, c1,c2;
		
		switch (p.currentAlternative()) {
		case 1:
			p.goDownLeftBranch();
			result = executeComp();
			p.goUp();
			break;
		case 2:
			p.goDownLeftBranch();
			result = !executeCond();
			p.goUp();
			break;
		case 3:
			p.goDownLeftBranch();
			c1 = executeCond();
			p.goUp();
			
			p.goDownMiddleBranch();
			c2 = executeCond();
			p.goUp();
			
			result = c1 && c2;
			
			break;
		case 4:
			p.goDownLeftBranch();
			c1 = executeCond();
			p.goUp();
			
			p.goDownMiddleBranch();
			c2 = executeCond();
			p.goUp();
			
			result = c1 || c2;
			
			break;
		}
		
		return result;
	
	}
	/**
	 * Execute a comparison. 
	 * @throws ExecutorException 
	 */
	private boolean executeComp() throws ExecutorException {
		boolean result = false;
		
		p.goDownLeftBranch();
		int op1 = executeOp();
		p.goUp();
		
		p.goDownRightBranch();
		int op2 = executeOp();
		p.goUp();
		
		p.goDownMiddleBranch();
		
		switch (p.currentAlternative()) {
		case 1:
			result = op1 != op2;
			break;
		case 2:
			result = op1 == op2;
			break;
		case 3:
			result = op1 < op2;
			break;
		case 4:
			result = op1 > op2;
			break;
		case 5:
			result = op1 <= op2;
			break;
		case 6:
			result = op1 >= op2;
			break;
		}
		
		p.goUp();
		return result;
	
	}
	
	/**
	 * Execute an expression.
	 * @return value of the expression
	 * @throws ExecutorException 
	 */
	private int executeExp() throws ExecutorException {
		p.goDownLeftBranch();
		int result = executeTrm();
		p.goUp();
		
		switch (p.currentAlternative()){
		case 2:
			p.goDownMiddleBranch();
			result += executeExp();
			p.goUp();
			break;
		case 3:
			p.goDownMiddleBranch();
			result -= executeExp();
			p.goUp();
			break;
		}
		return result;
	}
	
	/**
	 * Execute a term.
	 * @throws ExecutorException 
	 */
	private int executeTrm() throws ExecutorException {
		p.goDownLeftBranch();
		int result = executeOp();
		p.goUp();
		
		if (p.currentAlternative()==2) {
			p.goDownMiddleBranch();
			result *= executeTrm();
			p.goUp();
		}
		
		return result;
	}
	
	/**
	 * Execute an operator.
	 * @throws ExecutorException 
	 */
	private int executeOp() throws ExecutorException {
		int result = 0;
		String id;
		
		switch(p.currentAlternative()) {
		case 1:
			p.goDownLeftBranch();
			result=executeNo();
			p.goUp();
			break;
		case 2:
			p.goDownLeftBranch();
			id = executeId();
			p.goUp();
			if (!runtimeVariables.containsKey(id)) throw new ExecutorException(id+" is undeclared!");
			if (!runtimeVariables.get(id).defined()) throw new ExecutorException(id+" is undefined!");
			result = runtimeVariables.get(id).get();
			break;
		case 3:
			p.goDownLeftBranch();
			result = executeExp();
			p.goUp();
			break;
		}
		
		return result;
	}
	
	/**
	 * Execute an ID. 
	 */
	private String executeId() {
		return p.getCurrentIdName();
	}
	
	/**
	 * Execute a number. 
	 */
	private int executeNo() {
		return p.getCurrentIntVal();
	}
}
