/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.Token;

public class Identifier extends Terminal {
  private Declaration decl;
  
  public Identifier (Token t) {
    super (t);
  }

  public void setDecalaration(Declaration d) {
	  this.decl = d;
  }
  
  public Declaration getDeclaration() {
	  return this.decl;
  }

  public <A,R> R visit(Visitor<A,R> v, A o) {
      return v.visitIdentifier(this, o);
  }

}
