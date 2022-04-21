package miniJava.SyntacticAnalyzer;

import miniJava.ErrorReporter;
import miniJava.AbstractSyntaxTrees.*; // TODO remove wildcard
import miniJava.AbstractSyntaxTrees.Package;

public class Parser {
	private Scanner scanner;
	private Token currentToken;

	public Parser(Scanner scanner) {
		this.scanner = scanner;
		this.currentToken = scanner.scan();
	}

	// Program ::= (ClassDeclaration)* eot 
	public Package parseProgram() {
		SourcePosition startPos = this.currentToken.getPosition(); // TODO could get NPE?
		ClassDeclList classes = new ClassDeclList();
		ErrorReporter.get().log("<Parser> Parsing Program Rule", 3);
		while (this.currentToken.getType() == TokenKind.CLASS) {
			classes.add(parseClass());
		}
		accept(TokenKind.EOT, "Expected EOT after series of class declarations");
		SourcePosition endPos = this.currentToken.getPosition(); // TODO ask about position issue
		Package pack = new Package(classes, new SourcePosition(startPos.getStartPos(), endPos.getEndPos(), startPos.getLineNumber()));
		return pack;
	}

	private ClassDecl parseClass() {
		SourcePosition startPos = this.currentToken.getPosition();
		accept(TokenKind.CLASS, "Expected class keyword to begin class declaration");
		
		Identifier className = this.parseIdentifier("Expected identifier following class keyword");
		FieldDeclList fields = new FieldDeclList();
		MethodDeclList methods = new MethodDeclList();
		
		accept(TokenKind.OPEN_CURLY, "Expected '{'");
		while (this.currentToken.getType() != TokenKind.CLOSE_CURLY) {
			MemberDecl decl = this.parseFieldOrMethodDeclaration();
			if (decl instanceof FieldDecl) {
				fields.add((FieldDecl) decl);
			} else if (decl instanceof MethodDecl) {
				methods.add((MethodDecl) decl);
			} else {
				ErrorReporter.get().reportError("Internal Parsing Error");
			}
		}
		accept(TokenKind.CLOSE_CURLY, "Expected '}' at end of class body");
		SourcePosition pos = new SourcePosition(startPos.getStartPos(), this.currentToken.getPosition().getStartPos(), startPos.getLineNumber());
		return new ClassDecl(className.spelling, fields, methods, pos);
	}

	// ( FieldDeclaration | MethodDeclaration )* 
	private MemberDecl parseFieldOrMethodDeclaration() {
		ErrorReporter.get().log("<Parser> Parsing FieldOrMethodDeclaration Rule", 3);
		
		SourcePosition pos = this.currentToken.getPosition();
		boolean isPrivate = parseVisibility();
		boolean isStatic = parseAccess();
		ParameterDeclList params = new ParameterDeclList();
		StatementList sl = new StatementList();
		Identifier iden = null;
		TypeDenoter memberType = null;
		
		// If we find void keyword, then it must be a method declaration
		if (currentToken.getType() == TokenKind.VOID) { 
			memberType = new BaseType(TypeKind.VOID, this.currentToken.getPosition()); // TODO do we used basetype for void
			accept(TokenKind.VOID, "Internal Parsing Error");
			iden = this.parseIdentifier("Expected identifier in field/method declaration");
			// Must be a method, parse parameter list
			accept(TokenKind.OPEN_PAREN, "Expected '('");
			if (currentToken.getType() != TokenKind.CLOSE_PAREN) {
				params = parseParameterList();
			}
			accept(TokenKind.CLOSE_PAREN, "Expected ')'");
						
			// Parse method body
			accept(TokenKind.OPEN_CURLY, "Expected '{'");
			while (this.currentToken.getType() != TokenKind.CLOSE_CURLY) {
				sl.add(parseStatement());
			}
			accept(TokenKind.CLOSE_CURLY, "Expected '}' to finish class declaration. "); 
						
			return new MethodDecl(new FieldDecl(isPrivate, isStatic, memberType, iden.spelling, pos),params, sl, pos);
		} else {
			memberType = parseType();
			
		}
	
		iden = this.parseIdentifier("Expected identifier in field/method declaration");
		
		// If we find a semicolon, then it must be a field declaration
		if (currentToken.getType() == TokenKind.SEMICOLON) {
			accept(TokenKind.SEMICOLON, "Internal  Parsing Error");
			return new FieldDecl(isPrivate, isStatic, memberType, iden.spelling, pos);
		} else if (currentToken.getType() == TokenKind.OPEN_PAREN) {
			// Must be a method, parse parameter list
			accept(TokenKind.OPEN_PAREN, "Expected '('");
			if (currentToken.getType() != TokenKind.CLOSE_PAREN) {
				params = parseParameterList();
			}
			accept(TokenKind.CLOSE_PAREN, "Expected ')'");
			
			// Parse method body
			accept(TokenKind.OPEN_CURLY, "Expected '{'");
			while (this.currentToken.getType() != TokenKind.CLOSE_CURLY) {
				sl.add(parseStatement());
			}
			accept(TokenKind.CLOSE_CURLY, "Expected '}' to finish class declaration. "); 
			
			return new MethodDecl(new FieldDecl(isPrivate, isStatic, memberType, iden.spelling, pos),params, sl, pos);
		} else {
			ErrorReporter.get().reportError("<Parser> Invalid class body. Failed to parse field or method declaration. Expected ';' or '('." +  pos);
		}
		return null;
	}

