package edu.c3341;

/**
 * Token kinds needed for Part 1 of the Core Interpreter project.
 *
 * @author Wayne D, Heym
 * @author Powell, Ben
 *
 */
enum TokenKind {

    /**
     * Test driver's token number = 1. Token is program.
     */
	PROGRAM(1),
	
    /**
     * Test driver's token number = 2. Token is begin.
     */
	BEGIN(2),
    
	/**
     * Test driver's token number = 3. Token is end.
     */
	END(3),
	
    /**
     * Test driver's token number = 4. Token is int.
     */
	INT(4),
	
    /**
     * Test driver's token number = 5. Token is if.
     */
	IF(5),
	
    /**
     * Test driver's token number = 6. Token is then.
     */
	THEN(6),
	
    /**
     * Test driver's token number = 7. Token is else.
     */
	ELSE(7),
	
    /**
     * Test driver's token number = 8. Token is while.
     */
	WHILE(8),
	
    /**
     * Test driver's token number = 9. Token is loop.
     */
	LOOP(9),
	
    /**
     * Test driver's token number = 10. Token is read.
     */
	READ(10),
	
    /**
     * Test driver's token number = 11. Token is write.
     */
	WRITE(11),

    /**
     * Test driver's token number = 12; token is ;.
     */
    SEMICOLON(12),

    /**
     * Test driver's token number = 13; token is ,.
     */
    COMMA(13),
    
    /**
     * Test driver's token number = 14; token is =.
     */
    ASSIGNMENT_OPERATOR(14),

    /**
     * Test driver's token number = 15; token is !.
     */
    BANG(15),
    
    /**
     * Test driver's token number = 16; token is [.
     */
    OPEN_BRACKET(16),
    
    /**
     * Test driver's token number = 17; token is ].
     */
    CLOSE_BRACKET(17),
    
    /**
     * Test driver's token number = 18; token is &&.
     */
    AND_OPERATOR(18),
    
    /**
     * Test driver's token number = 19; token is ||.
     */
    OR_OPERATOR(19),

    /**
     * Test driver's token number = 20; token is (.
     */
    OPEN_PAREN(20),

    /**
     * Test driver's token number = 21; token is ).
     */
    CLOSE_PAREN(21),
    
    /**
     * Test driver's token number = 22; token is +.
     */
    PLUS(22),
    
    /**
     * Test driver's token number = 23; token is -.
     */
    MINUS(23),
    
    /**
     * Test driver's token number = 24; token is *.
     */
    ASTERISK(24),
    
    /**
     * Test driver's token number = 25; token is !=.
     */
    NOT_EQ_TEST(25),
    
    /**
     * Test driver's token number = 26; token is ==.
     */
    EQUALITY_TEST(26),

    /**
     * Test driver's token number = 27; token is >.
     */
    GREATER_THAN(27),
    
    /**
     * Test driver's token number = 28; token is <.
     */
    LESS_THAN(28),
    
    /**
     * Test driver's token number = 29; token is <=.
     */
    GREATER_THAN_OR_EQUAL_TO(29),
    
    /**
     * Test driver's token number = 30; token is >=.
     */
    LESS_THAN_OR_EQUAL_TO(30),
    
    /**
     * Test driver's token number = 31.
     */
    INTEGER_CONSTANT(31),

    /**
     * Test driver's token number = 32.
     */
    IDENTIFIER(32),

    /**
     * Test driver's token number = 33.
     */
    EOF(33),

    /**
     * Test driver's token number = 34.
     */
    ERROR(34);

    /**
     * Test driver's token number.
     */
    private int testDriverTokenNumber;

    /**
     * Constructor.
     *
     * @param number
     *            the test driver's token number
     */
    private TokenKind(int number) {
        this.testDriverTokenNumber = number;
    }

    /**
     * Return test driver's token number.
     *
     * @return test driver's token number
     */
    public int testDriverTokenNumber() {
        return this.testDriverTokenNumber;
    }
}
