package miniJava.SyntacticAnalyzer;

public class Token {
	private TokenType type;
	private String spelling;
	private int startPos, endPos;
	
	public Token(TokenType type, String spelling, int startPos, int endPos) {
		this.type = type;
		this.spelling = spelling;
		this.startPos = startPos;
		this.endPos = endPos;
	}
}
