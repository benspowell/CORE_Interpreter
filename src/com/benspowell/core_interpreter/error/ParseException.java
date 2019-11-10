package core_interpreter.error;
import core_interpreter.tokenizer.*;

@SuppressWarnings("serial")
public class ParseException extends Exception {
	public ParseException(String expected, Tokenizer t) {
		super ("\nPARSE ERROR: expected token from { "+expected+" }, but got a/an "+t.getTokenKind().toString());
	}	
}
