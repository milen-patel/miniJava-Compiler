package miniJava;

import java.io.*;

import miniJava.AbstractSyntaxTrees.AST;
import miniJava.AbstractSyntaxTrees.Package;
import miniJava.AbstractSyntaxTrees.ASTDisplay;
import miniJava.SyntacticAnalyzer.InputReader;
import miniJava.SyntacticAnalyzer.Scanner;
import miniJava.SyntacticAnalyzer.Parser;

public class Compiler {
	public static void Compiler() {}
	public static void main(String[] args) {
		//runTests();
		
		args = new String[] {"/Users/milenpatel/git/miniJava-Compiler/test/Contextual Analysis/fail20-equal-not-equal-to-operators.java"};
	
		if (args == null || args.length != 1) {
			System.out.println("Invalid Program Arguements");
			System.exit(3);
		}
		if (!args[0].endsWith(".java") && !args[0].endsWith(".mjava"))
			ErrorReporter.get().reportError("Input file has incorrect extension");
		
	//	try {
			InputReader reader = new InputReader(args[0]);
			Scanner scnr = new Scanner(reader);
			Parser p = new Parser(scnr);
			Package tree = p.parseProgram();			
			
			(new miniJava.ContextualAnalyzer.Identification()).visitPackage(tree, null);
			(new miniJava.ContextualAnalyzer.TypeChecker()).visitPackage(tree, null);
			ErrorReporter.get().endWithSuccess();
	//	} catch (Exception e) {
	//		ErrorReporter.get().reportError("Uncaught Exception");
	//	} 
		
	}
	
	public static void runTests() {
		File folder = new File("/Users/milenpatel/Downloads/pa2_tests");
		PrintStream defaultPrintStream = System.out;
		File[] listOfFiles = folder.listFiles();
		
		// Iterate Over Each File
		for (int i = 0; i < listOfFiles.length; i++) {
		  if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".java") && listOfFiles[i].getName().startsWith("pass")) {
			// Open a dummy file to write the AST Into
			try {
				System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("/Users/milenpatel/Downloads/pa2_tests/text.txt")), true));
			} catch (FileNotFoundException e1) { e1.printStackTrace();}
			
			// Create the AST and write it to teh file
			System.out.println("======= AST Display =========================");
			InputReader reader = new InputReader(listOfFiles[i].getAbsoluteFile().toString());
			Scanner scnr = new Scanner(reader);
			Parser p = new Parser(scnr);
			try {
				Package tree = p.parseProgram();			
				//(new miniJava.ContextualAnalyzer.Identification()).visitPackage(tree, null);
				//(new miniJava.ContextualAnalyzer.TypeChecker()).visitPackage(tree, null);
				(new ASTDisplay()).visitPackage(tree, "");
				System.out.println("=============================================");
				
				// Once we finish AST Display, change the output stream back
				System.setOut(defaultPrintStream);
			
				// Take the diff of the file with the expected output
				Process pro = Runtime.getRuntime().exec(new String[]{"zsh","-c","diff " + listOfFiles[i].getAbsoluteFile().toString() + ".out /Users/milenpatel/Downloads/pa2_tests/text.txt"});
				if (pro.waitFor() == 1) {
					System.out.println("Different AST than what was expected for " + listOfFiles[i].getAbsolutePath().toString());
				}
			} catch (Exception e)  {
				System.out.println("FAIL");
			}
		  } else if (listOfFiles[i].getName().endsWith(".java") && listOfFiles[i].getName().startsWith("fail")){
			  // Fails
			  try {
				  InputReader reader = new InputReader(listOfFiles[i].getAbsoluteFile().toString());
				  System.setOut(new PrintStream(new OutputStream() { public void write(int b) {} }));
				  	Scanner scnr = new Scanner(reader);
					Parser p = new Parser(scnr);
					Package tree = p.parseProgram();
					System.setOut(defaultPrintStream);
				  System.out.println("Should have failed but didn't " + listOfFiles[i].getAbsoluteFile());
			  } catch (Exception e) {
				  System.setOut(defaultPrintStream);
			  }
		  }
		}
	}
}
