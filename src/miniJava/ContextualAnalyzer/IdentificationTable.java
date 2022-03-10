package miniJava.ContextualAnalyzer;

import java.util.HashMap;
import java.util.Map;

import miniJava.ErrorReporter;
import miniJava.AbstractSyntaxTrees.ClassDecl;
import miniJava.AbstractSyntaxTrees.Declaration;
import miniJava.AbstractSyntaxTrees.FieldDecl;
import miniJava.AbstractSyntaxTrees.MethodDecl;

public class IdentificationTable {
	private LinkedHashMap map;
	
	public IdentificationTable() {
		map = new LinkedHashMap();
	}
	
	public void openScope() {
		LinkedHashMap childScope = new LinkedHashMap(this.map);
		this.map = childScope;
	}
	
	public void closeScope() {
		if (!this.map.hasParentScope()) {
			// Report Error
		}
		this.map = this.map.getParent();
	}
	
	public void addEntry(ClassDecl c) {
		if (this.map.lookup(c.name) != null) {
			// TODO should duplicate class declarations be caught here?
			ErrorReporter.get().reportError("Class with name '" + c.name + "' appears  to have been declared twice");
		}
		this.map.map.put(c.name, c);
	}
	
	public void addEntry(FieldDecl f) {
		// Check duplicate, not sure how if var has class name or summ like that
		this.map.map.put(f.name, f);
	}
	
	public void addEntry(MethodDecl m) {
		// Check duplicate
		this.map.map.put(m.name, m);
	}
	
	
	
	
}
