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
		while (this.currentToken.getType() == TokenType.CLASS) {
			classes.add(parseClass());
		}
		accept(TokenType.EOT, "Expected EOT after series of class declarations");
		SourcePosition endPos = this.currentToken.getPosition(); // TODO ask about position issue
		Package pack = new Package(classes, new SourcePosition(startPos.getStartPos(), endPos.getEndPos()));
		return pack;
	}

	private ClassDecl parseClass() {
		SourcePosition startPos = this.currentToken.getPosition();
		accept(TokenType.CLASS, "Expected class keyword to begin class declaration");
		
		Identifier className = this.parseIdentifier("Expected identifier following class keyword");
		FieldDeclList fields = new FieldDeclList();
		MethodDeclList methods = new MethodDeclList();
		
		accept(TokenType.OPEN_CURLY, "Expected '{'");
		while (this.currentToken.getType() != TokenType.CLOSE_CURLY) {
			MemberDecl decl = this.parseFieldOrMethodDeclaration();
			if (decl instanceof FieldDecl) {
				fields.add((FieldDecl) decl);
			} else if (decl instanceof MethodDecl) {
				methods.add((MethodDecl) decl);
			} else {
				ErrorReporter.get().reportError("Internal Parsing Error");
			}
		}
		accept(TokenType.CLOSE_CURLY, "Expected '}' at end of class body");
		SourcePosition pos = new SourcePosition(startPos.getStartPos(), this.currentToken.getPosition().getStartPos());
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
		if (currentToken.getType() == TokenType.VOID) { 
			memberType = new BaseType(TypeKind.VOID, this.currentToken.getPosition()); // TODO do we used basetype for void
			iden = this.parseIdentifier("Expected identifier for method name");
			
			// Parse the parameter list
			accept(TokenType.OPEN_PAREN, "Expected '(' in method declaration");
			if (currentToken.getType() != TokenType.CLOSE_PAREN) {
				params = parseParameterList(); 
			}
			accept(TokenType.CLOSE_PAREN, "Expected ')' in method declaration");
			
			// Parse the method body
			accept(TokenType.OPEN_CURLY, "Expected '{'");
			while (this.currentToken.getType() != TokenType.CLOSE_CURLY) {
				sl.add(parseStatement());
			}
			accept(TokenType.CLOSE_CURLY, "Expected '}' to finish class declaration. "); // TODO repeated code below
			
			// TODO, do we use the same position for both here?
			return new MethodDecl(new FieldDecl(isPrivate, isStatic, memberType, iden.spelling, pos),params, sl, pos);
		}
	
		memberType = parseType();
		iden = this.parseIdentifier("Expected identifier in field/method declaration");
	
		// If we find a semicolon, then it must be a field declaration
		if (currentToken.getType() == TokenType.SEMICOLON) {
			accept(TokenType.SEMICOLON, "Internal  Parsing Error");
			return new FieldDecl(isPrivate, isStatic, memberType, iden.spelling, pos);
		} else if (currentToken.getType() == TokenType.OPEN_PAREN) {
			// Must be a method, parse parameter list
			accept(TokenType.OPEN_PAREN, "Expected '('");
			if (currentToken.getType() != TokenType.CLOSE_PAREN) {
				params = parseParameterList();
			}
			accept(TokenType.CLOSE_PAREN, "Expected ')'");
			
			// Parse method body
			accept(TokenType.OPEN_CURLY, "Expected '{'");
			while (this.currentToken.getType() != TokenType.CLOSE_CURLY) {
				sl.add(parseStatement());
			}
			accept(TokenType.CLOSE_CURLY, "Expected '}' to finish class declaration. "); 
			
			return new MethodDecl(new FieldDecl(isPrivate, isStatic, memberType, iden.spelling, pos),params, sl, pos);
		} else {
			ErrorReporter.get().reportError("<Parser> Invalid class body. Failed to parse field or method declaration. Expected ';' or '('.");
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
		while(this.currentToken.getType() == TokenType.COMMA) {
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
	private void parseReference() {
		ErrorReporter.get().log("<Parser> Parsing Reference Rule", 3);
		if (this.currentToken.getType() == TokenType.THIS) {
			acceptNext();
		} else {
			accept(TokenType.IDENTIFIER, "Expected Identifier in reference parsing");
		}
		
		while (this.currentToken.getType() == TokenType.DOT) {
			acceptNext();
			accept(TokenType.IDENTIFIER, "Expected Identifier after '.'");
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
			accept(TokenType.PUBLIC, "Internal Parsing Error");
			return false;
		case PRIVATE:
			accept(TokenType.PRIVATE, "Internal Parsing Error");
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
		if (currentToken.getType() == TokenType.STATIC) {
			acceptNext();
			return true;
		}
		return false;
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
		ErrorReporter.get().log("<Parser> Parsing Statement Rule", 3);
		switch (this.currentToken.getType()) {
			case RETURN: // (1)
				accept(TokenType.RETURN, "Internal Parsing Error");
				if (currentToken.getType() != TokenType.SEMICOLON) {
					parseExpression();
				}
				accept(TokenType.SEMICOLON, "Expected ';' after return statement.");
				break;
			case IF: // (2)
				accept(TokenType.IF, "Internal  Parsing Error");
				accept(TokenType.OPEN_PAREN, "Expected '('");
				parseExpression();
				accept(TokenType.CLOSE_PAREN, "Expected ')");
				parseStatement();
				if (this.currentToken.getType() == TokenType.ELSE) {
					accept(TokenType.ELSE, "Internal  Parsing Error");
					parseStatement(); // TODO check if this can cause stack overflow or in expression
				}
				break;
			case WHILE: // (3)
				accept(TokenType.WHILE, "Internal  Parsing Error");
				accept(TokenType.OPEN_PAREN, "Expected '('");
				parseExpression();
				accept(TokenType.CLOSE_PAREN, "Expected ')");
				parseStatement();
				break;
			case OPEN_CURLY: // (4)
				accept(TokenType.OPEN_CURLY, "Internal  Parsing Error");
				while (currentToken.getType() != TokenType.CLOSE_CURLY) {
					parseStatement();
				}
				accept(TokenType.CLOSE_CURLY, "Expected '}'");
				break;
			case IDENTIFIER:
				// Not sure if Type or reference
				accept(TokenType.IDENTIFIER, "Internal Parsing Error");
				if (this.currentToken.getType()  == TokenType.OPEN_BRACKET) {
					// Either (int|id)[] or Reference[expression]
					accept(TokenType.OPEN_BRACKET,  "Internal Parsing Error");
					if  (this.currentToken.getType() == TokenType.CLOSE_BRACKET) {
						// Confirmed (int|id)[] => Finish and exit
						accept(TokenType.CLOSE_BRACKET, "Internal Parsing Error");
						this.parseIDAssignmentExpression();
						break;
					} else  {
						// Confirmed  Reference[expression] = Expression; => Finish and  Exit
						parseExpression();
						accept(TokenType.CLOSE_BRACKET, "Expected  ']'");
						accept(TokenType.ASSIGNMENT, "Expected = ");
						parseExpression();
						accept(TokenType.SEMICOLON, "Expected ';'");
						break;
					}
				} else if  (this.currentToken.getType() == TokenType.DOT) {
					// Confirmed Reference, finish parsing reference and fall through
					while (this.currentToken.getType() == TokenType.DOT) {
						acceptNext();
						accept(TokenType.IDENTIFIER, "Expected Identifier after '.'");
					}
				} else if (currentToken.getType() ==  TokenType.IDENTIFIER)  {
						// Confirmed Type id=Expression; => Parse and exit
						this.parseIDAssignmentExpression();
						break;
				} 
				 
			case THIS:
				if (currentToken.getType() == TokenType.THIS) {
					parseReference(); // catch fall through
				}
				if (currentToken.getType() == TokenType.ASSIGNMENT) {
					acceptNext();
					parseExpression();
					accept(TokenType.SEMICOLON, "Expected ';'");
				} else if (currentToken.getType() == TokenType.OPEN_BRACKET) {
					acceptNext();
					parseExpression();
					accept(TokenType.CLOSE_BRACKET, "Expected ']'");
					accept(TokenType.ASSIGNMENT, "Expected '='");
					parseExpression();
					accept(TokenType.SEMICOLON, "Expected ';'");
				} else {
					accept(TokenType.OPEN_PAREN, "Expected '('");
					if (currentToken.getType() != TokenType.CLOSE_PAREN) {
						parseArguementList();
					}
					accept(TokenType.CLOSE_PAREN, "Expected ')'");
					accept(TokenType.SEMICOLON, "Expected ')'");					
				}
				break;
			case INT:
			case BOOLEAN:
				parseType();
				this.parseIDAssignmentExpression();
				break;
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
		accept(TokenType.IDENTIFIER, "Expected identifier");
		accept(TokenType.ASSIGNMENT, "Expected '='");
		parseExpression();
		accept(TokenType.SEMICOLON, "Expected ';'");
	}
	
	// Expression -> Precedence0
	private void parseExpression() {
		ErrorReporter.get().log("<Parser> Parsing Expression Rule", 3);
		this.parsePrecedence0();
	}
	
	// Precedence0		-> Precedence1 ('||' Precedence1)*
	private void parsePrecedence0() {
		this.parsePrecedence1();
		while (this.currentToken.getType() == TokenType.LOGICAL_OR) {
			acceptNext();
			this.parsePrecedence1();
			
		}
	}
	
	// Precedence1		-> Precedence2 ('&&' Precedence2)*
	private void parsePrecedence1() {
		this.parsePrecedence2();
		while (this.currentToken.getType() == TokenType.LOGICAL_AND) {
			acceptNext();
			this.parsePrecedence2();
		}
	}
	
	// Precedence2		-> Precedence3 (('==' || '!=') Precedence3)*
	private void parsePrecedence2() {
		this.parsePrecedence3();
		while (this.currentToken.getType() == TokenType.DOUBLE_EQUALS || 
			this.currentToken.getType() == TokenType.NOT_EQUAL_TO) {
			acceptNext();
			this.parsePrecedence3();
		}
	}
	
	// Precedence3		-> Precedence4 (('<=' || '<' || '>' || '>=')) Precedence4)*
	private void parsePrecedence3() {
		this.parsePrecedence4();
		
		while (currentToken.getType() == TokenType.LESS_THAN_OR_EQUAL_TO ||
				currentToken.getType() == TokenType.LESS_THAN ||
				currentToken.getType() == TokenType.GREATER_THAN ||
				currentToken.getType() == TokenType.GREATHER_THAN_OR_EQUAL_TO) {
			this.acceptNext();
			this.parsePrecedence4();
		}
	}
	
	// Precedence4		-> Precedence5 (('+' || '-') Precedence5)*
	private void parsePrecedence4() {
		this.parsePrecedence5();
		while (currentToken.getType() == TokenType.ADDITION ||
				currentToken.getType() == TokenType.SUBTRACTION) {
			this.acceptNext();
			this.parsePrecedence5();
		}
		
		// Intersting debate between my way and class way- does either matter
		/*
		this.parsePrecedence5();
		while (this.currentToken.getType() == TokenType.ADDITION or sub) {
			this.acceptNext();
			this.parsePrecedence5();
		}*/
	}
	
	// Precedence5		-> Precedence6 (('*' || '/') Precedence6)*
	private void parsePrecedence5() {
		this.parsePrecedence6();
		while (currentToken.getType() == TokenType.MULTIPLICATION ||
				currentToken.getType() == TokenType.DIVISION) {
			this.acceptNext();
			this.parsePrecedence6();
		}
	}
	
	// Precedence6		-> (('-' || '!') Precedence6) | Final
	private void parsePrecedence6() {
		switch (this.currentToken.getType()) {
		case SUBTRACTION:
		case LOGICAL_NEGATION:
			acceptNext();
			this.parsePrecedence6();
			break;
		default:
			this.parsePrecedenceFinal();
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
	private void parsePrecedenceFinal() {
		switch (this.currentToken.getType()) {
		case TRUE: // (6)
			accept(TokenType.TRUE, "Internal Parsing Error");
			break;
		case FALSE: // (7)
			accept(TokenType.FALSE, "Internal Parsing Error");
			break;
		case NUMBER_LITERAL: // (5)
			this.acceptNext();
			break;
		case IDENTIFIER:
		case THIS:
			parseReference(); // (1)
			if (currentToken.getType() == TokenType.OPEN_BRACKET) { // (2)
				acceptNext();
				parseExpression();
				accept(TokenType.CLOSE_BRACKET, "Expected ']' following '['");
			} else if (currentToken.getType() == TokenType.OPEN_PAREN) { // (3)
				acceptNext();
				if (currentToken.getType() != TokenType.CLOSE_PAREN) {
					parseArguementList();
				}
				accept(TokenType.CLOSE_PAREN, "Expected ')' following '('");
			}
			break;
		case NEW: // (8)
			accept(TokenType.NEW, "Internal  Parsing Error");
			if (currentToken.getType() == TokenType.INT) {
				accept(TokenType.INT, "Internal  Parsing Error");
				accept(TokenType.OPEN_BRACKET, "Expected '['");
				parseExpression();
				accept(TokenType.CLOSE_BRACKET, "Expected ']'");
			} else {
				accept(TokenType.IDENTIFIER, "Expected identifier following 'new'");
				if (currentToken.getType() == TokenType.OPEN_PAREN) {
					accept(TokenType.OPEN_PAREN, "Internal  Parsing Error");
					accept(TokenType.CLOSE_PAREN, "Expected ')'");
				} else {
					accept(TokenType.OPEN_BRACKET, "Expected '['");
					parseExpression();
					accept(TokenType.CLOSE_BRACKET, "Expected ']'");
				}
			}
			break;
		case OPEN_PAREN: // (4)
			accept(TokenType.OPEN_PAREN, "Expected '('");
			this.parseExpression();
			accept(TokenType.CLOSE_PAREN, "Expected ')'");
			break;
		default:
			ErrorReporter.get().reportError("<Parser> Failed to parse arithmetic");
		}
	}

	// ArgumentList ::= Expression(,Expression)*
	private void parseArguementList() {
		ErrorReporter.get().log("<Parser> Parsing ArgumentList Rule", 3);
		parseExpression();
		while (this.currentToken.getType() == TokenType.COMMA) {
			accept(TokenType.COMMA, "Internal  Parsing Error");
			parseExpression();
		}
	}

	// Type ::= int | boolean | id | (int|id)[] 
	private TypeDenoter parseType() {
		ErrorReporter.get().log("<Parser> Parsing Type Rule", 3);
		switch (this.currentToken.getType()) {
			case INT:
				SourcePosition intPos = this.currentToken.getPosition();
				accept(TokenType.INT, "Internal Parsing Error");
				BaseType intDef = new BaseType(TypeKind.INT, intPos);
				
				if (this.removeBrackets()) {
					// TODO do we use the same pos in both arguements or what? whhen is typekind array used? same for below
					return new ArrayType(intDef, intPos);
				} else {
					return intDef;
				}
			case BOOLEAN:
				SourcePosition boolPos = this.currentToken.getPosition();
				accept(TokenType.BOOLEAN, "Internal Parsing Error");
				return new BaseType(TypeKind.BOOLEAN, boolPos);
			case IDENTIFIER:
				SourcePosition idPos = this.currentToken.getPosition();
				Identifier cn = this.parseIdentifier("Internal Parsing Error");
				ClassType ct = new ClassType(cn, idPos);
				
				if (this.removeBrackets()) {
					return new ArrayType(ct, idPos);
				} else {
					return ct;
				}
			default:
				ErrorReporter.get().reportError("<Parser> Failed to parse type declaration");	
				return null;
		}
	}
	
	/*
	 * Parses an identifiier and returns an Identifier AST node.
	 */
	private Identifier parseIdentifier(String reason) {
		Token id = this.currentToken;
		accept(TokenType.IDENTIFIER, reason);
		return new Identifier(id);
	}
	
	/*
	 *  Handles ([])?
	 *  Returns true if there are brackets, false otherwise
	 */
	private boolean removeBrackets() {
		ErrorReporter.get().log("<Parser> Checking/Parsing Brackets", 3);
		if (currentToken.getType() == TokenType.OPEN_BRACKET) {
			accept(TokenType.OPEN_BRACKET, "Internal Parsing Error");
			accept(TokenType.CLOSE_BRACKET, "Expected ] after [");
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
	private void accept(TokenType type, String errorReason) {
		ErrorReporter.get().log("\n<Parser> Expecting a Token Type of " + type, 3);
		if (this.currentToken.getType() == type) {
			this.currentToken = this.scanner.scan();
		} else {
			ErrorReporter.get().reportError("<Parser> " + errorReason + ". Got: " + currentToken);
		}
		ErrorReporter.get().log("<Parser> Successfully found a Token Type of " + type, 3);
	}
}