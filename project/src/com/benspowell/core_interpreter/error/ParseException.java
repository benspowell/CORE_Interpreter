package com.benspowell.core_interpreter.error;

import com.benspowell.core_interpreter.tokenizer.*;

@SuppressWarnings("serial")
/**
 * Parse Exception.
 * 
 * @author Benjamin S. Powell
 */
public class ParseException extends Exception {
	public ParseException(String expected, Tokenizer t) {
		super ("\nPARSE ERROR: expected token from { "+expected+" }, but got a/an "+t.getTokenKind().toString());
	}	
}
