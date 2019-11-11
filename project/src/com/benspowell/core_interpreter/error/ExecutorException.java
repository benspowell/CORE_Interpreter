package com.benspowell.core_interpreter.error;

import com.benspowell.core_interpreter.tokenizer.Tokenizer;

@SuppressWarnings("serial")
/**
 * Parse Exception.
 * 
 * @author Benjamin S. Powell
 */
public class ExecutorException extends Exception {
	public ExecutorException(String msg) {
		super ("\nRUN-TIME ERROR: "+msg);
	}	
}
