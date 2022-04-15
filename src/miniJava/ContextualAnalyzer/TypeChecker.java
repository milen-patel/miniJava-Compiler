package miniJava.ContextualAnalyzer;

import miniJava.ErrorReporter;
import miniJava.AbstractSyntaxTrees.ArrayType;
import miniJava.AbstractSyntaxTrees.AssignStmt;
import miniJava.AbstractSyntaxTrees.BaseType;
import miniJava.AbstractSyntaxTrees.BinaryExpr;
import miniJava.AbstractSyntaxTrees.BlockStmt;
import miniJava.AbstractSyntaxTrees.BooleanLiteral;
import miniJava.AbstractSyntaxTrees.CallExpr;
import miniJava.AbstractSyntaxTrees.CallStmt;
import miniJava.AbstractSyntaxTrees.ClassDecl;
import miniJava.AbstractSyntaxTrees.ClassType;
import miniJava.AbstractSyntaxTrees.Declaration;
import miniJava.AbstractSyntaxTrees.ExprList;
import miniJava.AbstractSyntaxTrees.Expression;
import miniJava.AbstractSyntaxTrees.FieldDecl;
import miniJava.AbstractSyntaxTrees.IdRef;
import miniJava.AbstractSyntaxTrees.Identifier;
import miniJava.AbstractSyntaxTrees.IfStmt;
import miniJava.AbstractSyntaxTrees.IntLiteral;
import miniJava.AbstractSyntaxTrees.IxAssignStmt;
import miniJava.AbstractSyntaxTrees.IxExpr;
import miniJava.AbstractSyntaxTrees.LiteralExpr;
import miniJava.AbstractSyntaxTrees.MethodDecl;
import miniJava.AbstractSyntaxTrees.NewArrayExpr;
import miniJava.AbstractSyntaxTrees.NewObjectExpr;
import miniJava.AbstractSyntaxTrees.NullLiteral;
import miniJava.AbstractSyntaxTrees.Operator;
import miniJava.AbstractSyntaxTrees.Package;
import miniJava.AbstractSyntaxTrees.ParameterDecl;
import miniJava.AbstractSyntaxTrees.ParameterDeclList;
import miniJava.AbstractSyntaxTrees.QualRef;
import miniJava.AbstractSyntaxTrees.RefExpr;
import miniJava.AbstractSyntaxTrees.ReturnStmt;
import miniJava.AbstractSyntaxTrees.Statement;
import miniJava.AbstractSyntaxTrees.ThisRef;
import miniJava.AbstractSyntaxTrees.TypeDenoter;
import miniJava.AbstractSyntaxTrees.TypeKind;
import miniJava.AbstractSyntaxTrees.UnaryExpr;
import miniJava.AbstractSyntaxTrees.VarDecl;
import miniJava.AbstractSyntaxTrees.VarDeclStmt;
import miniJava.AbstractSyntaxTrees.WhileStmt;
import miniJava.SyntacticAnalyzer.SourcePosition;
import miniJava.SyntacticAnalyzer.Token;
import miniJava.SyntacticAnalyzer.TokenKind;

public class TypeChecker implements miniJava.AbstractSyntaxTrees.Visitor<Object, TypeDenoter> {
	SourcePosition dummyPos = new SourcePosition(0,0,0);
	
	@Override
	public TypeDenoter visitPackage(Package prog, Object arg) {
		for (ClassDecl cd : prog.classDeclList) {
			cd.visit(this, null);
		}
		
		return null;
	}

	@Override
	public TypeDenoter visitClassDecl(ClassDecl cd, Object arg) {
		for (MethodDecl md : cd.methodDeclList) {
			md.visit(this, null);
		}
		return null;
	}

	@Override
	public TypeDenoter visitFieldDecl(FieldDecl fd, Object arg) {
		// Never Called
		return null;
	}

	@Override
	public TypeDenoter visitMethodDecl(MethodDecl md, Object arg) {
		for(Statement s : md.statementList) {
			s.visit(this, md); 
		}
		
		// If we have a non-void method, the last statement of the body must be a return statement
		if (md.type.typeKind != TypeKind.VOID) {
			if (md.statementList.size() == 0 || !(md.statementList.get(md.statementList.size()-1) instanceof ReturnStmt)) {
				ErrorReporter.get().typeError(md.posn.getLineNumber(), "non-void methods must end with a return statement");
			}
		}
		return null;
	}

