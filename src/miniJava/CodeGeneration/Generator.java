package miniJava.CodeGeneration;

import java.util.HashSet;
import java.util.Set;

import mJAM.Machine;
import mJAM.Machine.Op;
import mJAM.Machine.Prim;
import mJAM.Machine.Reg;
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
import miniJava.AbstractSyntaxTrees.TypeKind;
import miniJava.AbstractSyntaxTrees.UnaryExpr;
import miniJava.AbstractSyntaxTrees.VarDecl;
import miniJava.AbstractSyntaxTrees.VarDeclStmt;
import miniJava.AbstractSyntaxTrees.Visitor;
import miniJava.AbstractSyntaxTrees.WhileStmt;
import miniJava.SyntacticAnalyzer.SourcePosition;

public class Generator implements Visitor<Object, Object> {
	Set<UnknownFunctionAddressRequest> s = new HashSet<UnknownFunctionAddressRequest>();

	public void generateCode(Package p) {
		Machine.initCodeGen();
		// First, verify existence of main method
		MethodDecl main = this.findEntryMethod(p);

		// Ensure all void methods end in a return
		this.appendReturnToVoidMethods(p);

		// Reserve space for static class fields
		int numStaticFields = 0;
		for (ClassDecl cd : p.classDeclList) {
			for (FieldDecl fd : cd.fieldDeclList) {
				if (fd.isStatic) {
					System.out.println("Static field " + fd.name + " of class " + cd.name + " assigned to address SB["
							+ numStaticFields + "]");
					fd.runtimeEntity = new RuntimeEntity(Reg.SB, numStaticFields);
					numStaticFields++;
				}
			}
		}
		Machine.emit(Op.PUSH, numStaticFields);

		// Emit Code to Call Main Function
		// Generate String[] args
		Machine.emit(Op.LOADL, 0);
		Machine.emit(Prim.newarr);
		s.add(new UnknownFunctionAddressRequest(Machine.nextInstrAddr(), main));
		Machine.emit(Op.CALL, Reg.CB, -1); // Call main(String[] args)
		Machine.emit(Op.POP, numStaticFields); // TODO is this needed
		Machine.emit(Op.HALT);

		// Generate Code for _PrintStream and System
		ClassDecl printStream = null, system = null;
		for (ClassDecl cd : p.classDeclList) {
			if (cd.name.contentEquals("_PrintStream")) {
				printStream = cd;
			} else if (cd.name.contentEquals("System")) {
				system = cd;
			}
		}

		MethodDecl println = printStream.methodDeclList.get(0);
		println.runtimeEntity = new RuntimeEntity(Reg.CB, Machine.nextInstrAddr());
		Machine.emit(Op.LOAD, Reg.LB, -1);
		Machine.emit(Prim.putintnl);
		Machine.emit(Op.RETURN, 0, 0, 1);

		p.visit(this, null);
		
		// Patch function call addresses
		for (UnknownFunctionAddressRequest curr : s) {
			curr.fix();
		}
	}

	@Override
	public Object visitPackage(Package prog, Object arg) {
		for (ClassDecl cd : prog.classDeclList) {
			if (cd.name.contentEquals("System"))
				continue;
			if (cd.name.contentEquals("_PrintStream"))
				continue;

			// Create Runtime Entities for each of the non-static class fields
			int fieldDisplacement = 0;
			for (FieldDecl fd : cd.fieldDeclList) {
				if (fd.isStatic)
					continue;
				fd.runtimeEntity = new RuntimeEntity(Reg.OB, fieldDisplacement++);
			}
		}

		// Now generate code for each of the class methods
		for (ClassDecl cd : prog.classDeclList) {
			if (cd.name.contentEquals("System"))
				continue;
			if (cd.name.contentEquals("_PrintStream"))
				continue;

			cd.visit(this, null);
		}
		return null;
	}

	@Override
	public Object visitClassDecl(ClassDecl cd, Object arg) {
		for (MethodDecl md : cd.methodDeclList) {
			md.visit(this, null);
		}
		return null;
	}