	// ParameterList ::= Type id (, Type id)* 
	private ParameterDeclList parseParameterList() {
		ErrorReporter.get().log("<Parser> Parsing ParameterList Rule", 3);
		ParameterDeclList l = new ParameterDeclList();
		
		// Parse the required first parameter
		SourcePosition pos = this.currentToken.getPosition();
		TypeDenoter t = parseType();
		Identifier i = parseIdentifier("Expected identifier following type in parameter list.");
		l.add(new ParameterDecl(t, i.spelling, pos));
		
		// Parse remaining parameters as we see them
		while(this.currentToken.getType() == TokenKind.COMMA) {
			acceptNext();
			SourcePosition posn = this.currentToken.getPosition();
			TypeDenoter type = parseType();
			Identifier identifier = parseIdentifier("Expected identifier following type in parameter list.");
			l.add(new ParameterDecl(type, identifier.spelling, posn));
		}
		
		return l;
	}

	/*
	 * Reference ::= id | this | Reference.id -> Eliminate left recursion Reference
	 * 			 ::= (id | this)(.id)*
	 * 
	 * >starter[Reference]
	 * =starter[(id | this)(.id)*]
	 * =starter[(id | this)]
	 * ={id, this}
	 */
	private Reference parseReference() {
		ErrorReporter.get().log("<Parser> Parsing Reference Rule", 3);
		Reference r = null;
		
		if (this.currentToken.getType() == TokenKind.THIS) {
			r = new ThisRef(this.currentToken.getPosition());
			acceptNext();
		} else {
			SourcePosition pos = this.currentToken.getPosition(); // TODO figure out if right pos
			Identifier id = this.parseIdentifier("Expected Identifier in reference parsing");
			r = new IdRef(id, pos);
		}
		
		while (this.currentToken.getType() == TokenKind.DOT) {
			// TODO figure out source position, figure out if tree should be left developed
			acceptNext();
			Identifier id = this.parseIdentifier("Expected Identifier after '.'");
			r = new QualRef(r, id, r.posn);
		}
		
		return r; // TODO test this method
	}
	