	@Override
	public TypeDenoter visitParameterDecl(ParameterDecl pd, Object arg) {
		return pd.type;
	}

	@Override
	public TypeDenoter visitVarDecl(VarDecl decl, Object arg) {
		return decl.type;
	}

	@Override
	public TypeDenoter visitBaseType(BaseType type, Object arg) {
		return null;
	}

	@Override
	public TypeDenoter visitClassType(ClassType type, Object arg) {
		return null;
	}

	@Override
	public TypeDenoter visitArrayType(ArrayType type, Object arg) {
		return null;
	}

	@Override
	public TypeDenoter visitBlockStmt(BlockStmt stmt, Object arg) {
		for (Statement s : stmt.sl) {
			s.visit(this, arg);
		}
		return null;
	}

	@Override
	public TypeDenoter visitVardeclStmt(VarDeclStmt stmt, Object arg) {
		TypeDenoter lhs = stmt.varDecl.visit(this, arg);
		TypeDenoter rhs = stmt.initExp.visit(this, lhs);
		
		if (lhs == null || rhs == null) {
			ErrorReporter.get().typeError(stmt.initExp.posn.getLineNumber(), "Invalid right hand side of variable declaration");
			return null;
		}
		
		if (lhs instanceof BaseType) {
			if (!(rhs instanceof BaseType)) {
				ErrorReporter.get().typeError(stmt.posn.getLineNumber(), "initializing expression type (" + rhs.typeKind + ") does not match base variable type!");
				return null;
			}
			if (lhs.typeKind != rhs.typeKind) {
				ErrorReporter.get().typeError(stmt.posn.getLineNumber(), "initializing expression type (" + rhs.typeKind + ") does not match variable type (" + lhs.typeKind + ")!");
				return null;
			}
		} else if (lhs instanceof ClassType) {
			if (rhs.typeKind == TypeKind.NULL) {
				return null;
			}
			if (!(rhs instanceof ClassType)) {
				ErrorReporter.get().typeError(stmt.posn.getLineNumber(), "initializing expression doesn't match expected type");
				return null;
			}
			String l = ((ClassType) lhs).className.spelling;
			String r = ((ClassType) rhs).className.spelling;
			if (!l.contentEquals(r)) {
				ErrorReporter.get().typeError(stmt.posn.getLineNumber(), "expected '" + l + "' but got '" + r + "'.");
			}
		} else if (lhs instanceof ArrayType) {
			if (rhs.typeKind == TypeKind.NULL)
				return null;
			if (!(rhs instanceof ArrayType)) {
				ErrorReporter.get().typeError(stmt.posn.getLineNumber(), "initializing expression needed to be of type ARRAY");
				return null;
			}
			ArrayType l = (ArrayType) lhs;
			ArrayType r = (ArrayType) rhs;
			
			if (l.eltType.typeKind == TypeKind.INT) {
				if (r.eltType.typeKind != TypeKind.INT) {
					ErrorReporter.get().typeError(stmt.posn.getLineNumber(), "initializing expression is not of expected type INT[]");
				}
				return null;
			}
			
			if (!(r.eltType instanceof ClassType)) {
				ErrorReporter.get().typeError(stmt.posn.getLineNumber(), "expected array to be of class type element");
				return null;
			}
			ClassType lt = (ClassType) l.eltType;
			ClassType rt = (ClassType) r.eltType;
			if (!lt.className.spelling.contentEquals(rt.className.spelling)) {
				ErrorReporter.get().typeError(stmt.posn.getLineNumber(), "expected elements of type '" + lt.className.spelling + "' but got '" + rt.className.spelling + "'.");
				return null;
			}	
		}
		return null;
	}