	@Override
	public Object visitFieldDecl(FieldDecl fd, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitMethodDecl(MethodDecl md, Object arg) {
		// Assign an address to this function
		md.runtimeEntity = new RuntimeEntity(Reg.CB, Machine.nextInstrAddr());
		System.out.println("Assigned method " + md.name + " to CB[" + md.runtimeEntity.displacement + "]");
		
		// Assign addresses to each of its parameters
		int disp = -1;
		for (int i = md.parameterDeclList.size()-1; i >= 0; i--) {
			md.parameterDeclList.get(i).runtimeEntity = new RuntimeEntity(Reg.LB, disp--);
		}
		
		// Generate code for the method body
		for (Statement s : md.statementList) {
			s.visit(this, null);
		}
		
		// Generate a return statement
		Machine.emit(Op.RETURN, md.type.typeKind == TypeKind.VOID ? 0 : 1, 0, md.parameterDeclList.size());
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
		// TODO Auto-generated method stub
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
		QualRef q = (QualRef) stmt.methodRef;
		RuntimeEntity re = q.ref.getDeclaration().runtimeEntity;
		stmt.argList.get(0).visit(this, null);
		Machine.emit(Op.LOAD, re.baseRegister, re.displacement);

		s.add(new UnknownFunctionAddressRequest(Machine.nextInstrAddr(), (MethodDecl) stmt.methodRef.getDeclaration()));
		Machine.emit(Op.CALLI, Reg.CB,-1);
		return null;
	}

	@Override
	public Object visitReturnStmt(ReturnStmt stmt, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitIfStmt(IfStmt stmt, Object arg) {
		// Form 1: if E then C1 else C2
		if (stmt.elseStmt != null) {
			stmt.cond.visit(this, null);
			int jumpToElse = Machine.nextInstrAddr();
			Machine.emit(Op.JUMPIF, 0, Reg.CB,0);
			stmt.thenStmt.visit(this, null);
			int jumpToExit = Machine.nextInstrAddr();
			Machine.emit(Op.JUMP, Reg.CB, 0);
			Machine.patch(jumpToElse, Machine.nextInstrAddr());
			stmt.elseStmt.visit(this, null);
			Machine.patch(jumpToExit, Machine.nextInstrAddr());
			return null;
		}
		
		// Form 2: if E then C1
		if (stmt.elseStmt == null) {
			stmt.cond.visit(this, null);
			int jumpToExit = Machine.nextInstrAddr();
			Machine.emit(Op.JUMPIF, 0, Reg.CB, 0);
			stmt.thenStmt.visit(this, null);
			Machine.patch(jumpToExit, Machine.nextInstrAddr());
		}
		
		return null;
	}

	@Override
	public Object visitWhileStmt(WhileStmt stmt, Object arg) {
		System.out.println("WETEWTET");
		int jumpToConditionCheck = Machine.nextInstrAddr();
		Machine.emit(Op.JUMP, Reg.CB, 0);
		int bodyExecutionStart = Machine.nextInstrAddr();
		stmt.body.visit(this, null);
		Machine.patch(jumpToConditionCheck, Machine.nextInstrAddr());
		stmt.cond.visit(this, null);
		Machine.emit(Op.JUMPIF, 1, Reg.CB, bodyExecutionStart);
		return null;
	}

	@Override
	public Object visitUnaryExpr(UnaryExpr expr, Object arg) {
		switch (expr.operator.spelling) {
			case "!":
				expr.expr.visit(this, null);
				Machine.emit(Prim.not);
				break;
			case "-":
				Machine.emit(Op.LOADL, 0);
				expr.expr.visit(this, null);
				Machine.emit(Prim.sub);
				break;
			default:
				System.out.println("TODO");
		}
		return null;
	}

	@Override
	public Object visitBinaryExpr(BinaryExpr expr, Object arg) {
		expr.left.visit(this, null);
		expr.right.visit(this, null);
		switch (expr.operator.spelling) {
			case "+":
				Machine.emit(Prim.add);
				break;
			case "-":
				Machine.emit(Prim.sub);
				break;
			case "*":
				Machine.emit(Prim.mult);
				break;
			case "/":
				Machine.emit(Prim.div);
				break;
			case "<":
				Machine.emit(Prim.lt);
				break;
			case "<=":
				Machine.emit(Prim.le);
				break;
			case ">":
				Machine.emit(Prim.gt);
				break;
			case ">=":
				Machine.emit(Prim.ge);
				break;
			case "==":
				Machine.emit(Prim.eq);
				break; // TODO is this right instruction
			case "!=":
				Machine.emit(Prim.ne);
				break;
			case "&&":
				Machine.emit(Prim.and);
				break;
			case "||":
				Machine.emit(Prim.or);
				break;
			default:
				System.out.println("TODO");
		}
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
		expr.lit.visit(this, null);
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
		Machine.emit(Op.LOADL, Integer.parseInt(num.spelling));
		return null;
	}

	@Override
	public Object visitBooleanLiteral(BooleanLiteral bool, Object arg) {
		switch (bool.spelling) {
			case "true": // TODO are these the right truth values
				Machine.emit(Op.LOADL, 1);
				break;
			case "false":
				Machine.emit(Op.LOADL, 0);
				break;
			default:
				System.out.println("TODO");
				break;
		}
		return null;
	}

	@Override
	public Object visitNullLiteral(NullLiteral nullLiteral, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	private MethodDecl findEntryMethod(Package p) {
		ErrorReporter.get().log("Searching for main method", 7);
		MethodDecl mainMethod = null;
		for (ClassDecl cd : p.classDeclList) {
			for (MethodDecl curr : cd.methodDeclList) {
				if (!curr.name.contentEquals("main") || curr.isPrivate || !curr.isStatic) {
					continue;
				}
				if (curr.type.typeKind != TypeKind.VOID || curr.parameterDeclList.size() != 1) {
					continue;
				}
				ParameterDecl pd = curr.parameterDeclList.get(0);
				if (!pd.name.contentEquals("args") || pd.type.typeKind != TypeKind.ARRAY) {
					continue;
				}
				ArrayType arr = (ArrayType) pd.type;
				if (!(arr.eltType instanceof ClassType)) {
					continue;
				}
				ClassType ct = (ClassType) arr.eltType;
				if (ct.className.spelling.contentEquals("String")) {
					if (mainMethod != null) {
						ErrorReporter.get().reportError("Main must be unique");
					}
					mainMethod = curr;
				}
			}
		}
		if (mainMethod == null) {
			ErrorReporter.get()
					.reportError("Failed to find entry method of signature 'public static void main(String[] args)'");
		}
		return mainMethod;
	}

	private void appendReturnToVoidMethods(Package p) {
		for (ClassDecl c : p.classDeclList) {
			for (MethodDecl m : c.methodDeclList) {
				if (m.type.typeKind != TypeKind.VOID)
					continue;
				if (m.statementList.size() == 0) {
					m.statementList.add(new ReturnStmt(null, new SourcePosition(0, 0, 0)));
				} else if (!(m.statementList.get(m.statementList.size() - 1) instanceof ReturnStmt)) {
					m.statementList.add(new ReturnStmt(null, new SourcePosition(0, 0, 0)));
				}
			}
		}
	}

}
