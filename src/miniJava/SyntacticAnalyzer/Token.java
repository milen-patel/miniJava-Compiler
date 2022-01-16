package miniJava.SyntacticAnalyzer;

public class Token {
	private TokenType type;
	private String spelling;
	private TokenPosition position;
	
	public Token(TokenType type, String spelling, int startPos, int endPos) {
		this.type = type;
		this.spelling = spelling;
		this.position = new TokenPosition(startPos, endPos);
	}
	
	public String toString() {
		return "{ Type: " + this.type + ", Spelling: \"" + this.spelling + "\" [" + position.getStartPos() + ", " + position.getEndPos() +"] }";
	}
	
	public TokenType getType() {
		return this.type;
	}
}