	/*
	 * Statement ::=
	 * 	(4) { Statement* }
	 * | Type id=Expression;
	 * | Reference = Expression ;
	 * | Reference [Expression]=Expression;
	 * | Reference (ArgumentList?);
	 * (1) | return Expression? ;
	 * (2) | if ( Expression ) Statement (else Statement)?
	 * (3) | while ( Expression ) Statement
	 */
	private Statement parseStatement() {
		SourcePosition pos = this.currentToken.getPosition();
		ErrorReporter.get().log("<Parser> Parsing Statement Rule", 3);
		Reference ref = null;
		switch (this.currentToken.getType()) {
			case RETURN: // (1)
				accept(TokenKind.RETURN, "Internal Parsing Error");
				Expression returnExpr = null; // TODO, should return null if no return statement?
				
				// Check if there is a return statement
				if (currentToken.getType() != TokenKind.SEMICOLON) {
					returnExpr = parseExpression();
				}
				
				accept(TokenKind.SEMICOLON, "Expected ';' after return statement.");
				return new ReturnStmt(returnExpr, pos);
			case IF: // (2)
				accept(TokenKind.IF, "Internal  Parsing Error");
				accept(TokenKind.OPEN_PAREN, "Expected '('");
				Expression condition = parseExpression();
				accept(TokenKind.CLOSE_PAREN, "Expected ')");
				Statement then = parseStatement();
				if (this.currentToken.getType() == TokenKind.ELSE) {
					accept(TokenKind.ELSE, "Internal  Parsing Error");
					Statement elseStmt = parseStatement(); // TODO check if this can cause stack overflow or in expression
					return new IfStmt(condition, then, elseStmt, pos);
				}
				return new IfStmt(condition, then, pos);
			case WHILE: // (3)
				accept(TokenKind.WHILE, "Internal  Parsing Error");
				accept(TokenKind.OPEN_PAREN, "Expected '('");
				Expression whileCondition = parseExpression();
				accept(TokenKind.CLOSE_PAREN, "Expected ')");
				Statement whileBody = parseStatement();
				return new WhileStmt(whileCondition, whileBody, pos);
			case OPEN_CURLY: // (4)
				accept(TokenKind.OPEN_CURLY, "Internal  Parsing Error");
				StatementList sl = new StatementList();
				while (currentToken.getType() != TokenKind.CLOSE_CURLY) {
					sl.add(parseStatement());
				}
				accept(TokenKind.CLOSE_CURLY, "Expected '}'");
				return new BlockStmt(sl, pos);
			case IDENTIFIER:
				// Not sure if Type or reference
				Identifier iden = this.parseIdentifier("Internal Parsing Error");
				if (this.currentToken.getType()  == TokenKind.OPEN_BRACKET) {
					// Either (int|id)[] or Reference[expression]
					accept(TokenKind.OPEN_BRACKET,  "Internal Parsing Error");
					if  (this.currentToken.getType() == TokenKind.CLOSE_BRACKET) {
						// Confirmed (int|id)[] => Finish and exit
						accept(TokenKind.CLOSE_BRACKET, "Internal Parsing Error");
						Identifier varName = this.parseIdentifier("Expected name for local var declaration");
						accept(TokenKind.ASSIGNMENT, "Expected '='");
						Expression value = parseExpression();
						accept(TokenKind.SEMICOLON, "Expected ';'");				
						
						TypeDenoter type = new ArrayType(new ClassType(iden, pos),pos); // TODO figure out pos
						VarDecl dec = new VarDecl(type, varName.spelling, pos);
						return new VarDeclStmt(dec, value, pos);
					} else  {
						// Confirmed  Reference[expression] = Expression; => Finish and  Exit
						Expression indexEx = parseExpression();
						accept(TokenKind.CLOSE_BRACKET, "Expected  ']'");
						accept(TokenKind.ASSIGNMENT, "Expected = ");
						Expression rhsExpr = parseExpression();
						accept(TokenKind.SEMICOLON, "Expected ';'");
						
						return new IxAssignStmt(new IdRef(iden, iden.posn), indexEx, rhsExpr, pos);
					}
				} else if  (this.currentToken.getType() == TokenKind.DOT) {
					// Confirmed Reference, finish parsing reference and fall through
					Reference r = new IdRef(iden, iden.posn);
					while (this.currentToken.getType() == TokenKind.DOT) {
						acceptNext();
						Identifier idf = this.parseIdentifier("Expected Identifier after '.'");
						r = new QualRef(r,idf,idf.posn);
					}
					ref = r;
				} else if (currentToken.getType() ==  TokenKind.IDENTIFIER)  {
						// Confirmed Type id=Expression; => Parse and exit
						Identifier varName = this.parseIdentifier("Expected Identifier in local var declaration statement");
						accept(TokenKind.ASSIGNMENT, "Expected '='");
						Expression varValue = parseExpression();
						accept(TokenKind.SEMICOLON, "Expected ';'");				
						VarDecl dec = new VarDecl(new ClassType(iden, iden.posn), varName.spelling, pos);
						return new VarDeclStmt(dec, varValue, pos);
				} 
				if (ref == null) {
					ref = new IdRef(iden, iden.posn);
				}
				 
			case THIS:
				if (currentToken.getType() == TokenKind.THIS) {
					if (ref != null) {
						ErrorReporter.get().reportError("Cannot have this following identifier");
					}
					ref = parseReference(); // catch fall through
				}
				if (currentToken.getType() == TokenKind.ASSIGNMENT) {
					acceptNext();
					Expression expr = parseExpression();
					accept(TokenKind.SEMICOLON, "Expected ';'");
					return new AssignStmt(ref, expr, pos);
				} else if (currentToken.getType() == TokenKind.OPEN_BRACKET) {
					acceptNext();
					Expression index = parseExpression();
					accept(TokenKind.CLOSE_BRACKET, "Expected ']'");
					accept(TokenKind.ASSIGNMENT, "Expected '='");
					Expression value = parseExpression();
					accept(TokenKind.SEMICOLON, "Expected ';'");
					return new IxAssignStmt(ref, index, value, pos);
				} else {
					ExprList funcallParams = new ExprList();
					accept(TokenKind.OPEN_PAREN, "Expected '('");
					if (currentToken.getType() != TokenKind.CLOSE_PAREN) {
						funcallParams = parseArguementList();
					}
					accept(TokenKind.CLOSE_PAREN, "Expected ')'");
					accept(TokenKind.SEMICOLON, "Expected ')'");
					return new CallStmt(ref, funcallParams, pos);
				}
			case INT:
			case BOOLEAN:
				TypeDenoter t = parseType();
				Identifier id = this.parseIdentifier("Expected Identifier in local var declaration statement");
				accept(TokenKind.ASSIGNMENT, "Expected '='");
				Expression value = parseExpression();
				accept(TokenKind.SEMICOLON, "Expected ';'");				
				VarDecl dec = new VarDecl(t, id.spelling, pos);
				return new VarDeclStmt(dec, value, pos);
			default:
				ErrorReporter.get().reportError("<Parser> Failed to parse statement" + this.currentToken);		
		}	
		return new BlockStmt(new StatementList(), currentToken.getPosition()); // TODO change this
	}
	
