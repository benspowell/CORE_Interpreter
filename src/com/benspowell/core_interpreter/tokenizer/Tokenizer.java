package core_interpreter.tokenizer;
import java.util.Scanner;

/**
 * Tokenizer for Core Interpreter project. (Note: by package-wide convention,
 * unless stated otherwise, all references are non-null.)
 *
 * @author Benjamin S. Powell
 *
 * @mathsubtypes <pre>
 * TOKENIZER_MODEL is (
 *   front: string of character,
 *   remainder: string of character)
 *  exemplar tzr
 *  constraint
 *   [tzr.front cannot be extended by the first character of tzr.remainder
 *    to be the prefix of any legal token kind]
 * </pre>
 * @mathdefinitions <pre>
 * AGGREGATE(
 *   ss: string of string of character
 *  ): string of character satisfies
 *  if ss = <> then
 *    AGGREGATE(ss) = <>
 *  else
 *    AGGREGATE(ss) = ss[0,1) * AGGREGATE(ss[1,|ss|))
 * </pre>
 * @mathmodel type Tokenizer is modeled by TOKENIZER_MODEL
 *
 */
public class Tokenizer {
	
	class PeekableScanner {

	    private final Scanner scanner;
	    private String next;

	    public PeekableScanner(Scanner scanner) {
	        this.scanner = scanner;
	        this.next = scanner.hasNext() ? scanner.next() : null;
	    }

	    public boolean hasNext() {
	        return next != null;
	    }

	    public String next() {
	        String current = next;
	        next = scanner.hasNext() ? scanner.next() : null;
	        return current;
	    }
	    
	    public void skip(String s) {
	    	while(!next.isEmpty() && !s.isEmpty() && (peek().charAt(0)==s.charAt(0))) {
	    		next = next.substring(1,next.length());
	    		s = s.substring(1,s.length());
	    	}
	    	if (next.isEmpty()) {
	    		next = scanner.hasNext()? scanner.next() : null;
	    	}
	    }
	    
	    public String peek() {
	        return next;
	    }
	}
	
	PeekableScanner in;
	private TokenKind Top_Token_Kind = null; 
	private String Top_Token = ""; 
	private static boolean isUpperCase(char c) { return "ABCDEFGHIJKLMNOPQRSTUVWXYZ".contains(String.valueOf(c));}
	private static boolean isLowerCase(char c) { return "abcdefghijklmnopqrstuvwxyz".contains(String.valueOf(c));}
	private static boolean isDigit(char c) { return "0123456789".contains(String.valueOf(c));}
	private static boolean isAnotherAcceptableCharacter(char c) { return ";,=![]&|()+-*<>".contains(String.valueOf(c));}
	
	public Tokenizer(Scanner s) {
		in = new PeekableScanner(s);
	}

    /**
     * Java interfaces cannot contain constructors pertaining to the classes
     * that are to implement them; hence, here in this Javadoc comment
     * expectations for constructors are stated. Each class that implements
     * interface Tokenizer is expected to follow the singleton pattern; hence,
     * each such class should have exactly one private constructor. To be a bit
     * more general about it, every constructor for each such class must be
     * private.
     *
     * In Java versions 7 and below, interfaces cannot contain static methods.
     * To remain compatible with these Java versions, this interface presents
     * the expected static methods here in a Javadoc comment.
     *
     * /** If no instance of Tokenizer yet exists, create one; in any case,
     * return a reference to the single instance of the Tokenizer.
     *
     * @param itString
     *            the Iterator<String> from which tokens will be extracted;
     *            Tokenizer expects itString's next() method never to deliver an
     *            empty String or a String containing whitespace.
     * @return the single instance of the Tokenizer
     * @updates itString
     * @ensures <pre>[the reference create returned is the reference to the
     *                newly created and only instance of the class implementing
     *                the Tokenizer interface]  and
     *          there exists content: string of character
     *            (content = AGGREGATE(~#itString.unseen)  and
     *             create.front * create.remainder = content  and
     *             (([no prefix of content is a legal token] and
     *               [create.front is the text of an ERROR token]) or
     *              ([create.front is the text of a legal token kind] and
     *               [create.front cannot be extended by the first character
     *                of create.remainder to be the prefix of any
     *                legal token kind])))
     *       </pre> public static Tokenizer create(Iterator<String> itString)
     *
     */
	private enum State{
		READY_FOR_FIRST_CHAR_OF_NEXT_TOKEN, 
		GATHER_UC, 
		FINISH_ID, 
		GATHER_LC, 
		GATHER_DIGITS, 
		GOT_AN_EQ, 
		GOT_A_BANG, 
		GOT_AN_AND, 
		GOT_A_BAR, 
		GOT_A_GREATER_THAN, 
		GOT_A_LESS_THAN, 
		FINISHED;
	}
    /**
     * /** Return either null or the single instance of the Tokenizer, if it
     * exists.
     *
     * @return either null or the single instance of the Tokenizer, if it exists
     *
     *         public static Tokenizer instance()
     *
     */

