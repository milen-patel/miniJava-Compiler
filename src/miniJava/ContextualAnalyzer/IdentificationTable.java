package miniJava.ContextualAnalyzer;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import miniJava.ErrorReporter;
import miniJava.AbstractSyntaxTrees.ClassDecl;
import miniJava.AbstractSyntaxTrees.Declaration;
import miniJava.AbstractSyntaxTrees.FieldDecl;
import miniJava.AbstractSyntaxTrees.MethodDecl;

public class IdentificationTable {
	Stack<Map<String, Declaration>> table;
	Map<String, ClassDecl> classesTable;
	
	public IdentificationTable() {
		this.table = new Stack<Map<String, Declaration>>();
		this.classesTable = new HashMap<String, ClassDecl>();
		this.table.push(new HashMap<String, Declaration>());
	}
	
	public void addClass(String className, ClassDecl decl) {
		// Check if class already exists
		if (classesTable.containsKey(className)) {
			System.out.println("*** line " + decl.posn.getLineNumber() + ": Duplicate class error. Class " + className + " has already been defined.");
			// TODO should we stop here
		}
		this.classesTable.put(className, decl);
	}
	
	public void openScope() {
		ErrorReporter.get().log("Opening Scope " + (table.size() + 1), 5);
		this.table.push(new HashMap<String, Declaration>());
	}
	
	public void closeScope() {
		ErrorReporter.get().log("Closing Scope " + (table.size() + 1), 5);
		if (this.table.size() <= 1) {
			ErrorReporter.get().reportError("Shouldn't be closing scope here");
		}
		this.table.pop();
	}
}
