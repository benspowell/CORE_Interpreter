package com.benspowell.core_interpreter;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

import com.benspowell.core_interpreter.error.*;
import com.benspowell.core_interpreter.executor.*;
import com.benspowell.core_interpreter.printer.*;
import com.benspowell.core_interpreter.tokenizer.*;
import com.benspowell.core_interpreter.parser.*;

/**
 * Full Interpreter for CORE Programs.
 * 
 * CSE 3341 - Principles of Programming Languages - Project 3
 * 
 * @author Benjamin S. Powell
 */
public class Interpreter {

	public static void main(String [] args) {
		
		
		boolean iShouldPrettyPrint = args[args.length-1].equals("print");
		
		try {
			
			// Open the program file.
	    	Scanner programInput = new Scanner ( Paths.get(args[0]) );
	        
			// Open the input file.
	    	Scanner fileInput = new Scanner ( Paths.get(args[1]) );
	    	
	    	// Create the Tokenizer.
	        Tokenizer tokenizer = new Tokenizer ( programInput );
			
	        // Create the Parser.
	        Parser parser = new Parser ( tokenizer );
	        
	        // Parse the program.
	        ParseTree parseTree = parser.coreProgram();
	        
	        // Create the Printer
	        Printer printer = new Printer ( System.out, parseTree );
	        
	        // Print, if the user wants to.
	        if ( iShouldPrettyPrint ) printer.printCoreProgram();
	        
	        // Create the Executor.
	        Executor executor = new Executor (System.out, parseTree, fileInput );
	        
	        // Execute the program.
	        executor.executeCoreProgram();
	        
		}
		catch(IOException e) {
			
			System.err.println("Error opening file: " + e.getMessage());
			
		}
		catch (ParseException e){
			
			System.err.println(e.getMessage());
			e.printStackTrace();
			
		} catch (ExecutorException e) {
			
			System.err.println(e.getMessage());
			e.printStackTrace();
			
		}
	}
}
