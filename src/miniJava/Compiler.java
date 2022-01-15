package miniJava;

import java.io.File; 
import java.io.FileNotFoundException;

import miniJava.SyntacticAnalyzer.Scanner;
import miniJava.SyntacticAnalyzer.Token;
import miniJava.SyntacticAnalyzer.TokenType; 

public class Compiler {
	public static final int SUCCESS_RETURN_CODE = 0;
	public static final int FAILURE_RETURN_CODE = 4;

	public static void main(String[] args) {
		if (args == null)
			throw new RuntimeException("Bad Command Line Arguements");
		if (args.length != 1)
			throw new RuntimeException("Expected one command line arguement");
		// TODO: Should we throw exception or print error and exit with certain code

		// All Credit goes to: https://www.w3schools.com/java/java_files_read.asp
		// TODO is it fine for this to be modeled from online 
		try {
			File inputFile = new File(args[0]);
			java.util.Scanner scanner = new java.util.Scanner(inputFile);
			
			Scanner scnr = new Scanner(new java.io.FileInputStream(inputFile));
			while (true) {
				Token t = scnr.scan();
				System.out.println(t);
				if (t.getType() == TokenType.EOT)
					break;
			}
			scanner.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("Failed to open the file.");
			System.exit(FAILURE_RETURN_CODE);
		}
		System.exit(SUCCESS_RETURN_CODE);
	}

}