	@Override
	public TypeDenoter visitAssignStmt(AssignStmt stmt, Object arg) {
		Declaration d = stmt.ref.getDeclaration();
		if (d instanceof MethodDecl) {
			ErrorReporter.get().typeError(stmt.ref.posn.getLineNumber(), "cannot assign to a method");
			return null;
		}
		if (d instanceof ClassDecl) {
			ErrorReporter.get().typeError(stmt.ref.posn.getLineNumber(), "cannot assign to a class");
			return null;
		}
		
		// Cannot reassign array length
		if (stmt.ref instanceof QualRef) {
			QualRef ref= (QualRef) stmt.ref;
			if (ref.ref.getDeclaration().type instanceof ArrayType) {
				if (ref.id.spelling.contentEquals("length"))
					ErrorReporter.get().typeError(ref.posn.getLineNumber(), "Cannot reassign the length property of an array");
			}
		}
		
		TypeDenoter expectedType = stmt.ref.visit(this, arg);
		TypeDenoter actualType = stmt.val.visit(this, expectedType);
		if (expectedType == null || actualType == null) {
			ErrorReporter.get().typeError(stmt.val.posn.getLineNumber(), "Invalid right hand side of variable declaration");
			return null;
		}
		
		if ((expectedType instanceof ClassType || expectedType instanceof ArrayType) && actualType.typeKind == TypeKind.NULL)
			return null;
		if (!this.typesAreEqual(expectedType, actualType)) {
			ErrorReporter.get().typeError(stmt.val.posn.getLineNumber(), "expected type " + expectedType + " but got " + actualType);
		}
		return null;
	}

	@Override
	public TypeDenoter visitIxAssignStmt(IxAssignStmt stmt, Object arg) {
		TypeDenoter ref = stmt.ref.visit(this, arg);
		if (!(ref instanceof ArrayType)) {
			ErrorReporter.get().typeError(stmt.ref.posn.getLineNumber(), "cannot attempt to index a non-array structure.");
			return null;
		}
		
		TypeDenoter idx = stmt.ix.visit(this, arg);
		if (idx == null) {
			ErrorReporter.get().typeError(stmt.ix.posn.getLineNumber(), "Invalid index of array");
			return null;
		}
		if (idx.typeKind != TypeKind.INT) {
			ErrorReporter.get().typeError(stmt.ix.posn.getLineNumber(), "index to an array must be of type integer but got " + idx.typeKind + ".");
			return null;
		}
		
		TypeDenoter exprType = stmt.exp.visit(this, ((ArrayType) ref).eltType);
		
		if (exprType == null) {
			ErrorReporter.get().typeError(stmt.exp.posn.getLineNumber(), "Invalid right hand side of variable declaration");
			return null;
		}
		
		// Null can be assigned
		if (ref.typeKind == TypeKind.CLASS || ref.typeKind == TypeKind.ARRAY) {
			if (exprType.typeKind == TypeKind.NULL)
				return null;
		}
		
		if (!this.typesAreEqual(((ArrayType) ref).eltType, exprType)) {
			ErrorReporter.get().typeError(stmt.posn.getLineNumber(), "incompatible types for indexed assign statement");
		}
		
		return null;		
	}

	@Override
	public TypeDenoter visitCallStmt(CallStmt stmt, Object arg) {
		if (!(stmt.methodRef.getDeclaration() instanceof MethodDecl)) {
			ErrorReporter.get().typeError(stmt.methodRef.posn.getLineNumber(), "reference in a call statement must point to a function.");
			return null;
		}
		MethodDecl md = (MethodDecl) stmt.methodRef.getDeclaration();
		ParameterDeclList expectedArgs = md.parameterDeclList;
		ExprList actualArgs = stmt.argList;
		
		if (expectedArgs.size()  != actualArgs.size()) {
			ErrorReporter.get().typeError(stmt.methodRef.posn.getLineNumber(), "expected " + expectedArgs.size() + " args but got " + actualArgs.size());
			return null;
		}
		
		// Type check each of the parameters
		for (int i = 0; i < expectedArgs.size(); i++) {
			TypeDenoter expect = expectedArgs.get(i).type;
			TypeDenoter actual = actualArgs.get(i).visit(this, expect);
			if (!this.typesAreEqual(expect, actual)) {
				ErrorReporter.get().typeError(actualArgs.get(i).posn.getLineNumber(), "expected parameter of type " + expect.typeKind + " at position " + (i+1));
			}
		}
		
		return null;
	}

