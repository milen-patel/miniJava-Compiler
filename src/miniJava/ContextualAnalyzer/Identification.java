package miniJava.ContextualAnalyzer;

import miniJava.ErrorReporter;
import miniJava.AbstractSyntaxTrees.*;
import miniJava.AbstractSyntaxTrees.Package;

public class Identification implements miniJava.AbstractSyntaxTrees.Visitor<Object, Object> {
	IdentificationTable table = new IdentificationTable();
	Context ctx = new Context();
	
	@Override
	public Object visitPackage(Package prog, Object arg) {
		ErrorReporter.get().log("<Contextual Analysis> Starting contextual analysis", 5);

		ClassDeclList classes = prog.classDeclList;
		for (int i = 0; i < classes.size(); i++) {
			ClassDecl cd = classes.get(i);
			String cn = cd.name;
			table.addClass(cn, cd);
			table.add(cn, cd);
		}

		for (int i = 0; i < classes.size(); i++) {
			classes.get(i).visit(this, arg);
		}
		return null;
	}

	int VisitClassDecl = 5;

	@Override
	public Object visitClassDecl(ClassDecl cd, Object arg) {
		ErrorReporter.get().log("<Contextual Analysis> Visiting class: " + cd.name, 5);
		table.openScope();
		ctx.setCurrentClass(cd);

		// Visit all the variable declarations, make sure none are already defined
		FieldDeclList varDecls = cd.fieldDeclList;
		for (int i = 0; i < varDecls.size(); i++) {
			varDecls.get(i).visit(this, arg);
		}

		// Visit all the method declarations, make sure none are already defined
		MethodDeclList methodDecls = cd.methodDeclList;
		for (int i = 0; i < methodDecls.size(); i++) {
			methodDecls.get(i).visit(this, arg);
		}
		table.print();

		table.closeScope();
		ctx.clearCurrentClass();
		return null;
	}

	@Override
	public Object visitFieldDecl(FieldDecl decl, Object arg) {
		ErrorReporter.get().log("<Contextual Analysis> Visiting class variable declaration: " + decl.name, 5);
		// Check if variable already defined
		String vName = decl.name;
		if (table.containsKeyAtTopScope(vName)) {
			System.out.println("*** line " + decl.posn.getLineNumber()
					+ ": Duplicate class field declaration error. Variable " + vName + " has already been defined.");
		}
		
		// Record the variable name
		table.add(vName, decl);
		
		// Check the type of the variable
		decl.type.visit(this, arg);

		return arg;
	}

	@Override
	public Object visitMethodDecl(MethodDecl md, Object arg) {
		ErrorReporter.get().log("<Contextual Analysis> Visiting class method declaration: " + md.name, 5);

		// Make sure a variable/method with that name hasn't already been defined
		if (table.containsKeyAtTopScope(md.name)) {
			System.out
					.println("*** line " + md.posn.getLineNumber() + ": Duplicate class field declaration error. Field "
							+ md.name + " has already been defined. oops!");
		}
		
		// Check if we are in a static method
		if (md.isStatic) {
			ctx.setStatic(true);
		} else {
			ctx.setStatic(false);
		}
		
		// Record the method name in the top scope level of the table
		table.add(md.name, md);
		table.openScope();
		
		// Ensure all of the parameters are correctly typed
		for (int i = 0; i < md.parameterDeclList.size(); i++) {
			md.parameterDeclList.get(i).visit(this, arg);
		}
		
		// Visit each statement in the method body
		for (int i = 0; i < md.statementList.size(); i++) {
			md.statementList.get(i).visit(this, arg);
		}
		
		table.closeScope();
		return null;
		
		// TODO needs to do a separate check to make sure no instance variables are being used
	}
	

	@Override
	public Object visitParameterDecl(ParameterDecl pd, Object arg) {
		ErrorReporter.get().log("<Contextual Analysis> Visiting parameter declaration: " + pd.name, 5);
		// Ensure parameter type is valid
		pd.type.visit(this, arg);
		
		// Add parameter to top level of scope table
		// This will also ensure that there are no duplicate name declarations of parameters.
		this.table.add(pd.name, pd);
		
		return null;
	}

	@Override
	public Object visitVarDecl(VarDecl decl, Object arg) {
		// Check that the type is valid
		decl.type.visit(this, arg);
		
		// See if the variable has already been defined
		if (this.table.containsKeyAtNonCoverableScope(decl.name)) {
			System.out.println("*** line " + decl.posn.getLineNumber() + ": Local variable '" + decl.name + "' declaration attempts to hide declaration at level 3+");
		} else {
			this.table.add(decl.name, decl);
		}
		
		return null;
	}

	@Override
	public Object visitBaseType(BaseType type, Object arg) {
		ErrorReporter.get().log("Visiting Base Type...Nothing to Do", 5);
		return null;
	}

	@Override
	public Object visitClassType(ClassType declType, Object arg) {
		ErrorReporter.get().log("Visiting Class Denoter Type", 5);
		
		// Make sure the class exists
		if (!this.table.classesTable.containsKey(declType.className.spelling)) {
			System.out.println(
					"*** line " + declType.posn.getLineNumber() + ": Unknown class type '" + declType.className.spelling + "'.");
		}

		// If the class exists, make the declaration point to the class declaration
		declType.className.setDecalaration(table.classesTable.get(declType.className.spelling));

		return null;
	}

