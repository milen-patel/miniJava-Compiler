package miniJava.SyntacticAnalyzer;

/*
 * Used to record the character position range of a Token in the source file.
 */
public class TokenPosition {
	private int startPos, endPos;

	public TokenPosition(int startPos, int endPos) {
		this.startPos = startPos;
		this.endPos = endPos;
	}
	
	public int getStartPos() {
		return this.startPos;
	}
	
	public int getEndPos() {
		return this.endPos;
	}
}
