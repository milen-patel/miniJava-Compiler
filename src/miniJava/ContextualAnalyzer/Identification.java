package miniJava.ContextualAnalyzer;

import miniJava.ErrorReporter;
import miniJava.AbstractSyntaxTrees.*;
import miniJava.AbstractSyntaxTrees.Package;
import miniJava.SyntacticAnalyzer.SourcePosition;
import miniJava.SyntacticAnalyzer.Token;
import miniJava.SyntacticAnalyzer.TokenKind;

public class Identification implements miniJava.AbstractSyntaxTrees.Visitor<Object, Object> {
	IdentificationTable table = new IdentificationTable();
	Context ctx = new Context();
	
	@Override
	public Object visitPackage(Package prog, Object arg) {
		int y = ctx.x;
		ErrorReporter.get().log("<Contextual Analysis> Starting contextual analysis", 5);
		
		// Add predefined classes
		FieldDeclList class_System_Fields = new FieldDeclList();
		MethodDeclList class_System_Methods = new MethodDeclList();
		Token printStreamToken = new Token(TokenKind.IDENTIFIER, "_PrintStream", 0 , 0, 0);
		Identifier printStreamIdentifier = new Identifier(printStreamToken);
		SourcePosition dummy_Pos = new SourcePosition(0, 0, 0);
		class_System_Fields.add(new FieldDecl(false, true, new ClassType(printStreamIdentifier, dummy_Pos), "out", dummy_Pos));
		ClassDecl class_System = new ClassDecl("System", class_System_Fields, class_System_Methods, dummy_Pos);
		table.addClass("System", class_System);
		table.add("System", class_System);
		table.classMethodDeclarations.put("System", class_System_Methods);
		table.classVariableDeclarations.put("System", class_System_Fields);
		
		FieldDeclList printStreamFields = new FieldDeclList();
		MethodDeclList printStreamMethods = new MethodDeclList();
		ParameterDeclList pdl = new ParameterDeclList();
		pdl.add(new ParameterDecl(new BaseType(TypeKind.INT, dummy_Pos), "n", dummy_Pos));
		FieldDecl lhs = new FieldDecl(false, false, new BaseType(TypeKind.VOID, dummy_Pos), "println", dummy_Pos);
		MethodDecl printDecl = new MethodDecl(lhs, pdl, new StatementList(), dummy_Pos);
		printStreamMethods.add(printDecl);
		ClassDecl class_PrintStream = new ClassDecl("_PrintStream", printStreamFields, printStreamMethods, dummy_Pos);
		table.addClass("_PrintStream", class_PrintStream);
		table.add("_PrintStream", class_PrintStream);
		table.classMethodDeclarations.put("_PrintStream", printStreamMethods);
		table.classVariableDeclarations.put("_PrintStream", printStreamFields);
		
		ClassDecl class_String = new ClassDecl("String", new FieldDeclList(), new MethodDeclList(), dummy_Pos);
		table.addClass("String", class_String);
		table.add("String", class_String);
		table.classMethodDeclarations.put("String", class_String.methodDeclList);
		table.classVariableDeclarations.put("String", class_String.fieldDeclList);
		
		// todo need to do id + type checking for predefined classes
		
		ClassDeclList classes = prog.classDeclList;
		for (int i = 0; i < classes.size(); i++) {
			ClassDecl cd = classes.get(i);
			String cn = cd.name;
			table.addClass(cn, cd);
			table.add(cn, cd);
			table.classMethodDeclarations.put(cn, cd.methodDeclList);
			table.classVariableDeclarations.put(cn, cd.fieldDeclList);
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
		
		// Record all method names
		for (int i = 0; i < methodDecls.size(); i++) {
			MethodDecl md = methodDecls.get(i);
			// Make sure a variable/method with that name hasn't already been defined
			if (table.containsKeyAtTopScope(md.name)) {
				System.out
						.println("*** line " + md.posn.getLineNumber() + ": Duplicate class field declaration error. Field "
								+ md.name + " has already been defined. oops!");
			}
			table.add(md.name, md);
		}
		
		
		for (int i = 0; i < methodDecls.size(); i++) {
			methodDecls.get(i).visit(this, arg);
		}

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
		
		// Check if we are in a static method
		if (md.isStatic) {
			ctx.setStatic(true);
		} else {
			ctx.setStatic(false);
		}
		
		// Check the return type
		md.type.visit(this, null);
		
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
		
		//good for debuggnig System.out.println("Finished processing vardeclstmt..." + stmt.posn.getLineNumber());
		//System.out.println(((RefExpr) stmt.initExp).ref.getDeclaration());
		
		return null;
	}

	@Override
	public Object visitAssignStmt(AssignStmt stmt, Object arg) {
		ErrorReporter.get().log("Visiting Assign Statement on Line " + stmt.posn.getLineNumber(), 5);
		stmt.ref.visit(this, arg);
		stmt.val.visit(this, arg);
		ErrorReporter.get().log("Left Hand Side of assign statement points to: " + stmt.ref.getDeclaration(), 5);
		return null;
	}

	@Override
	public Object visitIxAssignStmt(IxAssignStmt stmt, Object arg) {
		ErrorReporter.get().log("Visiting Indexed Assign Statement on Line " + stmt.posn.getLineNumber(), 5);
		stmt.ref.visit(this, arg);
		stmt.ix.visit(this, arg);
		stmt.exp.visit(this, arg); // TODO is ensuring reference an array a type checking issue
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
		ErrorReporter.get().log("Visiting Return Statement on Line " + stmt.posn.getLineNumber(), 5);
		Expression e = stmt.returnExpr;
		if (e != null) {
			e.visit(this, arg);
		}
		return null; 		
		// TOOD make sure we arent returning a function (type checking issue)
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
			
			//return null;
		}
		expr.ref.visit(this, arg);
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
		// Check that the class exists
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
		return null;
	}

