package miniJava.SyntacticAnalyzer;

public class Token {
	private TokenType type;
	private String spelling;
	private SourcePosition position;
	
	public Token(TokenType type, String spelling, int startPos, int endPos) {
		this.type = type;
		this.spelling = spelling;
		this.position = new SourcePosition(startPos, endPos);
	}
	
	public Token(TokenType type, String spelling, SourcePosition tokenPosition) {
		this.type = type;
		this.spelling = spelling;
		this.position = tokenPosition;
	}
	
	public String toString() {
		return "{ Type: " + this.type + ", Spelling: \"" + this.spelling + "\" [" + position.getStartPos() + ", " + position.getEndPos() +"] }";
	}
	
	public TokenType getType() {
		return this.type;
	}
	
	public int getStartPosition() {
		return this.position.getStartPos();
	}
	
	public SourcePosition getPosition() {
		return this.position;
	}
	
	public String getSpelling() {
		return this.spelling;
	}
}