	/*
	 * Parses = Expression;
	 * Used in statement sub  rule "Type id = Expression;"
	 */
	private void parseIDAssignmentExpression() {
		accept(TokenKind.IDENTIFIER, "Expected identifier");
		accept(TokenKind.ASSIGNMENT, "Expected '='");
		parseExpression();
		accept(TokenKind.SEMICOLON, "Expected ';'");
	}
	
	// Expression -> Precedence0
	private Expression parseExpression() {
		ErrorReporter.get().log("<Parser> Parsing Expression Rule", 3);
		return this.parsePrecedence0();
	}
	
	// Precedence0		-> Precedence1 ('||' Precedence1)*
	private Expression parsePrecedence0() {
		Expression expr = this.parsePrecedence1();
		while (this.currentToken.getType() == TokenKind.LOGICAL_OR) {
			Operator o = new Operator(this.currentToken);
			acceptNext();
			Expression rhs = this.parsePrecedence1();
			expr = new BinaryExpr(o, expr, rhs, o.posn);
		}
		
		return expr;
	}
	
	// Precedence1		-> Precedence2 ('&&' Precedence2)*
	private Expression parsePrecedence1() {
		Expression expr = this.parsePrecedence2();
		while (this.currentToken.getType() == TokenKind.LOGICAL_AND) {
			Operator o = new Operator(this.currentToken);
			acceptNext();
			Expression rhs = this.parsePrecedence2();
			expr = new BinaryExpr(o, expr, rhs, o.posn);
		}
		
		return expr;
	}
	
