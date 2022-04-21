package miniJava.CodeGeneration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
import miniJava.AbstractSyntaxTrees.Declaration;
import miniJava.AbstractSyntaxTrees.Expression;
import miniJava.AbstractSyntaxTrees.FieldDecl;
import miniJava.AbstractSyntaxTrees.IdRef;
import miniJava.AbstractSyntaxTrees.Identifier;
import miniJava.AbstractSyntaxTrees.IfStmt;
import miniJava.AbstractSyntaxTrees.IntLiteral;
import miniJava.AbstractSyntaxTrees.IxAssignStmt;
import miniJava.AbstractSyntaxTrees.IxExpr;
import miniJava.AbstractSyntaxTrees.LiteralExpr;
import miniJava.AbstractSyntaxTrees.LocalDecl;
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
	Map<String, ClassDecl> progClasses = new HashMap<String, ClassDecl>();
	MethodDecl currMethod = null;
	int nextLocalVarPos = 3;
	boolean inAssignStmt = false;

	public void generateCode(Package p) {
		Machine.initCodeGen();
		// First, verify existence of main method
		MethodDecl main = this.findEntryMethod(p);

		// Ensure all void methods end in a return
		this.appendReturnToVoidMethods(p);

		// Populate class map, needed for newObjectExpr
		for (ClassDecl c : p.classDeclList) {
			progClasses.put(c.name, c);
		}

		// Reserve space for static class fields
		int numStaticFields = 0;
		for (ClassDecl cd : p.classDeclList) {
			for (FieldDecl fd : cd.fieldDeclList) {
				if (fd.isStatic) {
					ErrorReporter.get().log("Static field " + fd.name + " of class " + cd.name + " assigned to address SB["
							+ numStaticFields + "]", 9);
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
		// Never Called
		return null;
	}

	@Override
	public Object visitMethodDecl(MethodDecl md, Object arg) {
		this.currMethod = md;
		this.nextLocalVarPos = 3;
		// Assign an address to this function
		md.runtimeEntity = new RuntimeEntity(Reg.CB, Machine.nextInstrAddr());
		ErrorReporter.get().log("Assigned method " + md.name + " to CB[" + md.runtimeEntity.displacement + "]", 9);

		// Assign addresses to each of its parameters
		int disp = -1;
		for (int i = md.parameterDeclList.size() - 1; i >= 0; i--) {
			md.parameterDeclList.get(i).runtimeEntity = new RuntimeEntity(Reg.LB, disp--);
		}

		// Generate code for the method body
		for (Statement s : md.statementList) {
			s.visit(this, null);
		}

		this.currMethod = null;
		// TODO figure out if we need to pop vardecls via nextLocalVarPos downto 3
		return null;
	}

	@Override
	public Object visitParameterDecl(ParameterDecl pd, Object arg) {
		// Never Called
		return null;
	}

	@Override
	public Object visitVarDecl(VarDecl decl, Object arg) {
		// Never Called
		return null;
	}

	@Override
	public Object visitBaseType(BaseType type, Object arg) {
		// Never Called
		return null;
	}

	@Override
	public Object visitClassType(ClassType type, Object arg) {
		// Never Called
		return null;
	}

	@Override
	public Object visitArrayType(ArrayType type, Object arg) {
		// Never Called
		return null;
	}

	@Override
	public Object visitBlockStmt(BlockStmt stmt, Object arg) {
		int varDecls = 0;
		for (Statement s : stmt.sl) {
			s.visit(this, null);
			if (s instanceof VarDeclStmt)
				varDecls++;
		}
		if (varDecls > 0) 
			Machine.emit(Op.POP, varDecls);
		this.nextLocalVarPos -= varDecls;
		return null;
	}

	@Override
	public Object visitVardeclStmt(VarDeclStmt stmt, Object arg) {
		stmt.varDecl.runtimeEntity = new RuntimeEntity(Reg.LB, this.nextLocalVarPos++);
		Machine.emit(Op.PUSH, 1);
		stmt.initExp.visit(this, null);
		Machine.emit(Op.STORE, 1, Reg.LB, stmt.varDecl.runtimeEntity.displacement);
		ErrorReporter.get().log("Assigning local variable " + stmt.varDecl.name + " to LB[" + stmt.varDecl.runtimeEntity.displacement + "]", 9);
		return null;
	}

	@Override
	public Object visitAssignStmt(AssignStmt stmt, Object arg) {
		if (stmt.ref.getDeclaration() instanceof FieldDecl && ((FieldDecl) stmt.ref.getDeclaration()).isStatic) {
			System.out.println("CASE"); // Thihnk this may let npe's pass through
			FieldDecl fd = (FieldDecl) stmt.ref.getDeclaration();
			stmt.val.visit(this, null);
			Machine.emit(Op.STORE, 1, fd.runtimeEntity.baseRegister, fd.runtimeEntity.displacement);
			return null;
		}
		if (stmt.ref.getDeclaration() instanceof LocalDecl) { // i.e. Params or Local Variables (make sure their REs are correct)
			stmt.val.visit(this, null);
			Machine.emit(Op.STORE, 1, stmt.ref.getDeclaration().runtimeEntity.baseRegister, stmt.ref.getDeclaration().runtimeEntity.displacement);
			return null;
		}
		if (stmt.ref.getDeclaration() instanceof FieldDecl) {
			stmt.ref.visit(this, null);
			Machine.CT--; // TOO HACKY TODO
			stmt.val.visit(this, null);
			Machine.emit(Prim.fieldupd);
		}

		return null;
	}

	@Override
	public Object visitIxAssignStmt(IxAssignStmt stmt, Object arg) {
		stmt.ref.visit(this, null); // TODO make sure ref can handle this
		stmt.ix.visit(this, null);
		stmt.exp.visit(this, null);
		Machine.emit(Prim.arrayupd);
		return null;
	}

	@Override
	public Object visitCallStmt(CallStmt stmt, Object arg) {
		stmt.methodRef.visit(this, null);

		
		// Put args onto stack
		for (Expression e : stmt.argList)
			e.visit(this, null);
		
		
		MethodDecl callee = (MethodDecl) stmt.methodRef.getDeclaration(); // TODO this migth alloow foor NPEs to pass
		if (callee.isStatic) {
			s.add(new UnknownFunctionAddressRequest(Machine.nextInstrAddr(), callee));
			Machine.emit(Op.CALL, Reg.CB, -1);
			return null;
		}
		
		// Push instance address onto stack

		s.add(new UnknownFunctionAddressRequest(Machine.nextInstrAddr(), callee));
		Machine.emit(Op.CALLI, Reg.CB, 0);
		
		// We dont want to leave a return value on the stack
		if (stmt.methodRef.getDeclaration().type.typeKind != TypeKind.VOID) {
			Machine.emit(Op.POP, 1);
		}
		return null;
	}

	@Override
	public Object visitReturnStmt(ReturnStmt stmt, Object arg) {
		if (stmt.returnExpr != null)
			stmt.returnExpr.visit(this, null);
		Machine.emit(Op.RETURN, stmt.returnExpr == null ? 0 : 1, 0, this.currMethod.parameterDeclList.size());
		return null;
	}

	@Override
	public Object visitIfStmt(IfStmt stmt, Object arg) {
		// Form 1: if E then C1 else C2
		if (stmt.elseStmt != null) {
			stmt.cond.visit(this, null);
			int jumpToElse = Machine.nextInstrAddr();
			Machine.emit(Op.JUMPIF, 0, Reg.CB, 0);
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
			Machine.emit(Prim.sub); //TODO
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
		expr.ref.visit(this, null);
		return null;
	}

	@Override
	public Object visitIxExpr(IxExpr expr, Object arg) {
		expr.ref.visit(this, null); // TODO need to make sure reference can handle this
		expr.ixExpr.visit(this, null);
		Machine.emit(Prim.arrayref);
		return null;
	}

	@Override
	public Object visitCallExpr(CallExpr expr, Object arg) {		
		// Put args onto stack
		for (Expression e : expr.argList)
			e.visit(this, null);
		
		expr.functionRef.visit(this, null);

		
		MethodDecl callee = (MethodDecl) expr.functionRef.getDeclaration();
		if (callee.isStatic) {
			s.add(new UnknownFunctionAddressRequest(Machine.nextInstrAddr(), callee));
			Machine.emit(Op.CALL, Reg.CB, -1);
			return null;
		}
		
		// Push instance address onto stack
		s.add(new UnknownFunctionAddressRequest(Machine.nextInstrAddr(), callee));
		Machine.emit(Op.CALLI, Reg.CB, 0);
		return null;
	}

	@Override
	public Object visitLiteralExpr(LiteralExpr expr, Object arg) {
		expr.lit.visit(this, null);
		return null;
	}

	@Override
	public Object visitNewObjectExpr(NewObjectExpr expr, Object arg) {
		ClassDecl cd = this.progClasses.get(expr.classtype.className.spelling);
		int numFields = 0;
		for (FieldDecl fd : cd.fieldDeclList) {
			if (!fd.isStatic)
				numFields++;
		}
		Machine.emit(Op.LOADL, -1);
		Machine.emit(Op.LOADL, numFields);
		Machine.emit(Prim.newobj);
		return null;
	}

	@Override
	public Object visitNewArrayExpr(NewArrayExpr expr, Object arg) {
		// Put the number of elements onto the stack
		expr.sizeExpr.visit(this, null);
		Machine.emit(Prim.newarr);
		return null;
	}

	@Override
	public Object visitThisRef(ThisRef ref, Object arg) {
		Machine.emit(Op.LOADA, Reg.OB, 0);
		return 1;
	}

	@Override
	// For Basic 1 variable name non-qualified references
	public Object visitIdRef(IdRef ref, Object arg) {
		Declaration d = ref.getDeclaration();
		// Case 0: Class Name - Shouldn't ever be encountered 
		if (d instanceof ClassDecl) {
			return 0;
		}

		// Case 1,2: Local Variable and Parameter Declaration
		if (d instanceof VarDecl || d instanceof ParameterDecl) {
			ref.runtimeEntity = d.runtimeEntity.duplicate();
			Machine.emit(Op.LOAD, 1, ref.runtimeEntity.baseRegister, ref.runtimeEntity.displacement);
			return 1;
		}
		
		// Case 3: Class Field
		if (d instanceof FieldDecl) {
			FieldDecl fd = (FieldDecl) d;
			if (fd.isStatic) {
				ref.runtimeEntity = fd.runtimeEntity.duplicate();
				Machine.emit(Op.LOAD, 1, ref.runtimeEntity.baseRegister, ref.runtimeEntity.displacement);
				return 1;
			}
			if (!fd.isStatic) {
				Machine.emit(Op.LOADA, Reg.OB, 0);
				Machine.emit(Op.LOADL, fd.runtimeEntity.displacement);
				Machine.emit(Prim.fieldref);
				return 1; 
			}
			return 0;
		}
		
		// Case 4: Method
		if (d instanceof MethodDecl) {
			// d.runtimeEntity will be null if we haven't already handled that function
			ref.runtimeEntity = d.runtimeEntity;
			MethodDecl md = (MethodDecl) d;
			if (!md.isStatic) {
				// Need to put object instance on the stack
				Machine.emit(Op.LOADA, Reg.OB, 0);
				return 1;
			}
			return 0;
		}
		return 0;
	}

	@Override
	public Object visitQRef(QualRef ref, Object arg) {
		int i = (int) ref.ref.visit(this, null);
		
		// Check if we are trying to access an array length
		if (ref.ref.getDeclaration() instanceof FieldDecl || ref.ref.getDeclaration() instanceof LocalDecl) {	
			if (ref.ref.getDeclaration().type instanceof ArrayType) {
				Machine.emit(Prim.arraylen);
				return i+1;
			}
		} 
		
		int j = (int) ref.id.visit(this, i);
		return i+j;
	}

	@Override
	public Object visitIdentifier(Identifier id, Object arg) { 
		int stackWords = (int) arg;
		// I believe this either has to be a method/field of a class 
		Declaration d = id.getDeclaration();
		if (d instanceof FieldDecl) {
			FieldDecl fd = (FieldDecl) d;
			if (fd.isStatic) {
				if (stackWords > 0) {
					// Free wasted space
					Machine.emit(Op.POP, stackWords);
				}
				Machine.emit(Op.LOAD, 1, fd.runtimeEntity.baseRegister, fd.runtimeEntity.displacement);
				id.runtimeEntity = fd.runtimeEntity;
				return -stackWords+1;
			}
			if (!fd.isStatic) {
				Machine.emit(Op.LOADL, fd.runtimeEntity.displacement);
				Machine.emit(Prim.fieldref);
				id.runtimeEntity = fd.runtimeEntity;
				return 0; // TODO check this
			}
		}
		
		if (d instanceof MethodDecl) {
			MethodDecl md = (MethodDecl) d;
			if (md.isStatic) {
				if (stackWords > 0) {
					// Free wasted space
					Machine.emit(Op.POP, stackWords);
					return -stackWords;
				}
			}
			
		}
		
		// If its a field, then we stop since visiting the left child of qual ref has put the object instance on
		return 0;
	}

	@Override
	public Object visitOperator(Operator op, Object arg) {
		// Never Called
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
		case "true": 
			Machine.emit(Op.LOADL, Machine.trueRep);
			break;
		case "false":
			Machine.emit(Op.LOADL, Machine.falseRep);
			break;
		default:
			ErrorReporter.get().reportError("Unknown boolean literal value encountered during code generation.");
			break;
		}
		return null;
	}

	@Override
	public Object visitNullLiteral(NullLiteral nullLiteral, Object arg) {
		Machine.emit(Op.LOADL, Machine.nullRep);
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