	@Override
	public TypeDenoter visitReturnStmt(ReturnStmt stmt, Object arg) {
		if (arg == null) {
			ErrorReporter.get().typeError(stmt.posn.getLineNumber(), "Internal Error...");
			return null;
		}
	
		MethodDecl md = (MethodDecl) arg;
		
		if (md.type.typeKind == TypeKind.VOID) {
			if (stmt.returnExpr != null) {
				ErrorReporter.get().typeError(stmt.returnExpr.posn.getLineNumber(), "void methods cannot have a return value");
			}
			return null;
		}
		
		if (stmt.returnExpr == null) {
			ErrorReporter.get().typeError(stmt.posn.getLineNumber(), "non-void methods must return a value");
			return md.type;
		}
		
		TypeDenoter rt = stmt.returnExpr.visit(this, md.type);
 		if (rt == null) {
			ErrorReporter.get().typeError(stmt.returnExpr.posn.getLineNumber(), "unable to resolve type on return expression");
			return md.type;
 		}

		if  (md.type.typeKind != rt.typeKind) {
			ErrorReporter.get().typeError(stmt.returnExpr.posn.getLineNumber(), "expected return type of " + md.type.typeKind + " but got " + rt.typeKind);
			return md.type;
		}
		if (md.type.typeKind == TypeKind.CLASS) {
			String l = ((ClassType) md.type).className.spelling;
			String r = ((ClassType) rt).className.spelling;
			if (!l.contentEquals(r)) {
				ErrorReporter.get().typeError(stmt.posn.getLineNumber(), "expected return type of '" + l + "' but got '" + r + "'");
			}
		} else if (md.type.typeKind == TypeKind.ARRAY) {
			ArrayType lhs = (ArrayType) md.type;
			ArrayType rhs = (ArrayType) rt;
			if (lhs.eltType.typeKind != rhs.eltType.typeKind) {
				ErrorReporter.get().typeError(stmt.posn.getLineNumber(), "expected an array of type " + lhs.eltType.typeKind + " but got " + rhs.eltType.typeKind);
				return md.type;
			}
			if (lhs.eltType.typeKind == TypeKind.CLASS) {
				String l = ((ClassType) lhs.eltType).className.spelling;
				String r = ((ClassType) rhs.eltType).className.spelling;
				if (!l.contentEquals(r)) {
					ErrorReporter.get().typeError(stmt.posn.getLineNumber(), "expected an array of elements of type " + l + " but got " + r);
				}
			}
		}
		
		return null;
	}

	@Override
	public TypeDenoter visitIfStmt(IfStmt stmt, Object arg) {
		TypeDenoter cond = stmt.cond.visit(this, new BaseType(TypeKind.BOOLEAN, dummyPos));
		if (cond.typeKind != TypeKind.BOOLEAN) {
			ErrorReporter.get().typeError(stmt.posn.getLineNumber(), "expected type BOOLEAN but got " + cond.typeKind);
			return null;
		}
		
		//A variable declaration cannot be the solitary statement in a branch of a conditional statement.
		if (stmt.thenStmt instanceof VarDeclStmt) {
			ErrorReporter.get().typeError(stmt.thenStmt.posn.getLineNumber(), "a variable declaration cannot be the solitary statement in a branch of a conditional statement");
		} 
		stmt.thenStmt.visit(this, arg);

		
		if (stmt.elseStmt != null) {
			stmt.elseStmt.visit(this, arg);
			if (stmt.elseStmt instanceof VarDeclStmt) {
				ErrorReporter.get().typeError(stmt.elseStmt.posn.getLineNumber(), "a variable declaration cannot be the solitary statement in a branch of a conditional statement");

			}
		}
		return null;
	}

	@Override
	public TypeDenoter visitWhileStmt(WhileStmt stmt, Object arg) {
		// Check that the condition is a boolean type
		TypeDenoter cond = stmt.cond.visit(this, new BaseType(TypeKind.BOOLEAN, dummyPos));
		if (cond.typeKind != TypeKind.BOOLEAN) {
			ErrorReporter.get().typeError(stmt.posn.getLineNumber(), "expected type BOOLEAN but got " + cond.typeKind);
		}
		
		// TypeCheck the body
		stmt.body.visit(this, arg);
		
		//A variable declaration cannot be the solitary statement in a branch of a conditional statement.
		if (stmt.body instanceof VarDeclStmt) {
			ErrorReporter.get().typeError(stmt.body.posn.getLineNumber(), "a variable declaration cannot be the solitary statement in a branch of a conditional statement");
		}
			
		return null;
	}

