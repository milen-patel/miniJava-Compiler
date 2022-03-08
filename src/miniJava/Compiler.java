package miniJava;

import miniJava.AbstractSyntaxTrees.AST;
import miniJava.AbstractSyntaxTrees.ASTDisplay;
import miniJava.SyntacticAnalyzer.InputReader;
import miniJava.SyntacticAnalyzer.Scanner;
import miniJava.SyntacticAnalyzer.Parser;

public class Compiler {
	public static void main(String[] args) {
		if (args == null || args.length != 1) {
			System.out.println("Invalid Program Arguements");
			System.exit(3);
		}
		if (!args[0].endsWith(".java") && !args[0].endsWith(".mjava"))
			ErrorReporter.get().reportError("Input file has incorrect extension");
		
		try {
			InputReader reader = new InputReader(args[0]);
			Scanner scnr = new Scanner(reader);
			Parser p = new Parser(scnr);
			System.out.println("Syntactic Analysis...");
			AST tree = p.parseProgram();
			System.out.println("Syntactic Analysis Complete");
			
			ASTDisplay.showPosition = false;			
			ASTDisplay display = new ASTDisplay();
			System.out.println("Valid miniJava program");
			display.showTree(tree);
			
			ErrorReporter.get().endWithSuccess();
		} catch (Exception e) {
			ErrorReporter.get().reportError("Uncaught Exception");
		} 
		
	}
}
