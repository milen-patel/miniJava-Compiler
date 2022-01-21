package miniJava.SyntacticAnalyzer;

/*
 * Enumeration representing all of the Token Types for the miniJava compiler.
 * Each Token instance has a TokenType property.
 */
public enum TokenType {
	// Reserved Words
	CLASS,
	VOID,
	PUBLIC,
	PRIVATE,
	STATIC,
	INT,
	BOOLEAN,
	THIS,
	RETURN,
	IF,
	WHILE,
	ELSE,

	
	// Syntax
	OPEN_CURLY, // {
	CLOSE_CURLY, // }
	SEMICOLON, // ;
	OPEN_PAREN, // (
	CLOSE_PAREN, // )
	OPEN_BRACKET, // [
	CLOSE_BRACKET, // ]
	COMMA, // /
	DOT, // .
	ASSIGNMENT, // =

	// Unary Operators
	LOGICAL_NEGATION,
	ARITHMETIC_NEGATION,
	
	// Relational Operators
	GREATER_THAN, 
	LESS_THAN,
	DOUBLE_EQUALS,
	LESS_THAN_OR_EQUAL_TO,
	GREATHER_THAN_OR_EQUAL_TO,
	NOT_EQUAL_TO,
	
	// Logical Operators
	LOGICAL_AND,
	LOGICAL_OR,

	// Arithmetic Operators
	ADDITION,
	SUBTRACTION,
	MULTIPLICATION,
	DIVISION,
	
	// Other
	NUMBER_LITERAL,
	IDENTIFIER,
	EOT,
	COMMENT, 
	ERROR,
}
