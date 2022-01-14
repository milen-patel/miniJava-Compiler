package miniJava.SyntacticAnalyzer;

public class TokenType {
	// Does 'num' in the grammar limit itself to integers or do we need to parse doubles
	// Note you can have a variable named THIS but not this, thus reserved words are case sensitive
	
	public static final int NUMBER_LITERAL = 0;
	public static final int IDENTIFIER = 0;
	//whitespace
	//reserved words
	public static final int CLASS = 0;
	public static final int VOID = 0;
	public static final int PUBLIC = 0;
	public static final int PRIVATE = 0;
	public static final int STATIC = 0;
	public static final int INT = 0;
	public static final int BOOLEAN = 0;
	public static final int THIS = 0;
	public static final int RETURN = 0;
	public static final int IF = 0;
	public static final int WHILE = 0;
	public static final int ELSE = 0;

	
	//syntax
	public static final int OPEN_CURLY = 0; // {
	public static final int CLOSE_CURLY = 0; // }
	public static final int SEMICOLON = 0;	// ;
	public static final int OPEN_PAREN = 0;	// (
	public static final int CLOSE_PAREN = 0; // )
	public static final int OPEN_BRACKET = 0; // [
	public static final int CLOSE_BRACKET = 0; // ]
	public static final int COMMA = 0; // /
	public static final int DOT = 0; // .
	public static final int ASSIGNMENT = 0; // =

	//unary operators
	MINUS,
	NOT,
	INCREMENT, //++
	DECREMENT, //--
	COMPLEMENT,
	
	//binary operators
	PLUS,
	STAR,
	SLASH,
	PERCENT,
	
	public static final int EOT = 0;

}
