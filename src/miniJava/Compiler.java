package miniJava;

import miniJava.SyntacticAnalyzer.Reporter;
import miniJava.SyntacticAnalyzer.InputReader;
import miniJava.SyntacticAnalyzer.Scanner;
import miniJava.SyntacticAnalyzer.Parser;

public class Compiler {
	public static void main(String[] args) {
		// Validate proper command line arguments to program
		if (args == null || args.length != 1)
			Reporter.get().reportError("Expected exactly 1 command line arguement");
		if (!args[0].endsWith(".java") && !args[0].endsWith(".mjava"))
			Reporter.get().reportError("Input file has incorrect extension");
		InputReader reader = new InputReader(args[0]);
		
		Scanner scnr = new Scanner(reader);
		Parser p = new Parser(scnr);
		
		p.parseProgram();
		reader.close();
		Reporter.get().endWithSuccess();
	}

}
