package miniJava.SyntacticAnalyzer;

/*
 * Used to record the character position range of a Token in the source file.
 */
public class SourcePosition {
	private int startPos, endPos;

	public SourcePosition(int startPos, int endPos) {
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
