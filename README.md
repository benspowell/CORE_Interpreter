
# CORE Language Interpreter

## Overview
CORE programming language Interpreter, consisting of a Tokenizer, Parser, Printer, and Executor. Written by Benjamin S. Powell for the Principles of Programming Languages Class at the Ohio State University (CSE 3341).

Definition of a Software Interpreter (Dr. Wayne Heym):
> A program whose job at run-time is to manage a process governed by an input program, ushering that process through its states at run-time.

The interpreter consists of the following components:
- **Tokenizer:** Inputs Core program, produces stream of *tokens*.
- **Parser:** Consumes stream of tokens, produces the *abstract parse tree* (PT).
- **Printer:** Given PT, prints the original prog. in a pretty format. Used optionally, by user’s choice. 
- **Executor:** Given PT (and input data), executes the program.
- **Full Interpreter**: `Tokenizer` -> `Parser` -> `Printer` -> `Executor` 

The Tokenizer simulates an FSA (finite state automaton).
The Parser, Printer, and Executor are written using a recursive descent (syntax-directed) approach.
The Parser uses a single, monolithic parse-tree object, which is an instance of the ParseTree class.

## User Manual
**To compile the project:**

  - Unzip folder or clone repository:
  <br>`$ unzip powell907_CORE_Interpreter.zip -d CORE_Interpreter`<br>
  or
  <br>`$ git clone https://github.com/benspowell/CORE_Interpreter.git`<br>
 - Move into project folder:
  <br>`$ cd CORE_Interpreter/project`<br>
- Compile the project and place the compiled output in the bin folder:
  <br>`$ javac -d bin src/com/benspowell/core_interpreter/*.java -cp src`<br>


**To run the compiled program:**

First navigate to bin: `$ cd CORE_Interpreter/project/bin`

Run the compiled output:

    $ java src/com/benspowell/core_interpreter/Interpreter program [print|doNotPrint]

Where `program` is the location of the CORE program you are running, and `[print|doNotPrint]` is either `print` or `doNotPrint`, depending on your preference for a pretty-printed version of the program to the console.

## Details 

### Grammar for CORE
The context-free grammar for CORE as defined in class, in BNF (Backus–Naur Form):

    <prog> ::= program <decl seq> begin <stmt seq> end
    <decl seq> ::= <decl> | <decl> <decl seq> 
    <stmt seq> ::= <stmt> | <stmt> <stmt seq>
    <decl>::= int <id list>;
    <id list> ::= <id> | <id>, <id list>
    <stmt> ::= <assign>|<if>|<loop>|<in>|<out>
    <assign> ::=<id> = <exp>;
    <if> ::= if <cond> then <stmt seq> end; | if <cond> then <stmt seq> else <stmt seq> end;
    <loop> ::= while <cond> loop <stmt seq> end;
    <in> ::= read <id list>;
    <out> ::= write <id list>;
    <cond> ::= <comp>|!<cond> | [<cond> && <cond>] | [<cond> or <cond>]
    <comp> ::= (<op> <comp op> <op>)
    <exp> ::= <trm>|<trm>+<exp>|<trm>−<exp>
    <trm> ::= <op> | <op> * <trm>
    <op> ::= <no> | <id> | (<exp>)
    <comp op> ::=!= | == | < | > | <= | >= 
    <id> ::= <let> | <let><id> | <let><no>
    <let>::=A | B | C | ... | X | Y | Z
    <no>::=<digit> | <digit><no>
    <digit>::=0 | 1 | 2 | 3 | ... | 9
    



### Tokenizer
Converting a program written in CORE to tokens and TokenKinds. The Core language consists of the following 33 legal tokens:

-   **Reserved words (11):**  
    program, begin, end, int, if, then, else, while, loop, read, write
-   **Special symbols (19):**  
    ; , = ! [ ] && || ( ) + - * != == < > <= >=
-   **Integers.** (unsigned, possibly with leading zeros)
-   **Identifiers:** start with uppercase letter, followed by zero or more uppercase letters and ending with zero or more digits.

These tokens are numbered 1 through 11 for the reserved words, 12 through 30 for the special symbols, 31 for integer, and 32 for identifier. One other useful token is the EOF token (for end-of-file); that is token number 33.

Public methods available from the Tokenizer class:

| Return Type | Method & Description |
|--|--|
| TokenKind | `getTokenKind()` <br> Peek at the TokenKind of the current top token.|
| String | `getTokenVal()` <br> Peek at the value of the current top token.|
| void | `skipToken()` <br> Skips the top token.|


Files included in the Tokenizer: 
-   Tokenizer.java - Contains the Tokenizer class which has public methods skipToken, getTokenKind, getTokenVal
-   TokenizerTest.java - Contains main method. used to run tests on Tokenizer
-   TokenKind.java - Contains TokenKind inormation used by other classes to differentiate tokens.

### Parser
The parser takes tokens in order from the Tokenizer and uses them to build a ParseTree structure for the CORE program. 

Files included in the Parser:
- Parser.java - 
- ParseTree.java - 
- NonTerminalKind.java - 

### Printer
The Printer navigates a fully-built ParseTree of a CORE program and prettyPrints it to the console.

### Executor 


## Testing
The testing process for the interpreter was performed manually during the developmental stages of the project. 

## Bugs
There are no known bugs in the program at this point.
