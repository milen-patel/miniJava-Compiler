/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public abstract class Reference extends AST
{
	private Declaration decl;
	
	public Reference(SourcePosition posn){
		super(posn);
	}
	
	public void setDeclaration(Declaration decl) {
		this.decl = decl;
	}
	
	public Declaration getDeclaration() {
		return this.decl;
	}

}
