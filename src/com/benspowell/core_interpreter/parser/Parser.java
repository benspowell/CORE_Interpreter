package core_interpreter.parser;
import core_interpreter.parser.*;
import core_interpreter.tokenizer.*;
import core_interpreter.error.*;

public class Parser {
	
	/*
	 * The persistent Tokenizer used throughout the class.
	 */
	Tokenizer t;
	ParseTree p;
	
	/*
	 * Parse a CORE program. This is the only public method, as all other methods 
	 * will be recursively called as needed. The method initializes and generates the program's 
	 * parseTree, and throws errors as necessary.
	 * 
	 * Returns the program's complete ParseTree.
	 */
	public ParseTree coreProgram(Tokenizer t) throws ParseException{
		
		//initialize class variables
		this.t = t;
		this.p = new ParseTree();
		
		//set nt
		p.setNT(NonTerminalKind.PROG);
		p.setAltNo(1);
		
		//check for 'program'
		if (t.getTokenKind()!=TokenKind.PROGRAM) throw new ParseException("'program'", t);
		t.skipToken();
		
		//create branches
		p.createLeftBranch();
		p.createMiddleBranch();
		
		//parse decl seq, add to tree
		p.goDownLeftBranch();
		parseDeclSeq();
		p.goUp();
		
		//check for 'begin'
		if (t.getTokenKind()!=TokenKind.BEGIN) throw new ParseException("'begin'", t);
		t.skipToken();
		
		//parse stmt seq, add to tree
		p.goDownMiddleBranch();
		parseStmtSeq();
		p.goUp();
		
		//check for 'end'
		if (t.getTokenKind()!=TokenKind.END) throw new ParseException("'end'", t);
		t.skipToken();
		
		//return the tree
		return p;
	}
	
	//done
	private void parseDeclSeq()  throws ParseException{
		p.setNT(NonTerminalKind.DECL_SEQ);
		
		p.createLeftBranch();
		
		p.goDownLeftBranch();
		parseDecl();
		p.goUp();
		
		if (t.getTokenKind()!=TokenKind.BEGIN) {
			p.setAltNo(2);
			
			p.createMiddleBranch();
			
			p.goDownMiddleBranch();
			parseDeclSeq();
			p.goUp();
		}
		else {
			p.setAltNo(1);
		}
	}
	
	//done
	private void parseStmtSeq() throws ParseException{
		p.setNT(NonTerminalKind.STMT_SEQ);
		
		//assume 1st alternative, will update it later if necessary.
		p.setAltNo(1);
		
		p.createLeftBranch();
		
		p.goDownLeftBranch();
		parseStmt();
		p.goUp();
		
		if (t.getTokenKind()!=TokenKind.END) {
			p.setAltNo(2);
			
			p.createMiddleBranch();
			
			p.goDownMiddleBranch();
			parseStmtSeq();
			p.goUp();
		}
	}
	
	//done
	private void parseDecl() throws ParseException{
		p.setNT(NonTerminalKind.DECL);
		p.setAltNo(1);
		
		if (t.getTokenKind()!=TokenKind.INT) throw new ParseException("'int'", t);
		t.skipToken();
		
		p.createLeftBranch();
		
		p.goDownLeftBranch();
		parseIdList();
		p.goUp();
		
		if (t.getTokenKind()!=TokenKind.SEMICOLON) throw new ParseException("';'", t);
		t.skipToken();
	}
	
	//done
	private void parseIdList() throws ParseException{
		p.setNT(NonTerminalKind.ID_LIST);
		p.setAltNo(1);
		
		p.createLeftBranch();
		
		p.goDownLeftBranch();
		parseId();
		p.goUp();
		
		if (t.getTokenKind()==TokenKind.COMMA) {
			p.setAltNo(2);
			
			p.createMiddleBranch();
			
			p.goDownMiddleBranch();
			parseIdList();
			p.goUp();
		}
	}
	
	//done
	private void parseStmt() throws ParseException{
		p.setNT(NonTerminalKind.STMT);
		
		p.createLeftBranch();
		
		p.goDownLeftBranch();
		
		switch (t.getTokenKind()) {
		case IDENTIFIER:
			p.setAltNo(1);
			parseAss();
			break;
		case IF:
			p.setAltNo(2);
			parseIf();
			break;
		case WHILE:
			p.setAltNo(3);
			parseLoop();
			break;
		case READ:
			p.setAltNo(4);
			parseIn();
			break;
		case WRITE: 
			p.setAltNo(5);
			parseOut();
			break;
		default:
			throw new ParseException("<id>, 'if', 'while', 'read', 'write'", t);
		}
		p.goUp();
	}
	