	@Override
	public TypeDenoter visitUnaryExpr(UnaryExpr expr, Object arg) {
		TypeDenoter expect = null;
		if (expr.operator.spelling.contentEquals("-")) {
			expect = new BaseType(TypeKind.INT, dummyPos);
		} else {
			expect = new BaseType(TypeKind.BOOLEAN, dummyPos);
		}
		TypeDenoter et = expr.expr.visit(this, expect == null ? arg : expect);
		
		if (expr.operator.spelling.contentEquals("-")) {
			if (et.typeKind != TypeKind.INT) {
				ErrorReporter.get().typeError(expr.posn.getLineNumber(), "type error for operator '" + expr.operator.spelling + "' expected INT but got " + et.typeKind);
			}
			return new BaseType(TypeKind.INT, dummyPos);
		}
		
		if (expr.operator.spelling.contentEquals("!")) {
			if (et.typeKind != TypeKind.BOOLEAN) {
				ErrorReporter.get().typeError(expr.posn.getLineNumber(), "type error for operator '" + expr.operator.spelling + "' expected BOOLEAN but got " + et.typeKind);
			}
			return new BaseType(TypeKind.BOOLEAN, dummyPos);
		}
		
		return null;
	}

	@Override
	public TypeDenoter visitBinaryExpr(BinaryExpr expr, Object arg) {
		TypeDenoter expectedOperandType = new BaseType(TypeKind.INT, dummyPos);
		String o = expr.operator.spelling;
		if (o.contentEquals("+") || o.contentEquals("-") || o.contentEquals("*") || o.contentEquals("/")) {
			expectedOperandType = new BaseType(TypeKind.INT, dummyPos);
		} else if (o.contentEquals("<") || o.contentEquals("<=") || o.contentEquals(">") || o.contentEquals(">=")) {
			expectedOperandType = new BaseType(TypeKind.INT, dummyPos);
		} else if (o.contentEquals("||") || o.contentEquals("&&")) {
			expectedOperandType = new BaseType(TypeKind.BOOLEAN, dummyPos);
		}
		
		TypeDenoter lhs = expr.left.visit(this, expectedOperandType);
		TypeDenoter rhs = expr.right.visit(this, expectedOperandType);     
		
		if (lhs == null || rhs == null) {
			ErrorReporter.get().typeError(expr.posn.getLineNumber(), "Unable to type check binary expr");
			if (o.contentEquals("+") || o.contentEquals("-") || o.contentEquals("*") || o.contentEquals("/")) {
				return new BaseType(TypeKind.INT, dummyPos);
			} else if (o.contentEquals("<") || o.contentEquals("<=") || o.contentEquals(">") || o.contentEquals(">=")) {
				return new BaseType(TypeKind.BOOLEAN, dummyPos);
			} else if (o.contentEquals("||") || o.contentEquals("&&")) {
				return new BaseType(TypeKind.BOOLEAN, dummyPos);
			} else {
				return new BaseType(TypeKind.BOOLEAN, dummyPos);
			}
		}

		// INT x INT -> INT
		if (expr.operator.spelling.contentEquals("+") ||
				expr.operator.spelling.contentEquals("-") || 
				expr.operator.spelling.contentEquals("*") ||
				expr.operator.spelling.contentEquals("/")) {
			if (lhs.typeKind != TypeKind.INT || rhs.typeKind != TypeKind.INT) {
				ErrorReporter.get().typeError(expr.posn.getLineNumber(), "type error for operator '" + expr.operator.spelling + "' expected INT X INT but got " + lhs.typeKind + " X " + rhs.typeKind);
			}
			return new BaseType(TypeKind.INT, dummyPos);
		}
		
		// INT x INT -> BOOLEAN
		if (expr.operator.spelling.contentEquals("<") || 
				expr.operator.spelling.contentEquals("<=")||
				expr.operator.spelling.contentEquals(">") ||
				expr.operator.spelling.contentEquals(">=")) {
			if (lhs.typeKind != TypeKind.INT || rhs.typeKind != TypeKind.INT) {
				ErrorReporter.get().typeError(expr.posn.getLineNumber(), "type error for operator '" + expr.operator.spelling + "' expected INT X INT but got " + lhs.typeKind + " X " + rhs.typeKind);
			}
			return new BaseType(TypeKind.BOOLEAN, dummyPos);
		}
		
		// BOOLEAN x BOOLEAN -> BOOLEAN
		if (expr.operator.spelling.contentEquals("||") || expr.operator.spelling.contentEquals("&&")) {
			if (lhs.typeKind != TypeKind.BOOLEAN || rhs.typeKind != TypeKind.BOOLEAN) {
				ErrorReporter.get().typeError(expr.posn.getLineNumber(), "type error for operator '" + expr.operator.spelling + "' expected BOOLEAN X BOOLEAN but got " + lhs.typeKind + " X " + rhs.typeKind);
			}
			return new BaseType(TypeKind.BOOLEAN, dummyPos);
		}
		
		// Overloaded
		if (expr.operator.spelling.contentEquals("==") || expr.operator.spelling.contentEquals("!=")) {
			if (lhs == null || rhs == null || lhs.typeKind == TypeKind.METHOD || rhs.typeKind == TypeKind.METHOD) {
				ErrorReporter.get().typeError(expr.posn.getLineNumber(), "Invalid use of operator " + expr.operator.spelling);
				return new BaseType(TypeKind.BOOLEAN, dummyPos);
			}
			// INT X INT -> BOOLEAN
			if (lhs.typeKind == TypeKind.INT) {
				if (rhs.typeKind != TypeKind.INT) {
					ErrorReporter.get().typeError(expr.posn.getLineNumber(), "Expected INT x INT but got INT x " + rhs.typeKind);
				}
				return new BaseType(TypeKind.BOOLEAN, dummyPos);
			}
			// BOOLEAN x BOOLEAN -> BOOLEAN
			if (lhs.typeKind == TypeKind.BOOLEAN) {
				if (rhs.typeKind != TypeKind.BOOLEAN) {
					ErrorReporter.get().typeError(expr.posn.getLineNumber(), "Expected BOOLEAN x BOOLEAN but got BOOLEAN x " + rhs.typeKind);
				}
				return new BaseType(TypeKind.BOOLEAN, dummyPos);
			}
			
			
			// REFERENCE x REFERENCE -> BOOLEAN
			if (lhs.typeKind != TypeKind.CLASS && lhs.typeKind != TypeKind.ARRAY && lhs.typeKind != TypeKind.NULL) {
				ErrorReporter.get().typeError(expr.posn.getLineNumber(), "Invalid use of operator " + expr.operator.spelling);
				return new BaseType(TypeKind.BOOLEAN, dummyPos);
			}
			if (rhs.typeKind != TypeKind.CLASS && rhs.typeKind != TypeKind.ARRAY && rhs.typeKind != TypeKind.NULL) {
				ErrorReporter.get().typeError(expr.posn.getLineNumber(), "Invalid use of operator " + expr.operator.spelling);
				return new BaseType(TypeKind.BOOLEAN, dummyPos);
			}
			
			if (lhs.typeKind == TypeKind.NULL || rhs.typeKind == TypeKind.NULL) {
				return new BaseType(TypeKind.BOOLEAN, dummyPos);
			}
			
			if (!this.typesAreEqual(lhs,rhs)) {
				ErrorReporter.get().typeError(expr.posn.getLineNumber(), "Invalid Type Comparison for ==/!=");
			}
			
			return new BaseType(TypeKind.BOOLEAN, dummyPos);
		}
		return null;
	}

