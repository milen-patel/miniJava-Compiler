package miniJava.SyntacticAnalyzer;

public class Token {
	public TokenKind kind;
	public String spelling;
	public SourcePosition posn;
	
	public Token(TokenKind type, String spelling, int startPos, int endPos, int lineNumber) {
		this.kind = type;
		this.spelling = spelling;
		this.posn = new SourcePosition(startPos, endPos, lineNumber);
	}
	
	public Token(TokenKind type, String spelling, SourcePosition tokenPosition) {
		this.kind = type;
		this.spelling = spelling;
		this.posn = tokenPosition;
	}
	
	public String toString() {
		return "{ Type: " + this.kind + ", Spelling: \"" + this.spelling + " " + posn.toString();
	}
	
	public TokenKind getType() {
		return this.kind;
	}
	
	public int getStartPosition() {
		return this.posn.getStartPos();
	}
	
	public SourcePosition getPosition() {
		return this.posn;
	}
	
	public String getSpelling() {
		return this.spelling;
	}
}
