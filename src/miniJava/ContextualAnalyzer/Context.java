package miniJava.ContextualAnalyzer;

import miniJava.ErrorReporter;
import miniJava.AbstractSyntaxTrees.ClassDecl;

public class Context {
	private ClassDecl currentClass;
	private boolean inStaticMethod = false;
	private String variableInDeclaration = null;
	
	public ClassDecl getCurrentClass() {
		return this.currentClass;
	}
	
	public void setCurrentClass(ClassDecl newClass) {
		if (currentClass != null) {
			ErrorReporter.get().log("Shouldn't be setting non-empty class", 9); // TODO
		}
		this.currentClass = newClass;
	}
	
	public void clearCurrentClass() {
		this.currentClass = null;
		this.inStaticMethod = false;
	}
	
	public boolean inClass() {
		return this.currentClass != null;
	}
	
	public void setStatic(boolean val) {
		this.inStaticMethod = val;
	}
	
	public boolean inStaticMethod() {
		return this.inStaticMethod;
	}
	
	public boolean inMethodVariableDeclaration() {
		return this.variableInDeclaration != null;
	}
	
	public void setVariableInDeclaration(String varName) {
		this.variableInDeclaration = varName;
	}
	
	public String getVariableInDeclaration() {
		// TODO assert non null
		return this.variableInDeclaration;
	}
	
	public void exitVariableInDeclaration() {
		// TODO need to assert that we are currently decalaring a variable already
		this.variableInDeclaration = null;
	}
}
