package miniJava.ContextualAnalyzer;

import miniJava.ErrorReporter;
import miniJava.AbstractSyntaxTrees.ClassDecl;

public class Context {
	private ClassDecl currentClass;
	
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
	}
	
	public boolean inClass() {
		return this.currentClass != null;
	}
}