	//done
	private void parseAss() throws ParseException{
		
		p.setNT(NonTerminalKind.ASS);
		p.setAltNo(1);
		
		p.createLeftBranch();
		p.createMiddleBranch();
		
		p.goDownLeftBranch();
		parseId();
		p.goUp();
		
		if (t.getTokenKind()!=TokenKind.ASSIGNMENT_OPERATOR) throw new ParseException("'='", t);
		t.skipToken();
		
		p.goDownMiddleBranch();
		parseExp();
		p.goUp();
		
		if (t.getTokenKind()!=TokenKind.SEMICOLON) throw new ParseException("';'", t);
		t.skipToken();
	}
	
	//done
	private void parseIf() throws ParseException{
		p.setNT(NonTerminalKind.IF);
		p.setAltNo(1);
		
		if (t.getTokenKind()!=TokenKind.IF) throw new ParseException("'if'", t);
		t.skipToken();
		
		p.createLeftBranch();
		p.createMiddleBranch();
		
		p.goDownLeftBranch();
		parseCond();
		p.goUp();
		
		if (t.getTokenKind()!=TokenKind.THEN) throw new ParseException("'then'", t);
		t.skipToken();
		
		p.goDownMiddleBranch();
		parseStmtSeq();
		p.goUp();
		
		if (t.getTokenKind()==TokenKind.ELSE) {
			p.setAltNo(2);
			
			p.createRightBranch();
			
			p.goDownRightBranch();
			parseStmtSeq();
			p.goUp();
		}
		
		if (t.getTokenKind()!=TokenKind.END) throw new ParseException("'end'", t);
		t.skipToken();
		if (t.getTokenKind()!=TokenKind.SEMICOLON) throw new ParseException("';'", t);
		t.skipToken();
	}
	
	//done
	private void parseLoop() throws ParseException{
		p.setNT(NonTerminalKind.LOOP);
		p.setAltNo(1);
		
		p.createLeftBranch();
		p.createMiddleBranch();
		
		if (t.getTokenKind()!=TokenKind.WHILE) throw new ParseException("'while'", t);
		t.skipToken();
		
		p.goDownLeftBranch();
		parseCond();
		p.goUp();
		
		if (t.getTokenKind()!=TokenKind.LOOP) throw new ParseException("'loop'", t);
		t.skipToken();
		
		p.goDownMiddleBranch();
		parseStmtSeq();
		p.goUp();
		
		if (t.getTokenKind()!=TokenKind.END) throw new ParseException("'end'", t);
		t.skipToken();
	}
	
	//done
	private void parseIn() throws ParseException{
		p.setNT(NonTerminalKind.IN);
		p.setAltNo(1);
		
		if (t.getTokenKind()!=TokenKind.READ) throw new ParseException("'read'", t);
		t.skipToken();
		
		p.createLeftBranch();
		
		p.goDownLeftBranch();
		parseIdList();
		p.goUp();
		
		if (t.getTokenKind()!=TokenKind.SEMICOLON) throw new ParseException("';'", t);
		t.skipToken();
	}
	
	//done
	private void parseOut() throws ParseException{
		p.setNT(NonTerminalKind.OUT);
		p.setAltNo(1);
		
		if (t.getTokenKind()!=TokenKind.WRITE) throw new ParseException("'write'", t);
		t.skipToken();
		
		p.createLeftBranch();
		
		p.goDownLeftBranch();
		parseIdList();
		p.goUp();
		
		if (t.getTokenKind()!=TokenKind.SEMICOLON) throw new ParseException("';'", t);
		t.skipToken();
	}
	
	//done
	private void parseCond() throws ParseException{
		p.setNT(NonTerminalKind.COND);
		
		p.createLeftBranch();
		
		p.goDownLeftBranch();
		
		switch (t.getTokenKind()) {
		case OPEN_PAREN:
			p.setAltNo(1);
			parseComp();
			p.goUp();
			break;
		case BANG:
			p.setAltNo(2);
			t.skipToken();
			parseCond();
			p.goUp();
			break;
		case OPEN_BRACKET:
			t.skipToken();
			parseCond();
			p.goUp();
			
			if (t.getTokenKind()!=TokenKind.AND_OPERATOR) {
				p.setAltNo(3);
				t.skipToken();
			}
			else if (t.getTokenKind()!=TokenKind.OR_OPERATOR) {
				p.setAltNo(4);
				t.skipToken();
			}
			else {
				throw new ParseException("'&&', '|'", t);
			}
			
			p.createMiddleBranch();
			
			p.goDownMiddleBranch();
			parseCond();
			p.goUp();
			
			if (t.getTokenKind()!=TokenKind.CLOSE_BRACKET) throw new ParseException("']'", t);
			t.skipToken();
			
			break;
		default:
			throw new ParseException("'(', '!', '['", t);
		}
		
	}

