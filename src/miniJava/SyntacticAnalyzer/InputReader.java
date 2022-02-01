package miniJava.SyntacticAnalyzer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/*
 * InputReader provides functions for traversing the input file for the purpose of aiding the Scanner class.
 */
public class InputReader {
	private InputStream fileInputStream;
	private java.util.Scanner scanner;
	private int scannerPos = 1;
	private boolean eofEncountered;
	private char current;

	public InputReader(String fileName) {
		File inputFile = new File(fileName);

		try {
			java.util.Scanner scanner = new java.util.Scanner(inputFile);
			this.scanner = scanner;
			this.fileInputStream = new java.io.FileInputStream(inputFile);
		} catch (FileNotFoundException e) {
			System.out.println("Unable to open input file.");
			System.exit(1);
		}
		
		this.eofEncountered = false;
	}

	public int getScannerPosition() {
		return this.scannerPos;
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
				Reporter.get().log("EOF Encountered in pullNextChar", 1);
				this.eofEncountered = true;
			}
			this.scannerPos++;
			return this.current;
		} catch (IOException e) {
			Reporter.get().reportError("I/O Exception");
		}
		throw new RuntimeException("I/O Exception");
	}
	
	public void close() {
		this.scanner.close();
	}
}