	@Override
	public TypeDenoter visitRefExpr(RefExpr expr, Object arg) {
		// Pointing to a Method
		if (expr.ref.getDeclaration() instanceof MethodDecl) {
			return new BaseType(TypeKind.METHOD, dummyPos);
		}
		return expr.ref.visit(this, arg);
	}

	@Override
	public TypeDenoter visitIxExpr(IxExpr expr, Object arg) {
		TypeDenoter ref = expr.ref.visit(this, arg);
		if (!(ref instanceof ArrayType)) {
			ErrorReporter.get().typeError(expr.ref.posn.getLineNumber(), "cannot attempt to index a non-array structure");
			return (TypeDenoter) arg; 
		}
		
		TypeDenoter idx = expr.ixExpr.visit(this, arg);
		if (idx.typeKind != TypeKind.INT) {
			ErrorReporter.get().typeError(expr.ixExpr.posn.getLineNumber(), "index to an array must be of type integer but got " + idx.typeKind);
			return ((ArrayType) ref).eltType; 
		}
		
		return ((ArrayType) ref).eltType;
	}

	@Override
	public TypeDenoter visitCallExpr(CallExpr expr, Object arg) {
		if (!(expr.functionRef.getDeclaration() instanceof MethodDecl)) {
			ErrorReporter.get().typeError(expr.functionRef.posn.getLineNumber(), "reference in a call expression must point to a function");
			return (TypeDenoter) arg; 
		}
		MethodDecl md = (MethodDecl) expr.functionRef.getDeclaration();
		ParameterDeclList expectedArgs = md.parameterDeclList;
		ExprList actualArgs = expr.argList;
		
		if (expectedArgs.size()  != actualArgs.size()) {
			ErrorReporter.get().typeError(expr.functionRef.posn.getLineNumber(), "expected " + expectedArgs.size() + " args but got " + actualArgs.size());
			return expr.functionRef.getDeclaration().type;
		}
		
		// Type check each of the parameters
		for (int i = 0; i < expectedArgs.size(); i++) {
			TypeDenoter expect = expectedArgs.get(i).type;
			TypeDenoter actual = actualArgs.get(i).visit(this, expect);
			if (!this.typesAreEqual(expect, actual)) {
				ErrorReporter.get().typeError(actualArgs.get(i).posn.getLineNumber(), "expected parameter of type " + expect.typeKind + " at position " + (i+1));
			}
		}
		
		return expr.functionRef.getDeclaration().type;
	}

