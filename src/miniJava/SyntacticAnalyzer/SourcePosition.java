package miniJava.SyntacticAnalyzer;

/*
 * Used to record the character position range of a Token in the source file.
 */
public class SourcePosition {
	private int startPos, endPos, lineNumber;

	public SourcePosition(int startPos, int endPos, int lineNumber) {
		this.startPos = startPos;
		this.endPos = endPos;
		this.lineNumber = lineNumber;
	}

	public int getStartPos() {
		return this.startPos;
	}

	public int getEndPos() {
		return this.endPos;
	}
	
	public int getLineNumber() {
		return this.lineNumber;
	}

	public String toString() {
		return "(Line " + lineNumber + ")";
	}
}
