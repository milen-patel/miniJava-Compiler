package miniJava.ContextualAnalyzer;

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
		return null;
	}

	@Override
	public TypeDenoter visitParameterDecl(ParameterDecl pd, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeDenoter visitVarDecl(VarDecl decl, Object arg) {
		return decl.type;
	}

	@Override
	public TypeDenoter visitBaseType(BaseType type, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeDenoter visitClassType(ClassType type, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeDenoter visitArrayType(ArrayType type, Object arg) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		TypeDenoter lhs = stmt.varDecl.visit(this, arg);
		TypeDenoter rhs = stmt.initExp.visit(this, arg);
		
		if (lhs instanceof BaseType) {
			if (!(rhs instanceof BaseType)) {
				System.out.println("*** line " + stmt.posn.getLineNumber() + ": initializing expression type (" + rhs.typeKind + ") does not match base variable type!");
				return null;
			}
			if (lhs.typeKind != rhs.typeKind) {
				System.out.println("*** line " + stmt.posn.getLineNumber() + ": initializing expression type (" + rhs.typeKind + ") does not match variable type (" + lhs.typeKind + ")!");
				return null;
			}
		} else if (lhs instanceof ClassType) {
			if (!(rhs instanceof ClassType)) {
				System.out.println("*** line " + stmt.posn.getLineNumber() + ": initializing expression doesn't match expected type");
				return null;
			}
			String l = ((ClassType) lhs).className.spelling;
			String r = ((ClassType) rhs).className.spelling;
			if (!l.contentEquals(r)) {
				System.out.println("*** line " + stmt.posn.getLineNumber() + ": expected '" + l + "' but got '" + r + "'.");
			}
		} else if (lhs instanceof ArrayType) {
			// TODO
			if (!(rhs instanceof ArrayType)) {
				System.out.println("*** line " + stmt.posn.getLineNumber() + ": initializing expression needed to be of type ARRAY");
				return null;
			}
			ArrayType l = (ArrayType) lhs;
			ArrayType r = (ArrayType) rhs;
			
			if (l.eltType.typeKind == TypeKind.INT) {
				if (r.eltType.typeKind != TypeKind.INT) {
					System.out.println("*** line " + stmt.posn.getLineNumber() + ": initializing expression is not of expected type INT[]");
				}
				return null;
			}
			
			if (!(r.eltType instanceof ClassType)) {
				System.out.println("*** line " + stmt.posn.getLineNumber() + ": expected array to be of class type element");
				return null;
			}
			ClassType lt = (ClassType) l.eltType;
			ClassType rt = (ClassType) r.eltType;
			if (!lt.className.spelling.contentEquals(rt.className.spelling)) {
				System.out.println("*** line " + stmt.posn.getLineNumber() + ": expected elements of type '" + lt.className.spelling + "' but got '" + rt.className.spelling + "'.");
				return null;
			}
			
		} else {
			// TODO error out
		}
		return null;
	}

	@Override
	public TypeDenoter visitAssignStmt(AssignStmt stmt, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeDenoter visitIxAssignStmt(IxAssignStmt stmt, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeDenoter visitCallStmt(CallStmt stmt, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeDenoter visitReturnStmt(ReturnStmt stmt, Object arg) {
		if (arg == null) {
			System.out.println("Internal Error..."); // TODO
		}
		MethodDecl md = (MethodDecl) arg;
		
		
		if (md.type.typeKind == TypeKind.VOID) {
			if (stmt.returnExpr != null) {
				System.out.println("*** line " + stmt.returnExpr.posn.getLineNumber() + ": void methods cannot have a return value");
			}
			return null;
		}
		
		if (stmt.returnExpr == null) {
			System.out.println("*** line " + stmt.posn.getLineNumber() + ": non-void methods must return a value.");
			return md.type;
		}
		
		TypeDenoter rt = stmt.returnExpr.visit(this, null);
 		
		// TODO this is a temp fix that deals with returning a class name need to investigate, visitRefExpr
 		if (rt == null) {
			System.out.println("*** line " + stmt.returnExpr.posn.getLineNumber() + ": unable to resolve type on return expression.");
			return md.type;
 		}

		if  (md.type.typeKind != rt.typeKind) {
			System.out.println("*** line " + stmt.returnExpr.posn.getLineNumber() + ": expected return type of " + md.type.typeKind + " but got " + rt.typeKind);
			return md.type;
		}
		if (md.type.typeKind == TypeKind.CLASS) {
			String l = ((ClassType) md.type).className.spelling;
			String r = ((ClassType) rt).className.spelling;
			if (!l.contentEquals(r)) {
				System.out.println("*** line " + stmt.posn.getLineNumber() + ": expected return type of '" + l + "' but got '" + r + "'.");
			}
		} else if (md.type.typeKind == TypeKind.ARRAY) {
			ArrayType lhs = (ArrayType) md.type;
			ArrayType rhs = (ArrayType) rt;
			if (lhs.eltType.typeKind != rhs.eltType.typeKind) {
				System.out.println("*** line " + stmt.posn.getLineNumber() + ": expected an array of type " + lhs.eltType.typeKind + " but got " + rhs.eltType.typeKind);
				return md.type;
			}
			if (lhs.eltType.typeKind == TypeKind.CLASS) {
				String l = ((ClassType) lhs.eltType).className.spelling;
				String r = ((ClassType) rhs.eltType).className.spelling;
				if (!l.contentEquals(r)) {
					System.out.println("*** line " + stmt.posn.getLineNumber() + ": expected an array of elements of type " + l + " but got " + r);
				}
			}
		}
		
		return null;
	}

	@Override
	public TypeDenoter visitIfStmt(IfStmt stmt, Object arg) {
		TypeDenoter cond = stmt.cond.visit(this, arg);
		if (cond.typeKind != TypeKind.BOOLEAN) {
			System.out.println("*** line " + stmt.posn.getLineNumber() + ": expected type BOOLEAN but got " + cond.typeKind);
			return null;
		}
		stmt.thenStmt.visit(this, arg);
		
		//A variable declaration cannot be the solitary statement in a branch of a conditional statement.
		if (stmt.thenStmt instanceof VarDeclStmt) {
			System.out.println("*** line " + stmt.thenStmt.posn.getLineNumber() + ": a variable declaration cannot be the solitary statement in a branch of a conditional statement.");

		} else if (stmt.thenStmt instanceof BlockStmt) {
			BlockStmt s = (BlockStmt) stmt.thenStmt;
			if (s.sl.size() == 1 && s.sl.get(0) instanceof VarDeclStmt) {
				System.out.println("*** line " + stmt.thenStmt.posn.getLineNumber() + ": a variable declaration cannot be the solitary statement in a branch of a conditional statement.");
			}
		}
		
		if (stmt.elseStmt != null) {
			stmt.elseStmt.visit(this, arg);
			
			if (stmt.elseStmt instanceof VarDeclStmt) {
				System.out.println("*** line " + stmt.elseStmt.posn.getLineNumber() + ": a variable declaration cannot be the solitary statement in a branch of a conditional statement.");

			} else if (stmt.elseStmt instanceof BlockStmt) {
				BlockStmt s = (BlockStmt) stmt.elseStmt;
				if (s.sl.size() == 1 && s.sl.get(0) instanceof VarDeclStmt) {
					System.out.println("*** line " + s.sl.get(0).posn.getLineNumber() + ": a variable declaration cannot be the solitary statement in a branch of a conditional statement.");
				}
			}
		}
		return null;
	}

	@Override
	public TypeDenoter visitWhileStmt(WhileStmt stmt, Object arg) {
		// Check that the condition is a boolean type
		TypeDenoter cond = stmt.cond.visit(this, arg);
		if (cond.typeKind != TypeKind.BOOLEAN) {
			System.out.println("*** line " + stmt.posn.getLineNumber() + ": expected type BOOLEAN but got " + cond.typeKind);
		}
		
		// TypeCheck the body
		stmt.body.visit(this, arg);
		
		//A variable declaration cannot be the solitary statement in a branch of a conditional statement.
		if (stmt.body instanceof VarDeclStmt) {
			System.out.println("*** line " + stmt.body.posn.getLineNumber() + ": a variable declaration cannot be the solitary statement in a branch of a conditional statement.");
		} else if (stmt.body instanceof BlockStmt) {
			BlockStmt s = (BlockStmt) stmt.body;
			if (s.sl.size() == 1 && s.sl.get(0) instanceof VarDeclStmt) {
				System.out.println("*** line " + s.sl.get(0).posn.getLineNumber() + ": a variable declaration cannot be the solitary statement in a branch of a conditional statement.");
			}
		}
			
		return null;
	}

	@Override
	public TypeDenoter visitUnaryExpr(UnaryExpr expr, Object arg) {
		TypeDenoter et = expr.expr.visit(this, arg);
		
		if (expr.operator.spelling.contentEquals("-")) {
			if (et.typeKind != TypeKind.INT) {
				System.out.println("*** line " + expr.posn.getLineNumber() + " type error for operator '" + expr.operator.spelling + "' expected INT but got " + et.typeKind);
			}
			return new BaseType(TypeKind.INT, dummyPos);
		}
		
		if (expr.operator.spelling.contentEquals("!")) {
			if (et.typeKind != TypeKind.BOOLEAN) {
				System.out.println("*** line " + expr.posn.getLineNumber() + " type error for operator '" + expr.operator.spelling + "' expected BOOLEAN but got " + et.typeKind);
			}
			return new BaseType(TypeKind.BOOLEAN, dummyPos);
		}
		
		// TODO error out
		return null;
	}

	@Override
	public TypeDenoter visitBinaryExpr(BinaryExpr expr, Object arg) {
		TypeDenoter lhs = expr.left.visit(this, arg);
		TypeDenoter rhs = expr.right.visit(this, arg);         

		// INT x INT -> INT
		if (expr.operator.spelling.contentEquals("+") ||
				expr.operator.spelling.contentEquals("-") || 
				expr.operator.spelling.contentEquals("*") ||
				expr.operator.spelling.contentEquals("/")) {
			if (lhs.typeKind != TypeKind.INT || rhs.typeKind != TypeKind.INT) {
				System.out.println("*** line " + expr.posn.getLineNumber() + " type error for operator '" + expr.operator.spelling + "' expected INT X INT but got " + lhs.typeKind + " X " + rhs.typeKind);
				// TODO so should we return a error type or just return the expected type
			}
			return new BaseType(TypeKind.INT, dummyPos);
		}
		
		// INT x INT -> BOOLEAN
		if (expr.operator.spelling.contentEquals("<") || 
				expr.operator.spelling.contentEquals("<=")||
				expr.operator.spelling.contentEquals(">") ||
				expr.operator.spelling.contentEquals(">=")) {
			if (lhs.typeKind != TypeKind.INT || rhs.typeKind != TypeKind.INT) {
				System.out.println("*** line " + expr.posn.getLineNumber() + " type error for operator '" + expr.operator.spelling + "' expected INT X INT but got " + lhs.typeKind + " X " + rhs.typeKind);
				// TODO so should we return a error type or just return the expected type
			}
			return new BaseType(TypeKind.BOOLEAN, dummyPos);
		}
		
		// BOOLEAN x BOOLEAN -> BOOLEAN
		if (expr.operator.spelling.contentEquals("||") || expr.operator.spelling.contentEquals("&&")) {
			if (lhs.typeKind != TypeKind.BOOLEAN || rhs.typeKind != TypeKind.BOOLEAN) {
				System.out.println("*** line " + expr.posn.getLineNumber() + " type error for operator '" + expr.operator.spelling + "' expected BOOLEAN X BOOLEAN but got " + lhs.typeKind + " X " + rhs.typeKind);
			}
			return new BaseType(TypeKind.BOOLEAN, dummyPos);
		}
		
		
		return null;
	}

	@Override
	public TypeDenoter visitRefExpr(RefExpr expr, Object arg) {
		// TODO something about thihs method is very wrong, how to deal with referecning classes statically
		if (expr.ref.getDeclaration() instanceof ClassDecl) {
		//	return new ClassType(new BaseType(TypeKind.CLASS, dummyPos), dummyPos);
		}
		return expr.ref.getDeclaration().type;//TODO double check
	}

	@Override
	public TypeDenoter visitIxExpr(IxExpr expr, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeDenoter visitCallExpr(CallExpr expr, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeDenoter visitLiteralExpr(LiteralExpr expr, Object arg) {
		expr.type = expr.lit.visit(this, arg);
		return expr.type; // TODO not sure if this is right
	}

	@Override
	public TypeDenoter visitNewObjectExpr(NewObjectExpr expr, Object arg) {
		return expr.classtype;
		// TODO figure out how to deal with this being used in a invalid context
	}

	@Override
	public TypeDenoter visitNewArrayExpr(NewArrayExpr expr, Object arg) {
		TypeDenoter sizeEx = expr.sizeExpr.visit(this, arg);
		if (!(sizeEx instanceof BaseType) || sizeEx.typeKind != TypeKind.INT) {
			System.out.println("*** line " + expr.posn.getLineNumber() + ":  array size expression expected to be of type INT but got " + sizeEx.typeKind);
		}
		return new ArrayType(expr.eltType, dummyPos);
	}

	@Override
	public TypeDenoter visitThisRef(ThisRef ref, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeDenoter visitIdRef(IdRef ref, Object arg) {
		// TODO Auto-generated method stub
		// Pointing to a Class
		if (ref.getDeclaration() instanceof ClassDecl) {
			ClassDecl cd = (ClassDecl) ref.getDeclaration();
			return cd.type;
		}
		// Pointing to a Variable
		if (ref.getDeclaration() instanceof FieldDecl) {
			FieldDecl fd = (FieldDecl) ref.getDeclaration();
			return fd.type;
		}
		// Pointing to a Method
		if (ref.getDeclaration() instanceof MethodDecl) {
			return new BaseType(TypeKind.METHOD, dummyPos);
		}
		return null;
	}

	@Override
	public TypeDenoter visitQRef(QualRef ref, Object arg) {
		// TODO Auto-generated method stub
		
		System.out.println(ref.getDeclaration());
		System.out.println(ref.id.getDeclaration()); //chheck that these are the same
		return null;
	}

	@Override
	public TypeDenoter visitIdentifier(Identifier id, Object arg) {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public TypeDenoter visitOperator(Operator op, Object arg) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return null;
	}

	

}
