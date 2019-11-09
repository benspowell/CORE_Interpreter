package edu.c3341;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Tokenizer for Core Interpreter project. (Note: by package-wide convention,
 * unless stated otherwise, all references are non-null.)
 *
 * @author Wayne D. Heym
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
	public Tokenizer(Scanner in) {
		
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
    TokenKind getToken() {
    	
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
    void skipToken() {
    	
    }

    /*
     * For Part 1 of the Core Interpreter project, the following two methods
     * need not be implemented.
     */

    /**
     * Return the integer value of the front INTEGER_CONSTANT token. (Restores
     * this.)
     *
     * @return the integer value of the front INTEGER_CONSTANT token
     * @requires [the kind of this.front is INTEGER_CONSTANT]
     * @ensures intVal = [the integer value of this.front]
     */
    int intVal() {
    	
    }

    /**
     * Return the name of the front IDENTIFIER token. (Restores this.)
     *
     * @return the name of the front IDENTIFIER token
     * @requires [the kind of this.front is IDENTIFIER]
     * @ensures intVal = this.front
     */
    String idName() {
    	
    }
}
