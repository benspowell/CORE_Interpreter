package core_interpreter;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

import core_interpreter.error.*;
import core_interpreter.printer.*;
import core_interpreter.tokenizer.*;
import core_interpreter.executer.*;
import core_interpreter.parser.*;

/**
 * Full Interpreter for CORE Programs.
 * 
 * CSE 3341 - Principles of Programming Languages - Project 3
 * 
 * @author Benjamin S. Powell
 */
public class Interpreter {

	public static void main(String [] args) {
		
		boolean iShouldPrettyPrint = args [ args.length - 1 ] == "print";
		
		try {
			
			// Open the program file.
	    	Scanner fileInput = new Scanner ( Paths.get(args[0]) );
	        
	    	// Create the Tokenizer.
	        Tokenizer tokenizer = new Tokenizer ( fileInput );
			
	        // Create the Parser.
	        Parser parser = new Parser ( tokenizer );
	        
	        // Parse the program.
	        ParseTree parseTree = parser.coreProgram();
	        
	        // Create the Printer
	        Printer printer = new Printer ( System.out, parseTree );
	        
	        // Print, if the user wants to.
	        if ( iShouldPrettyPrint ) printer.printCoreProgram();
	        
	        // TODO: Create the Executor.

	        // TODO: Execute the program and print the output.
	        
		}
		catch(IOException e) {
			System.err.println("Error opening file: " + args[0]);
		}
		catch (ParseException e){
			System.err.println(e.getMessage());
		}
	}
}