	@Override
	public TypeDenoter visitLiteralExpr(LiteralExpr expr, Object arg) {
		return expr.lit.visit(this, arg);
	}

	@Override
	public TypeDenoter visitNewObjectExpr(NewObjectExpr expr, Object arg) {
		return expr.classtype;	
	}

	@Override
	public TypeDenoter visitNewArrayExpr(NewArrayExpr expr, Object arg) {
		TypeDenoter sizeEx = expr.sizeExpr.visit(this, arg);
		if (!(sizeEx instanceof BaseType) || sizeEx.typeKind != TypeKind.INT) {
			ErrorReporter.get().typeError(expr.posn.getLineNumber(), "array size expression expected to be of type INT but got " + sizeEx.typeKind);
		}
		return new ArrayType(expr.eltType, dummyPos);
	}

	@Override
	public TypeDenoter visitThisRef(ThisRef ref, Object arg) {
		ClassDecl cd = (ClassDecl) ref.getDeclaration();
		return new ClassType(new Identifier(new Token(TokenKind.IDENTIFIER, cd.name, null)), dummyPos);
	}

	@Override
	public TypeDenoter visitIdRef(IdRef ref, Object arg) {
		return ref.getDeclaration().type;
	}

	@Override
	public TypeDenoter visitQRef(QualRef ref, Object arg) {
		// Pointing to a Method
		if (ref.getDeclaration() instanceof MethodDecl) {
			return new BaseType(TypeKind.METHOD, dummyPos);
		}
		
		// Attempting to access array length
		if (ref.ref.getDeclaration().type instanceof ArrayType) {
			return new BaseType(TypeKind.INT, dummyPos);
		}
		return ref.getDeclaration().type;
	}

	@Override
	public TypeDenoter visitIdentifier(Identifier id, Object arg) {		
		return null;
	}

	@Override
	public TypeDenoter visitOperator(Operator op, Object arg) {
		return null;
	}

	@Override
	public TypeDenoter visitIntLiteral(IntLiteral num, Object arg) {
		return new BaseType(TypeKind.INT, dummyPos);
	}

	@Override
	public TypeDenoter visitBooleanLiteral(BooleanLiteral bool, Object arg) {
		return new BaseType(TypeKind.BOOLEAN, dummyPos);
	}

	@Override
	public TypeDenoter visitNullLiteral(NullLiteral nullLiteral, Object arg) {
		return new BaseType(TypeKind.NULL, dummyPos);
	}
	
	private boolean typesAreEqual(TypeDenoter a, TypeDenoter b) {
		if (a == null || b == null)
			return false;
		
		if  (a.typeKind != b.typeKind) {
			return false;
		}
		
		if (a.typeKind == TypeKind.CLASS) {
			String l = ((ClassType) a).className.spelling;
			String r = ((ClassType) b).className.spelling;
			if (!l.contentEquals(r)) {
				return false;
			}
		} else if (a.typeKind == TypeKind.ARRAY) {
			ArrayType lhs = (ArrayType) a;
			ArrayType rhs = (ArrayType) b;
			if (lhs.eltType.typeKind != rhs.eltType.typeKind) {
				return false;
			}
			if (lhs.eltType.typeKind == TypeKind.CLASS) {
				String l = ((ClassType) lhs.eltType).className.spelling;
				String r = ((ClassType) rhs.eltType).className.spelling;
				if (!l.contentEquals(r)) {
					return false;
				}
			}
		}
		return true;
	}
}
