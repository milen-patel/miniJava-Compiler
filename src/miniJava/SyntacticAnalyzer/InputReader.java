package miniJava.SyntacticAnalyzer;

import java.io.FileInputStream;
import java.io.IOException;

public class InputReader {
	private FileInputStream fileInputStream;
	private int scannerPos = 1;

	public InputReader(FileInputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
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
			System.out.println("Input stream has no more tokens."); // TODO are empty files valid?
			System.exit(miniJava.Compiler.FAILURE_RETURN_CODE);
		}
		throw new RuntimeException("No more to read");
	}
}
