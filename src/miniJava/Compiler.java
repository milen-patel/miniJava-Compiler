package miniJava;

import mJAM.ObjectFile;
import miniJava.AbstractSyntaxTrees.Package;
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
			Package tree = p.parseProgram();			
			
			(new miniJava.ContextualAnalyzer.Identification()).visitPackage(tree, null);
			(new miniJava.ContextualAnalyzer.TypeChecker()).visitPackage(tree, null);
			
			if (ErrorReporter.get().typeErrors > 0)
				System.exit(ErrorReporter.FAILURE_RETURN_CODE);
			
			(new miniJava.CodeGeneration.Generator()).generateCode(tree);
			String name = args[0].substring(0, args[0].lastIndexOf('.')) + ".mJAM";
			ObjectFile objF = new ObjectFile(name);
			System.out.print("Writing object code file " + name + " ... ");
			if (objF.write()) {
				System.out.println("FAILED!");
				return;
			}
			else
				System.out.println("SUCCEEDED");
			
			ErrorReporter.get().endWithSuccess();
		} catch (Exception e) {
			ErrorReporter.get().reportError("Uncaught Exception");
		}	
	}
}