	//done
	private void parseComp() throws ParseException{
		p.setNT(NonTerminalKind.COMP);
		p.setAltNo(1);
		
		if (t.getTokenKind()!=TokenKind.OPEN_PAREN) throw new ParseException("'('", t);
		t.skipToken();
		
		p.createLeftBranch();
		p.createMiddleBranch();
		p.createRightBranch();
		
		p.goDownLeftBranch();
		parseOp();
		p.goUp();
		
		p.goDownMiddleBranch();
		parseCompOp();
		p.goUp();
		
		p.goDownRightBranch();
		parseOp();
		p.goUp();
		
		if (t.getTokenKind()!=TokenKind.CLOSE_PAREN) throw new ParseException("')'", t);
		t.skipToken();
	}
	
	//done
	private void parseExp() throws ParseException{
		p.setNT(NonTerminalKind.EXP);
		p.setAltNo(1);
		
		p.createLeftBranch();
		
		p.goDownLeftBranch();
		parseTrm();
		p.goUp();
		
		if (t.getTokenKind()==TokenKind.PLUS) {
			t.skipToken();
			p.setAltNo(2);
			p.createMiddleBranch();
			p.goDownMiddleBranch();
			parseExp();
			p.goUp();
		}
		else if (t.getTokenKind()==TokenKind.MINUS) {
			t.skipToken();
			p.setAltNo(3);
			p.createMiddleBranch();
			p.goDownMiddleBranch();
			parseExp();
			p.goUp();
		}
		
	}
	
	//done
	private void parseTrm() throws ParseException{
		p.setNT(NonTerminalKind.TRM);
		p.setAltNo(1);
		
		p.createLeftBranch();
		
		p.goDownLeftBranch();
		parseNo();
		p.goUp();
		
		if (t.getTokenKind()==TokenKind.ASTERISK) {
			t.skipToken();
			p.setAltNo(2);
			p.createMiddleBranch();
			p.goDownMiddleBranch();
			parseTrm();
			p.goUp();
		}
	}
	
	//done
	private void parseOp() throws ParseException{
		p.setNT(NonTerminalKind.OP);
		
		p.createLeftBranch();
		
		switch (t.getTokenKind()) {
		case INTEGER_CONSTANT:
			p.setAltNo(1);
			
			p.goDownLeftBranch();
			parseNo();
			break;
		case IDENTIFIER:
			p.setAltNo(2);
			
			p.goDownLeftBranch();
			parseId();
			break;
		case OPEN_PAREN:
			if (t.getTokenKind()!=TokenKind.OPEN_PAREN) throw new ParseException("'('", t);
			t.skipToken();
			
			p.goDownLeftBranch();

			parseExp();
			
			if (t.getTokenKind()!=TokenKind.CLOSE_PAREN) throw new ParseException("')'", t);
			t.skipToken();
			break;
		default:
			throw new ParseException("<no>, <id>, '('", t);
		}
		
		p.goUp();
	}
	
	//done
	private void parseCompOp() throws ParseException{
		p.setNT(NonTerminalKind.COMP_OP);
		p.createLeftBranch();
		
		switch (t.getTokenKind()) {
		case NOT_EQ_TEST:
			p.setAltNo(1);
			break;
		case EQUALITY_TEST:
			p.setAltNo(2);
			break;
		case LESS_THAN:
			p.setAltNo(3);
			break;
		case GREATER_THAN:
			p.setAltNo(4);
			break;
		case LESS_THAN_OR_EQUAL_TO:
			p.setAltNo(5);
			break;
		case GREATER_THAN_OR_EQUAL_TO:
			p.setAltNo(6);
			break;
		default:
			throw new ParseException("'!=', '==', '<', '>', '<=', '>='", t);
		}
	}
	
	//done
	private void parseId() throws ParseException{
		p.setNT(NonTerminalKind.ID);
		p.setCurrentIdName(t.getTokenVal());
	}
	
	//done
	private void parseNo() throws ParseException{
		p.setNT(NonTerminalKind.NO);
		p.setCurrentIdVal(Integer.parseInt(t.getTokenVal()));
	}
	
}