	@Override
	public Object visitIdRef(IdRef ref, Object arg) {
		// Check that there is a corresponding declaration
		if (this.table.find(ref.id.spelling) == null) {
			System.out.println(
					"*** line " + ref.posn.getLineNumber() + ": Unknown reference to identifier '" + ref.id.spelling + "'.");
		}
		
		// If we are defining a variable, we cannot reference that variable in the initializinig expression
		if (ctx.inMethodVariableDeclaration()) {
			if (ref.id.spelling.contentEquals(ctx.getVariableInDeclaration())) {
				System.out.println(
						"*** line " + ref.posn.getLineNumber() + ": cannot reference variable name '" + ref.id.spelling + "' in initializiing expression");
			} // TODO check if theres another place this issue could happen
		}

		ref.setDeclaration(table.find(ref.id.spelling));
		return null;
	}
	

	@Override
	public Object visitQRef(QualRef ref, Object arg) {
		Reference lhs = ref.ref;
		Identifier rhs = ref.id;
	
		// Check the reference first
		lhs.visit(this, arg);

		// If the reference is a function then we have an error
		if (lhs.getDeclaration() instanceof MethodDecl) {
			// TODO need to figure out if this is the correct way to check this
			System.out.println(
					"*** line " + ref.posn.getLineNumber() + ": cannot use a method name on left hand side of a qualified reference");
		}
		
		// Visit right hand side, pass the LHS as context
		rhs.visit(this, lhs);
		
		// TODO figure out if this is correct
		ref.setDeclaration(rhs.getDeclaration());
		return null;
	}

