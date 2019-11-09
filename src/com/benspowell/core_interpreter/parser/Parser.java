package core_interpreter.parser;
import core_interpreter.parser.*;
import core_interpreter.tokenizer.*;
import java.io.IOException;

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
	public ParseTree coreProgram(Tokenizer t) throws IOException{
		this.t = t;
		this.p = new ParseTree();
		
		return null;
	}
	private void parseDeclSeq()  throws IOException{
		
	}
	private void parseStmtSeq       () throws IOException{
		
	}
	private void parseDecl          () throws IOException{
		
	}
	private void parseIdList        () throws IOException{
		
	}
	private void parseStmt          () throws IOException{
		
	}
	private void parseAss () throws IOException{
		
		p.setNT(NonTerminalKind.ASS);
		p.setAltNo(1);
		
		p.createLeftBranch();
		p.createMiddleBranch();
		
		p.goDownLeftBranch();
		parseId();
		p.goUp();
		
		p.goDownMiddleBranch();
		parseExp();
		
		
	}
	private void parseIf() throws IOException{
		
	}
	private void parseLoop() throws IOException{
		
	}
	private void parseIn() throws IOException{
		
	}
	private void parseOut() throws IOException{
		
	}
	private void parseCond() throws IOException{
		
	}
	private void parseExp() throws IOException{
		
	}
	private void parseTrm() throws IOException{
		
	}
	private void parseOp() throws IOException{
		
	}
	private void parseCompOp() throws IOException{
		
	}
	private void parseId() throws IOException{
		
	}
	private void parseLet() throws IOException{
		
	}
	private void parseNo() throws IOException{
		
	}
	private void parseDigit() throws IOException{
		
	}
}
