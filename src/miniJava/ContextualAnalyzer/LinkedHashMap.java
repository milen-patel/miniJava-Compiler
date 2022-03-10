package miniJava.ContextualAnalyzer;

import java.util.HashMap;
import java.util.Map;

import miniJava.AbstractSyntaxTrees.Declaration;

public class LinkedHashMap {
	private LinkedHashMap parent;
	Map<String, Declaration> map;
	
	public LinkedHashMap() {
		this.map = new HashMap<String, Declaration>();
	}
	
	public LinkedHashMap(LinkedHashMap parent) {
		this.map = new HashMap<String, Declaration>();
		this.parent = parent;
	}
	
	public boolean hasParentScope() {
		return this.parent == null;
	}
	
	public LinkedHashMap getParent() {
		return this.parent;
	}
	
	public Declaration lookup(String key) {
		// Special case, there is no parent scope
		if (this.parent == null) {
			if (!this.map.containsKey(key)) {
				// Report Error
			}
			return this.map.get(key);
		}
		
		if (!this.map.containsKey(key)) {
			return this.parent.lookup(key);
		}
		
		return this.map.get(key);
	}
}
