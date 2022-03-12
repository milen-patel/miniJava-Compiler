package miniJava.SyntacticAnalyzer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import miniJava.ErrorReporter;

/*
 * InputReader provides functions for traversing the input file for the purpose of aiding the Scanner class.
 */
public class InputReader {
	private InputStream fileInputStream;
	private int scannerPos = 0;
	private int lineNumber = 1;
	private boolean eofEncountered;
	private char current;

	public InputReader(String fileName) {
		try {
			this.fileInputStream = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			System.out.println("Unable to open input file.");
			System.exit(3);
		}

		this.eofEncountered = false;
	}

	public int getScannerPosition() {
		return this.scannerPos;
	}
	
	public int getLineNumber() {
		return this.lineNumber;
	}

	public boolean eofEncountered() {
		return this.eofEncountered;
	}

	public char nextChar() {
		if (this.eofEncountered) {
			return this.current;
		}
		return nextCharHelper();
	}

	private char nextCharHelper() {
		try {
			int val = this.fileInputStream.read();
			this.current = (char) val;
			if (val == -1) {
				ErrorReporter.get().log("EOF Encountered in pullNextChar", 1);
				this.eofEncountered = true;
			}
			if (val == '\n') {
				this.lineNumber++;
			}
			this.scannerPos++;
			return this.current;
		} catch (IOException e) {
			ErrorReporter.get().reportError("I/O Exception");
		}
		throw new RuntimeException("I/O Exception");
	}
}
