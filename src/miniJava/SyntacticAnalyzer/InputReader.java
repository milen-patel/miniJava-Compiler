package miniJava.SyntacticAnalyzer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/*
 * InputReader provides functions for traversing the input file for the purpose of aiding the Scanner class.
 */
public class InputReader {
	private FileInputStream fileInputStream;
	private int scannerPos = 1;
	private java.util.Scanner scanner;

	public InputReader(String fileName) {
		// Attempt to open the file
		File inputFile = new File(fileName);
		try {
			java.util.Scanner scanner = new java.util.Scanner(inputFile);
			this.scanner = scanner;
			this.fileInputStream = new java.io.FileInputStream(inputFile);
		} catch (FileNotFoundException e) {
			Reporter.get().reportError("Unable to open input file.");
		}
	}

	public int getScannerPosition() {
		return this.scannerPos;
	}

	public boolean inputHasNext() { // test this
		try {
			return this.fileInputStream.available() >= 0;
		} catch (IOException e) {
			return false;
		}
	}

	public int next() {
		try {
			this.scannerPos++;
			return this.fileInputStream.read();
		} catch (IOException e) {
			Reporter.get().reportError("Input stream has no more tokens."); // TODO are empty files valid?
		}
		throw new RuntimeException("No more to read");
	}
	
	public void close() {
		this.scanner.close();
	}
}
