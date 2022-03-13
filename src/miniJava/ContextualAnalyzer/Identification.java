package miniJava.ContextualAnalyzer;

import java.util.HashMap;
import java.util.Map;

import miniJava.ErrorReporter;
import miniJava.AbstractSyntaxTrees.*;
import miniJava.AbstractSyntaxTrees.Package;

public class Identification implements miniJava.AbstractSyntaxTrees.Visitor<Object, Object> {
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
			varDecls.get(i).visit(this, arg);
		}

		// Visit all the method declarations, make sure none are already defined
		MethodDeclList methodDecls = cd.methodDeclList;
		for (int i = 0; i < methodDecls.size(); i++) {
			methodDecls.get(i).visit(this, arg);
		}

		table.closeScope();
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

		table.add(md.name, md);
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
