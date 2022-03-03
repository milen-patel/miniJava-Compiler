package miniJava;

import java.io.*;

import miniJava.AbstractSyntaxTrees.AST;
import miniJava.AbstractSyntaxTrees.ASTDisplay;
import miniJava.SyntacticAnalyzer.InputReader;
import miniJava.SyntacticAnalyzer.Scanner;
import miniJava.SyntacticAnalyzer.Parser;

public class Compiler {
	public static void main(String[] args) {
		// Validate proper command line arguments to program
		/*
		File folder = new File("/users/milenpatel/Desktop/pa1_tests");
		try {
			//System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("/users/milenpatel/Desktop/text.txt"))));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File[] listOfFiles = folder.listFiles();
		int numFiles = 0;
		for (int i = 0; i < listOfFiles.length; i++) {
		  if (listOfFiles[i].isFile()) {
		    if (listOfFiles[i].getName().indexOf("pass") == 0) {
		    	
					System.out.println("(" + (++numFiles) + ") + Testing: " + listOfFiles[i].getName());
			
		    	InputReader reader = new InputReader(listOfFiles[i].getAbsolutePath());
				Scanner scnr = new Scanner(reader);
				Parser p = new Parser(scnr);
				(new ASTDisplay()).showTree(p.parseProgram());
				System.out.println("\tSuccess");
		    }
		  }
		}*/
		
		
		if (args == null || args.length != 1) {
			System.out.println("Invalid Program Arguements");
			System.exit(1);
		}
		if (!args[0].endsWith(".java") && !args[0].endsWith(".mjava"))
			ErrorReporter.get().reportError("Input file has incorrect extension");
		
		try {
			InputReader reader = new InputReader(args[0]);
			Scanner scnr = new Scanner(reader);
			Parser p = new Parser(scnr);
			AST tree = p.parseProgram();
			ASTDisplay.showPosition = false; // TODO Change before submission
			ASTDisplay display = new ASTDisplay();
			display.showTree(tree);
			ErrorReporter.get().endWithSuccess();
		} catch (Exception e) {
			ErrorReporter.get().reportError("Uncaught Exception");
		} 
	}
}