    /**
     * Return the kind of the front token. (Restores this.)
     *
     * @return the kind of the front token
     * @ensures getToken = [the kind of token this.front]
     */
    @SuppressWarnings("unused")
	public void getToken() {
    	State currentState = State.READY_FOR_FIRST_CHAR_OF_NEXT_TOKEN; 
    	Top_Token_Kind = null; 
    	Top_Token = ""; 
    	char nextchar = ' ';
    	int pos = 0;

    	while(currentState != State.FINISHED) {
        	if (in.hasNext()) {
        		nextchar = pos >= in.peek().length()? ' ' : in.peek().trim().charAt(pos++);
        		Top_Token += nextchar;
        	}
        	else {
        		Top_Token_Kind = TokenKind.EOF;	
        		currentState = State.FINISHED;
        	}
	    	switch(currentState){
	    	case READY_FOR_FIRST_CHAR_OF_NEXT_TOKEN:
	    		if (isUpperCase(nextchar)) currentState = State.GATHER_UC;
	    		
	    		else if (isLowerCase(nextchar)) currentState = State.GATHER_LC;
	    		
	    		else if (isDigit(nextchar)) currentState = State.GATHER_DIGITS;
	    		
	    		else if (isAnotherAcceptableCharacter(nextchar)) {
	    			switch(nextchar) {
	    				case ';':
	    					Top_Token_Kind = TokenKind.SEMICOLON;
	    					currentState = State.FINISHED;
	    					break;
	    				case '[':
	    					Top_Token_Kind = TokenKind.OPEN_BRACKET;
	    					currentState = State.FINISHED;
	    					break;
	    				case ']':
	    					Top_Token_Kind = TokenKind.CLOSE_BRACKET;
	    					currentState = State.FINISHED;
	    					break;
	    				case ',':
	    					Top_Token_Kind = TokenKind.COMMA;
	    					currentState = State.FINISHED;
	    					break;
	    				case '(':
	    					Top_Token_Kind = TokenKind.OPEN_PAREN;
	    					currentState = State.FINISHED;
	    					break;
	    				case ')':
	    					Top_Token_Kind = TokenKind.CLOSE_PAREN;
	    					currentState = State.FINISHED;
	    					break;
	    				case '+':
	    					Top_Token_Kind = TokenKind.PLUS;
	    					currentState = State.FINISHED;
	    					break;
	    				case '-':
	    					Top_Token_Kind = TokenKind.MINUS;
	    					currentState = State.FINISHED;
	    					break;
	    				case '*':
	    					Top_Token_Kind = TokenKind.ASTERISK;
	    					currentState = State.FINISHED;
	    					break;
	    				case '=':
	    					currentState = State.GOT_AN_EQ;
	    					break;
	    				case '!':
	    					currentState = State.GOT_A_BANG;
	    					break;
	    				case '&':
	    					currentState = State.GOT_AN_AND;
	    					break;
	    				case '|':
	    					currentState = State.GOT_A_BAR;
	    					break;
	    				case '>':
	    					currentState = State.GOT_A_GREATER_THAN;
	    					break;
	    				case '<':
	    					currentState = State.GOT_A_LESS_THAN;
	    					break;
		    			}
	    		}
	    		else {
	    			Top_Token_Kind = TokenKind.ERROR;
	    			currentState = State.FINISHED;
	    		}
	    		break;
	    	case GATHER_UC:
	    		if (isUpperCase(nextchar)) ;
	    		else if(isDigit(nextchar)) currentState=State.FINISH_ID;
	    		else if(isLowerCase(nextchar)) {
	    			Top_Token_Kind = TokenKind.ERROR;
	    			currentState = State.FINISHED;
	    		}
	    		else {
	    	    	Top_Token = Top_Token.substring(0,Top_Token.length()-1);
	    			Top_Token_Kind=TokenKind.IDENTIFIER;
	    			currentState = State.FINISHED;
	    		}
	    		break;
			case FINISH_ID:
	    		if (isDigit(nextchar)) ;
	    		else if(isLowerCase(nextchar)||isUpperCase(nextchar)) {
	    			Top_Token_Kind = TokenKind.ERROR;
	    			currentState = State.FINISHED;
	    		}
	    		else {
	    			Top_Token = Top_Token.substring(0,Top_Token.length()-1);
	    			Top_Token_Kind=TokenKind.IDENTIFIER;
	    			currentState = State.FINISHED;
	    		}
	    		break;
			case GATHER_LC:
	    		if (isLowerCase(nextchar)) ;
	    		else if(isDigit(nextchar)||isUpperCase(nextchar)) {
	    			Top_Token_Kind = TokenKind.ERROR;
	    			currentState = State.FINISHED;
	    		}
	    		else {
	    			Top_Token = Top_Token.substring(0,Top_Token.length()-1);
	    			Top_Token_Kind = null;
	    			currentState = State.FINISHED;
	    		}
	    		break;
			case GATHER_DIGITS:
	    		if (isDigit(nextchar)) ;
	    		else if(isLowerCase(nextchar)||isUpperCase(nextchar)) {
	    			Top_Token_Kind = TokenKind.ERROR;
	    			currentState = State.FINISHED;
	    		}
	    		else {
	    			Top_Token = Top_Token.substring(0,Top_Token.length()-1);
	    			Top_Token_Kind=TokenKind.INTEGER_CONSTANT;
	    			currentState = State.FINISHED;
	    		}
	    		break;
			case GOT_AN_EQ:
				if (nextchar=='=') {
	    			Top_Token_Kind=TokenKind.EQUALITY_TEST;
	    			currentState = State.FINISHED;
				}
				else {
					Top_Token = Top_Token.substring(0,Top_Token.length()-1);
	    			Top_Token_Kind=TokenKind.ASSIGNMENT_OPERATOR;
	    			currentState = State.FINISHED;
				}
				break;
			case GOT_A_BANG:
				if (nextchar=='=') {
	    			Top_Token_Kind=TokenKind.NOT_EQ_TEST;
	    			currentState = State.FINISHED;
				}
				else {
					Top_Token = Top_Token.substring(0,Top_Token.length()-1);
	    			Top_Token_Kind=TokenKind.BANG;
	    			currentState = State.FINISHED;
				}
				break;
			case GOT_A_GREATER_THAN:
				if (nextchar=='=') {
					Top_Token_Kind=TokenKind.GREATER_THAN_OR_EQUAL_TO;
					currentState = State.FINISHED;
				}
				else {
					Top_Token = Top_Token.substring(0,Top_Token.length()-1);
					Top_Token_Kind=TokenKind.GREATER_THAN;
					currentState = State.FINISHED;
				}
				break;
			case GOT_A_LESS_THAN:
				if (nextchar=='=') {
					Top_Token_Kind=TokenKind.LESS_THAN_OR_EQUAL_TO;
					currentState = State.FINISHED;
				}
				else {
					Top_Token = Top_Token.substring(0,Top_Token.length()-1);
					Top_Token_Kind=TokenKind.LESS_THAN;
					currentState = State.FINISHED;
				}
				break;
			case GOT_AN_AND:
				if (nextchar=='&') {
	    			Top_Token_Kind=TokenKind.AND_OPERATOR;
	    			currentState = State.FINISHED;
				}
				else {
	    			Top_Token_Kind=TokenKind.ERROR;
	    			currentState = State.FINISHED;
				}
				break;
			case GOT_A_BAR:
				if (nextchar=='|') {
	    			Top_Token_Kind=TokenKind.OR_OPERATOR;
	    			currentState = State.FINISHED;
				}
				else {
	    			Top_Token_Kind=TokenKind.ERROR;
	    			currentState = State.FINISHED;
				}
				break;
			default:
				break;
	    	}
    	}
    	

    	
    	if (Top_Token_Kind==null) {
    		switch(Top_Token) {
    		case "program":
    			Top_Token_Kind=TokenKind.PROGRAM;
    			break;
    		case "begin":
    			Top_Token_Kind=TokenKind.BEGIN;
    			break;
    		case "end":
    			Top_Token_Kind=TokenKind.END;
    			break;
    		case "int":
    			Top_Token_Kind=TokenKind.INT;
    			break;
    		case "if":
    			Top_Token_Kind=TokenKind.IF;
    			break;
    		case "then":
    			Top_Token_Kind=TokenKind.THEN;
    			break;
    		case "else":
    			Top_Token_Kind=TokenKind.ELSE;
    			break;
    		case "while":
    			Top_Token_Kind=TokenKind.WHILE;
    			break;
    		case "loop":
    			Top_Token_Kind=TokenKind.LOOP;
    			break;
    		case "read":
    			Top_Token_Kind=TokenKind.READ;
    			break;
    		case "write":
    			Top_Token_Kind=TokenKind.WRITE;
    			break;
    		default:
    			Top_Token_Kind=TokenKind.ERROR;
    			break;
    		}
    	}
    }
    
    /**
     * Skip front token.
     *
     * @updates this
     * @ensures <pre>(if [the token kind of #this.front is good and legal]
     *                  then this.front * this.remainder = #this.remainder)  or
     *          ([the token kind of #this.front is EOF] and
     *          this = #this)</pre>
     */
    public void skipToken() {
    	getToken();
    	in.skip(Top_Token);
    }

    public TokenKind getTokenKind() {
    	this.getToken();
    	return this.Top_Token_Kind;
    }
    
    public String getTokenVal() {
    	this.getToken();
    	return this.Top_Token;
    }
    
    /**
     * Return the integer value of the front INTEGER_CONSTANT token. (Restores
     * this.)
     *
     * @return the integer value of the front INTEGER_CONSTANT token
     * @requires [the kind of this.front is INTEGER_CONSTANT]
     * @ensures intVal = [the integer value of this.front]
     */
//    int intVal() {
//    	
//    }

    /**
     * Return the name of the front IDENTIFIER token. (Restores this.)
     *
     * @return the name of the front IDENTIFIER token
     * @requires [the kind of this.front is IDENTIFIER]
     * @ensures intVal = this.front
     */
//    String idName() {
//    	
//    }
}