	// Precedence2		-> Precedence3 (('==' || '!=') Precedence3)*
	private Expression parsePrecedence2() {
		Expression expr = this.parsePrecedence3();
		while (this.currentToken.getType() == TokenKind.DOUBLE_EQUALS || 
			this.currentToken.getType() == TokenKind.NOT_EQUAL_TO) {
			Operator o = new Operator(this.currentToken);
			acceptNext();
			
			Expression rhs = this.parsePrecedence3();
			expr = new BinaryExpr(o,expr,rhs,o.posn);
		}
		
		return expr;
	}
	
	// Precedence3		-> Precedence4 (('<=' || '<' || '>' || '>=')) Precedence4)*
	private Expression parsePrecedence3() {
		Expression expr = this.parsePrecedence4();
		
		while (currentToken.getType() == TokenKind.LESS_THAN_OR_EQUAL_TO ||
				currentToken.getType() == TokenKind.LESS_THAN ||
				currentToken.getType() == TokenKind.GREATER_THAN ||
				currentToken.getType() == TokenKind.GREATHER_THAN_OR_EQUAL_TO) {
			Operator o = new Operator(this.currentToken);
			this.acceptNext();
			Expression rhs = this.parsePrecedence4();
			expr = new BinaryExpr(o,expr,rhs,o.posn);
		}
		
		return expr; 
	}
	
	// Precedence4		-> Precedence5 (('+' || '-') Precedence5)*
	private Expression parsePrecedence4() {
		Expression expr =this.parsePrecedence5();
		while (currentToken.getType() == TokenKind.ADDITION ||
				currentToken.getType() == TokenKind.SUBTRACTION) {
			Operator o = new Operator(this.currentToken);
			this.acceptNext();
			Expression rhs = this.parsePrecedence5();
			expr = new BinaryExpr(o,expr,rhs,o.posn);
		}
		
		return expr;
	}
	
	// Precedence5		-> Precedence6 (('*' || '/') Precedence6)*
	private Expression parsePrecedence5() {
		Expression expr = this.parsePrecedence6();
		while (currentToken.getType() == TokenKind.MULTIPLICATION ||
				currentToken.getType() == TokenKind.DIVISION) {
			Operator o = new Operator(this.currentToken);
			this.acceptNext();
			Expression rhs = this.parsePrecedence6();
			// TODO, do we left nest? which position to use?
			expr = new BinaryExpr(o, expr, rhs, o.posn);
		}
		
		return expr;
	}
	

	
	// Precedence6		-> (('-' || '!') Precedence6) | Final
	private Expression parsePrecedence6() {
		SourcePosition pos = this.currentToken.getPosition();
		switch (this.currentToken.getType()) {
		case SUBTRACTION:
		case LOGICAL_NEGATION:
			Operator operator = new Operator(this.currentToken);
			acceptNext();
			Expression e = this.parsePrecedence6();
			return new UnaryExpr(operator, e, pos);
		default:
			return this.parsePrecedenceFinal();
		}
	}
	