	@Override
	public Object visitArrayType(ArrayType at, Object arg) {
		ErrorReporter.get().log("Visiting Array Type", 5);

		TypeDenoter elementType = at.eltType;
		elementType.visit(this, arg);
		
		return null;
	}

	@Override
	public Object visitBlockStmt(BlockStmt stmt, Object arg) {
		ErrorReporter.get().log("Visiting Block Statement", 5);
		// Each Statement Block Opens a New Scope
		this.table.openScope();
		
		// Visit each statement in the block, then close scope
		for (int i = 0; i < stmt.sl.size(); i++) {
			stmt.sl.get(i).visit(this, arg);
		}
		
		this.table.closeScope();
		ErrorReporter.get().log("Finished Visiting Block Statement", 5);
		return null;
	}

	@Override
	public Object visitVardeclStmt(VarDeclStmt stmt, Object arg) {
		ErrorReporter.get().log("Visiting Variable Declaration Statement", 5);
		// Visit the Type+Id and then visit the initializing expression
		stmt.varDecl.visit(this, arg);
		
		// TODO init expression cannoot reference the variable name
		ctx.setVariableInDeclaration(stmt.varDecl.name);
		stmt.initExp.visit(this, arg);
		ctx.exitVariableInDeclaration();
		
		return null;
	}

	@Override
	public Object visitAssignStmt(AssignStmt stmt, Object arg) {
		// TODO Auto-generated method stub
		// visit reference, visit expression
		return null;
	}

	@Override
	public Object visitIxAssignStmt(IxAssignStmt stmt, Object arg) {
		// TODO Auto-generated method stub
		// visit reference, visit index, visit expression
		// make sure reference is an array type
		return null;
	}

	@Override
	public Object visitCallStmt(CallStmt stmt, Object arg) {
		// TODO Auto-generated method stub
		/*
		 * should be same as visitCallExpr
		 * Check if in static method, if so should only call other static methods and not use keywords (check params too)
		 * check reference is a method
		 * check parameters align with expected types - prolly type checking
		 * check that the method isn't private if we are external to class
		 * check we arent accessing a static method with 'this'
		 */
		return null;
	}

	@Override
	public Object visitReturnStmt(ReturnStmt stmt, Object arg) {
		// TODO Auto-generated method stub
		// make sure we arent returning a function
		return null;
	}

	@Override
	public Object visitIfStmt(IfStmt stmt, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitWhileStmt(WhileStmt stmt, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitUnaryExpr(UnaryExpr expr, Object arg) {
		/*
		 * Trivially visit the operator which requires no work since operators need not be identified.
		 * Then, visit the expression associated with the unary operator.
		 */
		expr.operator.visit(this, arg);
		expr.expr.visit(this, arg);
		return null;
	}

	@Override
	public Object visitBinaryExpr(BinaryExpr expr, Object arg) {
		/*
		 * Trivially visit the operator which requires no work since operators need not be identified.
		 * Then, visit the two expressions associated with the binary operator.
		 */
		expr.left.visit(this, arg);
		expr.operator.visit(this, arg);
		expr.right.visit(this, arg);
		return null;
	}

	@Override
	public Object visitRefExpr(RefExpr expr, Object arg) {
		// Check if the expression is the right hand side of a local variable declaration statement.
		if (ctx.inMethodVariableDeclaration()) {
			
			return null;
		}
		return null;
	}

	@Override
	public Object visitIxExpr(IxExpr expr, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitCallExpr(CallExpr expr, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitLiteralExpr(LiteralExpr expr, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitNewObjectExpr(NewObjectExpr expr, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitNewArrayExpr(NewArrayExpr expr, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitThisRef(ThisRef ref, Object arg) {
		ErrorReporter.get().log("Visiting a 'this' reference", 5);
		if (!ctx.inClass()) {
			System.out.println(
					"*** line " + ref.posn.getLineNumber() + ": Invalid reference to this");
		}
		
		if (ctx.inStaticMethod()) {
			System.out.println(
					"*** line " + ref.posn.getLineNumber() + ": static methods cannot use 'this' keyword");
		}
		
		ref.setDeclaration(ctx.getCurrentClass());
		// need to check for static TODO
		return null;
	}

	@Override
	public Object visitIdRef(IdRef ref, Object arg) {
		// Check that there is a corresponding declaration
		if (this.table.find(ref.id.spelling) == null) {
			System.out.println(
					"*** line " + ref.posn.getLineNumber() + ": Unknown reference to identifier '" + ref.id.spelling + "'.");
		}
		// TODO not sure this will work
		/*
		 * class A {
		 * 	int A;
		 * 	void f() { int x = A; } how does it know which one to point to
		 * }
		 */
		ref.setDeclaration(table.find(ref.id.spelling));
		return null;
	}
	

	@Override
	public Object visitQRef(QualRef ref, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitIdentifier(Identifier id, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	static int temp = 5;
	@Override

	public Object visitOperator(Operator op, Object arg) {
		// TODO Auto-generated method stub
		System.out.println(temp);
		return null;
	}

	@Override
	public Object visitIntLiteral(IntLiteral num, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitBooleanLiteral(BooleanLiteral bool, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitNullLiteral(NullLiteral nullLiteral, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}
}
