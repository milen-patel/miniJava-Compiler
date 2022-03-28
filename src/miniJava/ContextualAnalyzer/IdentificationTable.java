package miniJava.ContextualAnalyzer;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import miniJava.ErrorReporter;
import miniJava.AbstractSyntaxTrees.ClassDecl;
import miniJava.AbstractSyntaxTrees.Declaration;
import miniJava.AbstractSyntaxTrees.FieldDecl;
import miniJava.AbstractSyntaxTrees.FieldDeclList;
import miniJava.AbstractSyntaxTrees.MethodDecl;
import miniJava.AbstractSyntaxTrees.MethodDeclList;

public class IdentificationTable {
	Stack<Map<String, Declaration>> table;
	Map<String, ClassDecl> classesTable;
	Map<String, FieldDeclList> classVariableDeclarations;
	Map<String, MethodDeclList> classMethodDeclarations;
	
	public IdentificationTable() {
		this.table = new Stack<Map<String, Declaration>>();
		this.classesTable = new HashMap<String, ClassDecl>();
		this.table.push(new HashMap<String, Declaration>());
		this.classVariableDeclarations = new HashMap<String, FieldDeclList>();
		this.classMethodDeclarations = new HashMap<String, MethodDeclList>();
	}
	
	public void addClass(String className, ClassDecl decl) {
		// Check if class already exists
		if (classesTable.containsKey(className)) {
			// This logic is being replaced in the add function
			//System.out.println("*** line " + decl.posn.getLineNumber() + ": Duplicate class error. Class " + className + " has already been defined.");
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
	
	public boolean containsKeyAtTopScope(String key) {
		return this.table.peek().containsKey(key);
	}
	
	public boolean containsKeyAtNonCoverableScope(String key) {
		for (int i = 2; i < table.size(); i++) {
			if (table.get(i).containsKey(key)) {
				return true;
			}
		}
		
		return false;
	}
	
	public Declaration find(String key) {
		for (int i = this.table.size()-1; i >= 0; i--) {
			if (this.table.get(i).containsKey(key)) {
				return this.table.get(i).get(key);
			}
		}
		return null;
	}
	
	public int getScopeLevel() {
		return this.table.size();
	}
	
	public void add(String key, Declaration val) {
		// Check if declaration exists in most current scope
		if (this.table.peek().containsKey(key)) {
			System.out.println("*** line " + val.posn.getLineNumber() + ": ");
			ErrorReporter.get().idError(val.posn.getLineNumber(), "duplicate declaration error. Identifier '" + key + "' has already been used.");
			return;
		}
		
		// Declarations at level 4 or higher cannot hide declarations at level 3 or higher
		if (this.getScopeLevel() >= 4) {
			for (int i = 2; i < table.size(); i++) {
				if (this.table.get(i).containsKey(key)) {
					ErrorReporter.get().idError(val.posn.getLineNumber(), "declarations at level 4 or higher cannot hide declarations at level 3 or higher");
					break;
				}
			}
		}
		this.table.peek().put(key, val);	
	}
	
	public void print() {
		System.out.println("==========================================");
		String prefix = "";
		for (int i = 0; i < this.table.size(); i++) {
			System.out.println(prefix + "Scope " + (i+1));
			Map<String, Declaration> hm = this.table.get(i);
			for (String key : hm.keySet()) {
				System.out.println(prefix + "\t" + key);
			}
			prefix += "\t";
		}
		System.out.println("==========================================");
	}
}
