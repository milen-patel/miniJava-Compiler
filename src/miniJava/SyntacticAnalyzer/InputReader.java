package miniJava.SyntacticAnalyzer;

import java.io.FileInputStream;

public class InputReader {
	private FileInputStream fileInputStream;
	
	private InputReader(FileInputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}
}