	/*
	 * Final ::=
	 *  (1) Reference
	 *  (2) | Reference [ Expression ]
	 *  (3) | Reference(ArgumentList?)
	 *  (4) | ( Expression )
	 *  (5) | num 
	 *  (6) | true  
	 *  (7) | false
	 *  (8) | new (id() | int[Expression] | id[Expression])
	 */
	// Final			-> num | '(' Expression ')'
	private Expression parsePrecedenceFinal() {
		SourcePosition pos = this.currentToken.getPosition();
		switch (this.currentToken.getType()) {
		case NULL:
			Terminal nullTerminal = new NullLiteral(this.currentToken);
			accept(TokenKind.NULL, "Internal Parsing Error");
			return new LiteralExpr(nullTerminal, pos);
		case TRUE: // (6)
			Terminal tTerminal = new BooleanLiteral(this.currentToken);
			accept(TokenKind.TRUE, "Internal Parsing Error");
			return new LiteralExpr(tTerminal, pos);
		case FALSE: // (7)
			Terminal fTerminal = new BooleanLiteral(this.currentToken);
			accept(TokenKind.FALSE, "Internal Parsing Error");
			return new LiteralExpr(fTerminal, pos);
		case NUMBER_LITERAL: // (5)
			Terminal nTerminal = new IntLiteral(this.currentToken);
			accept(TokenKind.NUMBER_LITERAL, "Internal Parsing Error");
			return new LiteralExpr(nTerminal, pos);
		case IDENTIFIER:
		case THIS:
			Reference ref = parseReference(); // (1)
			if (currentToken.getType() == TokenKind.OPEN_BRACKET) { // (2)
				// Parsing: Reference [ Expression ]
				acceptNext();
				Expression e = parseExpression();
				accept(TokenKind.CLOSE_BRACKET, "Expected ']' following '['");
				return new IxExpr(ref, e, pos);
			} else if (currentToken.getType() == TokenKind.OPEN_PAREN) { // (3)
				// Parsing: Reference(ArgumentList?)
				acceptNext();
				ExprList argList = new ExprList();
				if (currentToken.getType() != TokenKind.CLOSE_PAREN) {
					argList = parseArguementList();
				}
				accept(TokenKind.CLOSE_PAREN, "Expected ')' following '('");
				return new CallExpr(ref, argList, pos);
			}
			return new RefExpr(ref, pos);
		case NEW: // (8)
			accept(TokenKind.NEW, "Internal  Parsing Error");
			if (currentToken.getType() == TokenKind.INT) {
				TypeDenoter elementType = new BaseType(TypeKind.INT, this.currentToken.getPosition());
				accept(TokenKind.INT, "Internal  Parsing Error");
				
				accept(TokenKind.OPEN_BRACKET, "Expected '['");
				Expression e = parseExpression();
				accept(TokenKind.CLOSE_BRACKET, "Expected ']'");
				
				return new NewArrayExpr(elementType, e, pos);
			} else {
				Identifier id = this.parseIdentifier("Expected identifier following 'new'");
				if (currentToken.getType() == TokenKind.OPEN_PAREN) {
					accept(TokenKind.OPEN_PAREN, "Internal  Parsing Error");
					accept(TokenKind.CLOSE_PAREN, "Expected ')'");
					ClassType ct = new ClassType(id, id.posn); // TODO correct posn?
					return new NewObjectExpr(ct, pos);
				} else {
					accept(TokenKind.OPEN_BRACKET, "Expected '['");
					Expression e = parseExpression();
					accept(TokenKind.CLOSE_BRACKET, "Expected ']'");
					TypeDenoter elementType = new ClassType(id, id.posn); // TODO which posn
					return new NewArrayExpr(elementType, e, pos);
					// TODO later refactoring to combine above code and remove duplication
				}
			}
		case OPEN_PAREN: // (4)
			accept(TokenKind.OPEN_PAREN, "Expected '('");
			Expression e = this.parseExpression();
			accept(TokenKind.CLOSE_PAREN, "Expected ')'");
			return e; // TODO is this correct?
		default:
			ErrorReporter.get().reportError("<Parser> Failed to parse expression");
		}
		return null;
	}

	/*
	 * ArgumentList ::= Expression (,Expression)*
	 * Returns an Expression List AST Node corresponding to the formal parameters of a function application.
	 */
	private ExprList parseArguementList() {
		ErrorReporter.get().log("<Parser> Parsing ArgumentList Rule", 3);
		
		// Create return list and expect atleast 1 expression
		ExprList expressionList = new ExprList();
		expressionList.add(parseExpression());
		
		// Keep parsing expressions as the list continues
		while (this.currentToken.getType() == TokenKind.COMMA) {
			accept(TokenKind.COMMA, "Internal  Parsing Error");
			expressionList.add(parseExpression());
		}
		
		return expressionList;
	}

