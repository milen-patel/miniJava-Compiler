package miniJava.ContextualAnalyzer;

import java.util.HashMap;
import java.util.Map;

import miniJava.ErrorReporter;
import miniJava.AbstractSyntaxTrees.*;
import miniJava.AbstractSyntaxTrees.Package;

public class Identification implements miniJava.AbstractSyntaxTrees.Visitor<Object, Object>{
	IdentificationTable table = new IdentificationTable();
	
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
		
		// Visit all the variable declarations, make sure none are already defined
		FieldDeclList varDecls = cd.fieldDeclList;
		for (int i = 0; i < varDecls.size(); i++) {
			String vName = varDecls.get(i).name;
			FieldDecl vDecl = varDecls.get(i);
			
			// Check if variable already defined
			if (table.containsKeyAtTopScope(vName)) {
				System.out.println("*** line " + vDecl.posn.getLineNumber() + ": Duplicate class variable declaration error. Variable " + vName + " has already been defined.");
			}
			table.add(vName, vDecl);
		}
		
		// Visit each of the variable types
		for (int i = 0; i < varDecls.size(); i++) {
			varDecls.get(i).visit(this, arg);
		}
		
		// Visit all the method declarations, make sure none are already defined
		
		
		table.closeScope();
		return null;
	}

	@Override
	public Object visitFieldDecl(FieldDecl decl, Object arg) {
		TypeDenoter declType = decl.type;
		
		if (declType instanceof ClassType) {
			// If the variable is of type class, then make sure the class exists
			ClassType ct = (ClassType) declType;
			if (!this.table.classesTable.containsKey(ct.className.spelling)) {
				System.out.println("*** line " + decl.posn.getLineNumber() + ": Unknown class type '" + ct.className.spelling + "'.");
			}
			
			// If the class exists, make the declaration point to the class declaration
			ct.className.setDecalaration(table.classesTable.get(ct.className.spelling));
		} else if (declType instanceof ArrayType) {
			System.out.println("Array Type");
			ArrayType at = (ArrayType) declType;
			TypeDenoter elementType = at.eltType;
			
			// For array variables, we only need to be cautious if the element type is non-basic
			if (elementType instanceof ClassType) {
				ClassType ct = (ClassType) elementType;
				if (!this.table.classesTable.containsKey(ct.className.spelling)) {
					System.out.println("*** line " + decl.posn.getLineNumber() + ": Unknown array element type '" + ct.className.spelling + "'.");
				}
				ct.className.setDecalaration(table.classesTable.get(ct.className.spelling));
			}
		} else {
			if (!(declType instanceof BaseType)) {
				throw new RuntimeException("TODO");
			}
		}
		
		return arg;
	}

	@Override
	public Object visitMethodDecl(MethodDecl md, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitParameterDecl(ParameterDecl pd, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitVarDecl(VarDecl decl, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitBaseType(BaseType type, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitClassType(ClassType type, Object arg) {
		ErrorReporter.get().log("Visiting A Class Denoter Type", 5);
		return null;
	}

	@Override
	public Object visitArrayType(ArrayType type, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitBlockStmt(BlockStmt stmt, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitVardeclStmt(VarDeclStmt stmt, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitAssignStmt(AssignStmt stmt, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitIxAssignStmt(IxAssignStmt stmt, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitCallStmt(CallStmt stmt, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitReturnStmt(ReturnStmt stmt, Object arg) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitBinaryExpr(BinaryExpr expr, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitRefExpr(RefExpr expr, Object arg) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitIdRef(IdRef ref, Object arg) {
		// TODO Auto-generated method stub
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
