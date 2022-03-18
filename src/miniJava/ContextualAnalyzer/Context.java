package miniJava.ContextualAnalyzer;

import miniJava.ErrorReporter;
import miniJava.AbstractSyntaxTrees.ClassDecl;

public class Context {
	static int x = 5;
	private ClassDecl currentClass;
	private boolean inStaticMethod = false;
	
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
}