	/*
	 *  Type ::= int | boolean | id | (int|id)[] 
	 *  Returns a TypeDenoter instance correctly identifying the parsed type.
	 */
	private TypeDenoter parseType() {
		ErrorReporter.get().log("<Parser> Parsing Type Rule", 3);
		
		// Grab the starting position
		SourcePosition pos = this.currentToken.getPosition();
		
		switch (this.currentToken.getType()) {
			case INT:
				accept(TokenKind.INT, "Internal Parsing Error");
				BaseType intDef = new BaseType(TypeKind.INT, pos);
				
				// Check if we have a int or an int[]
				if (this.removeBrackets()) {
					// TODO do we use the same pos in both arguements or what? whhen is typekind array used? same for below
					return new ArrayType(intDef, pos);
				} else {
					return intDef;
				}
			case BOOLEAN:
				accept(TokenKind.BOOLEAN, "Internal Parsing Error");
				return new BaseType(TypeKind.BOOLEAN, pos);
			case IDENTIFIER:
				Identifier cn = this.parseIdentifier("Internal Parsing Error");
				ClassType ct = new ClassType(cn, pos); 
				
				// Check if we have a Object or an array of Object type
				if (this.removeBrackets()) {
					return new ArrayType(ct, pos);
				} else {
					return ct;
				}
			default:
				ErrorReporter.get().reportError("<Parser> Failed to parse type declaration");	
				return null;
		}
	}
	
	/*
	 *  Visibility ::= ( public | private )?
	 *  returns true if private, false otherwise
	 */
	private boolean parseVisibility() {
		ErrorReporter.get().log("<Parser> Parsing Visibility Rule", 3);
		switch (currentToken.getType()) {
		case PUBLIC:
			accept(TokenKind.PUBLIC, "Internal Parsing Error");
			return false;
		case PRIVATE:
			accept(TokenKind.PRIVATE, "Internal Parsing Error");
			return true;
		default:
			return false;
		}
	}

	/*
	 *  Access ::= static? 
	 *  Returns true if static, false otherwise
	 */
	private boolean parseAccess() {
		ErrorReporter.get().log("<Parser> Parsing Access Rule", 3);
		if (currentToken.getType() == TokenKind.STATIC) {
			acceptNext();
			return true;
		}
		return false;
	}
	
	/*
	 * Parses an identifier and returns an Identifier AST node.
	 */
	private Identifier parseIdentifier(String reason) {
		Token id = this.currentToken;
		accept(TokenKind.IDENTIFIER, reason);
		return new Identifier(id);
	}
	
	/*
	 *  Handles ([])?
	 *  Returns true if there are brackets, false otherwise
	 */
	private boolean removeBrackets() {
		ErrorReporter.get().log("<Parser> Checking/Parsing Brackets", 3);
		if (currentToken.getType() == TokenKind.OPEN_BRACKET) {
			accept(TokenKind.OPEN_BRACKET, "Internal Parsing Error");
			accept(TokenKind.CLOSE_BRACKET, "Expected ] after [");
			return true;
		}
		return false;
	}
	
	// Accepts the next token without checking its type
	private void acceptNext() {
		ErrorReporter.get().log("<Parser> Accepting next terminal", 3);
		accept(this.currentToken.getType(), "Internal Parsing Error");
	}
	
	// Accepts the next token assuming it matches type, terminates program if type doesn't match
	private void accept(TokenKind type, String errorReason) {
		ErrorReporter.get().log("\n<Parser> Expecting a Token Type of " + type, 3);
		if (this.currentToken.getType() == type) {
			this.currentToken = this.scanner.scan();
		} else {
			ErrorReporter.get().reportError("<Parser> " + errorReason + ". Got: " + currentToken);
		}
		ErrorReporter.get().log("<Parser> Successfully found a Token Type of " + type, 3);
	}
}