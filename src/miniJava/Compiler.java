package miniJava;

import miniJava.AbstractSyntaxTrees.AST;
import miniJava.AbstractSyntaxTrees.ASTDisplay;
import miniJava.SyntacticAnalyzer.InputReader;
import miniJava.SyntacticAnalyzer.Scanner;
import miniJava.SyntacticAnalyzer.Parser;

public class Compiler {
	public static void main(String[] args) {
		// Validate proper command line arguments to program
		if (args == null || args.length != 1) {
			System.out.println("Invalid Program Arguements");
			System.exit(1);
		}
		if (!args[0].endsWith(".java") && !args[0].endsWith(".mjava"))
			ErrorReporter.get().reportError("Input file has incorrect extension");
		InputReader reader = new InputReader(args[0]);
		
		Scanner scnr = new Scanner(reader);
		Parser p = new Parser(scnr);
		
		AST tree = p.parseProgram();
		ASTDisplay.showPosition = true; // TODO Change before submission
		ASTDisplay display = new ASTDisplay();
		display.showTree(tree);
		ErrorReporter.get().endWithSuccess();
	}

}