	@Override
	public Object visitIdentifier(Identifier id, Object arg) {
		// Ensure you got a parameter
		if (arg == null) {
			ErrorReporter.get().reportError("Internal Identification Error"); // TODO
		}

		Reference r = (Reference) arg;
		Declaration d = r.getDeclaration();

		// Case 0: 'this' keyword
		if (r instanceof ThisRef) {
			// Check all class variables
			for (FieldDecl fd : ctx.getCurrentClass().fieldDeclList) {
				if (fd.name.contentEquals(id.spelling)) {
					id.setDecalaration(fd);
					return null; // TODO should check static here depending on piazza response
				}
			}
			
			// If no match, check class methods
			for (MethodDecl md : ctx.getCurrentClass().methodDeclList) {
				if (md.name.contentEquals(id.spelling)) {
					id.setDecalaration(md);
					return null;
				}
			}
			
			System.out.println(
					"*** line " + id.posn.getLineNumber() + ": unknown identifier following 'this' keyword: '" + id.spelling +  "'.");
			return null;
		}
		
		// Case 1: Pointing to a Variable
		if (d instanceof FieldDecl) {			
			// TODO check if there could be an exception to do
			// The variable must be a class type
			if (!(d.type instanceof ClassType)) {
				System.out.println(
						"*** line " + id.posn.getLineNumber() + ": cannot use primititve/array type on left hand side of qualified reference");
				return null;
			}
			
			ClassType ct = (ClassType) d.type;
			ClassDecl correspondingClass = this.table.classesTable.get(ct.className.spelling);
			
			if (correspondingClass == null) {
				System.out.println(
						"*** line " + id.posn.getLineNumber() + ": no such class"); // TODO figure out if its even possible to get here
				return null;
			}
			
			// Now go through and find the correct variable
			MemberDecl match = null;
			for (MethodDecl md : correspondingClass.methodDeclList) {
				if (md.name.contentEquals(id.spelling)) {
					match = md;
					break;
				}
			}
			for (FieldDecl fd: correspondingClass.fieldDeclList) {
				if (fd.name.contentEquals(id.spelling)) {
					match = fd;
					break;
				}
			}
			if (match == null) {
				System.out.println(
						"*** line " + id.posn.getLineNumber() + ": no match"); // TODO figure out if its even possible to get here
				return null;
			}
			// Respect the private keyword
						if (match.isPrivate && (table.getDefiningClassOfField((FieldDecl) d) != ctx.getCurrentClass())) { // TODO definetly needs some testing
							System.out.println(
									"*** line " + id.posn.getLineNumber() + ": cannot access private field from outside of class");
							return null;
						}
			id.setDecalaration(match);
			return null;
		}
		
		// Case 2: Pointing to a Class (ClassName.classPropertyName)
		if (d instanceof ClassDecl) {
			MethodDeclList mds = table.classMethodDeclarations.get(d.name); // TODO check they exist in tables first
			FieldDeclList fds = table.classVariableDeclarations.get(d.name);
			//System.out.println();

			for (MethodDecl md: mds) {

				if (md.name.contentEquals(id.spelling)) {
					// TODO check this,
					
					// Respect private keyword
					if (md.isPrivate) {
						if (ctx.getCurrentClass() != ((ClassDecl) d)) {
							System.out.println(
									"*** line " + id.posn.getLineNumber() + ": cannot reference private method outside of class.");
						}
						return null; // todo figure out exiting bc this yields 2 error lines if its private and non-static
					}
					
					// Must be a static method in the class to reference it like this
					if (!md.isStatic) {
						System.out.println(
								"*** line " + id.posn.getLineNumber() + ": cannot reference non-static method in a static manner");
					}
					
					id.setDecalaration(md);
					return null;
				}
			}
			
			for (FieldDecl fd : fds) {
				if (fd.name.contentEquals(id.spelling)) {
					if (fd.isPrivate && (ctx.getCurrentClass() != ((ClassDecl) d))) {
						System.out.println(
								"*** line " + id.posn.getLineNumber() + ": cannot reference private field outside of class.");
						return null;
					}
					if (!fd.isStatic) {
						System.out.println(d);
						System.out.println(
								"*** line " + id.posn.getLineNumber() + ": cannot reference non-static variable in a static manner");
					}
					id.setDecalaration(fd);
					return null;
				}
			}
			// TODO error if we made it here
			//System.out.println(
			//		"*** line " + id.posn.getLineNumber() + ": failed to identify '" + id.spelling + "'.");
		}
		System.out.println(
				"*** line " + id.posn.getLineNumber() + ": failed to identify '" + id.spelling + "'.");
		return null; // TODO error?
	}

	@Override
	public Object visitOperator(Operator op, Object arg) {
		// TODO Auto-generated method stub
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
