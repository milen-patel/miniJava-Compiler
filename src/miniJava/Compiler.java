package miniJava;

import java.io.File;

import miniJava.AbstractSyntaxTrees.AST;
import miniJava.AbstractSyntaxTrees.Package;
import miniJava.AbstractSyntaxTrees.ASTDisplay;
import miniJava.SyntacticAnalyzer.InputReader;
import miniJava.SyntacticAnalyzer.Scanner;
import miniJava.SyntacticAnalyzer.Parser;

public class Compiler {
	public static void main(String[] args) {
		//runTests();
	
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
			Package tree = p.parseProgram();
			System.out.println("Syntactic Analysis Complete");
			
			ASTDisplay.showPosition = true;			
			
			(new miniJava.ContextualAnalyzer.Identification()).visitPackage(tree, null);
			(new miniJava.ContextualAnalyzer.TypeChecker()).visitPackage(tree, null);
			ASTDisplay display = new ASTDisplay();
			System.out.println("Valid miniJava program");
			//display.showTree(tree);
			ErrorReporter.get().endWithSuccess();
		} catch (Exception e) {
			System.out.println(e.toString());
			ErrorReporter.get().reportError("Uncaught Exception");
		} 
		
	}
	
	public static void runTests() {
		File folder = new File("/Users/milenpatel/Downloads/pa2_tests");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
		  if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".java") && listOfFiles[i].getName().startsWith("pass")) {
		    main(new String[] {listOfFiles[i].getAbsoluteFile().toString()});
		  } else if (listOfFiles[i].isDirectory()) {
		    System.out.println("Directory " + listOfFiles[i].getName());
		  }
		}
	}
}
