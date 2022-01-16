package miniJava.SyntacticAnalyzer;

public enum TokenType {
	// Does 'num' in the grammar limit itself to integers or do we need to parse doubles
	// Note you can have a variable named THIS but not this, thus reserved words are case sensitive
	NUMBER_LITERAL,
	IDENTIFIER,
	EOT,
	
	//reserved words
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
	
	//syntax
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

	//unary operators
	LOGICAL_NEGATION,
	ARITHMETIC_NEGATION,
	
	// relational operators
	GREATER_THAN, 
	LESS_THAN,
	DOUBLE_EQUALS,
	LESS_THAN_OR_EQUAL_TO,
	GREATHER_THAN_OR_EQUAL_TO,
	NOT_EQUAL_TO,
	
	// logical operators
	LOGICAL_AND,
	LOGICAL_OR,

	// arithmetic operators
	ADDITION,
	SUBTRACTION,
	MULTIPLICATION,
	DIVISION,
	
	COMMENT,
}
