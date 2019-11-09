package core_interpreter.parser;

import java.util.List;

import core_interpreter.tokenizer.*;

/**
 * Parse Tree Class. Creates an abstract data structure of ParseTree, which 
 * can be built and navigated by the Parser.
 * 
 * @author Benjamin S. Powell
 */
public class ParseTree {
	private Node root;
	private Node currentNode;
	
	public class Node {
		private Node parent;
		private List<Node> children;
		
		private NonTerminalKind nt;
        private int alt;
        
        //ONLY FOR <id>'S
        private int idVal;
        private String idName;
    }
	
	/*
	 * Constructor for the ParseTree class.
	 */
	public ParseTree() {
		Node n = new Node();
		n.parent=null;
		
		this.root=n;
		this.currentNode=this.root;
	}
	
	/*
	 * Get the non-terminal at the current node.
	 */
	NonTerminalKind currentNTNo() {
		return this.currentNode.nt;
	}
	
	/*
	 * Get the alternative number used at the current node.
	 */
	int currentAlternative() {
		return this.currentNode.alt;
	}
	
	/*
	 * Move the cursor to the first child.
	 */
	void goDownLeftBranch() {
		this.currentNode=this.currentNode.children.get(0);
	}
	
	/*
	 * Create a left child of the current node.
	 */
	void createLeftBranch() {
		Node n = new Node();
		this.currentNode.children.add(0,n);
	}
	
	/*
	 * Move the cursor to the second child.
	 */
	void goDownRightBranch() {
		this.currentNode=this.currentNode.children.get(2);
	}
	
	/*
	 * Create a left child of the current node.
	 */
	void createRightBranch() {
		Node n = new Node();
		this.currentNode.children.add(2,n);
	}
	
	/*
	 * Move the cursor to the third child.
	 */
	void goDownMiddleBranch() {
		this.currentNode=this.currentNode.children.get(1);
	}
	
	/*
	 * Create a left child of the current node.
	 */
	void createMiddleBranch() {
		Node n = new Node();
		this.currentNode.children.add(1,n);
	} 
	
	/*
	 * Move the cursor to the parent node.
	 */
	void goUp() {
		this.currentNode=this.currentNode.parent;
	}
	
	/*
	 * Get the value of the current node if it's an <id>
	 */
	int getCurrentIdVal() {
		return this.currentNode.idVal;
	}
	
	/*
	 * Set the value of the current node if it's an <id>
	 */
	void setCurrentIdVal(int x) {
		this.currentNode.idVal=x;
	}
	
	/*
	 * Get the name of the current node if it's an <id>
	 */
	String getCurrentIdName() {
		return this.currentNode.idName;
	}
	
	/*
	 * Get the name of the current node if it's an <id>
	 */
	void setCurrentIdName(String x) {
		this.currentNode.idName = x;
	}

	void setNT(NonTerminalKind nt) {
		this.currentNode.nt=nt;
	}

	void setAltNo(int i) {
		this.currentNode.alt=i;
	}
	
	
}