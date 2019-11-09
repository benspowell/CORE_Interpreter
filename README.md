
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

## Grammar for CORE
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
    <exp> ::= <trm>|<trm>+<exp>|<trm>−<exp>
    <trm> ::= <op> | <op> * <trm>
    <op> ::= <no> | <id> | (<exp>)
    <comp op> ::=!= | == | < | > | <= | >= 
    <id> ::= <let> | <let><id> | <let><no>
    <let>::=A | B | C | ... | X | Y | Z
    <no>::=<digit> | <digit><no>
    <digit>::=0 | 1 | 2 | 3 | ... | 9
    
## User Manual
Setup.

How to use.
## Testing
The testing process for the interpreter was performed manually during the developmental stages of the project. 

## Bugs
There are no known bugs in the program at this point.