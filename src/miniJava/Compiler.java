package miniJava;

import miniJava.SyntacticAnalyzer.Reporter;
import miniJava.SyntacticAnalyzer.InputReader;
import miniJava.SyntacticAnalyzer.Scanner;
import miniJava.SyntacticAnalyzer.Token;
import miniJava.SyntacticAnalyzer.TokenType;

public class Compiler {
	public static void main(String[] args) {
		// Validate proper command line arguments to program
		if (args == null || args.length != 1)
			Reporter.get().reportError("Expected exactly 1 command line arguement");
		
		InputReader reader = new InputReader(args[0]);
		Scanner scnr = new Scanner(reader);
		
		while (true) {
			Token t = scnr.scan();
			System.out.println(t);
			if (t.getType() == TokenType.EOT)
				break;
		}

		reader.close();
		Reporter.get().endWithSuccess();
	}

}
