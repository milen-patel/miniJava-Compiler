package miniJava.SyntacticAnalyzer;

public class Parser {
	private Scanner scanner;
	private Token currentToken;

	public Parser(Scanner scanner) {
		this.scanner = scanner;
		this.currentToken = scanner.scan();
	}

	// Program ::= (ClassDeclaration)* eot 
	public void parseProgram() {
		Reporter.get().log("<Parser> Parsing Program Rule", 3);
		while (this.currentToken.getType() == TokenType.CLASS) {
			parseClass();
		}
		accept(TokenType.EOT, "Expected EOT after series of class declarations");
	}

	private void parseClass() {
		accept(TokenType.CLASS, "Expected class keyword to begin class declaration");
		accept(TokenType.IDENTIFIER, "Expected valid identifier following class keyword");
		accept(TokenType.OPEN_CURLY, "Expected '{'");
		while (this.currentToken.getType() != TokenType.CLOSE_CURLY) {
			this.parseFieldOrMethodDeclaration();
		}
		accept(TokenType.CLOSE_CURLY, "Expected '}' at end of class body");
	}

	// ( FieldDeclaration | MethodDeclaration )* 
	private void parseFieldOrMethodDeclaration() {
		Reporter.get().log("<Parser> Parsing FieldOrMethodDeclaration Rule", 3);
		parseVisibility();
		parseAccess();
		if (currentToken.getType() == TokenType.VOID) { // TODO something special should happen here
			accept(TokenType.VOID, "Internal Parsing Error");
			// Now we know it is a method declaration
			accept(TokenType.IDENTIFIER, "Expected identifier for method name");
			accept(TokenType.OPEN_PAREN, "Expected '(' in method declaration");
			if (currentToken.getType() != TokenType.CLOSE_PAREN) {
				parseParameterList(); 
			}
			accept(TokenType.CLOSE_PAREN, "Expected ')' in method declaration");
			accept(TokenType.OPEN_CURLY, "Expected '{'");
			while (this.currentToken.getType() != TokenType.CLOSE_CURLY) {
				parseStatement();
			}
			accept(TokenType.CLOSE_CURLY, "Expected '}' to finish class declaration. "); // TODO repeated code below
			return;
		}
		parseType();
		accept(TokenType.IDENTIFIER, "Expected identifier in field/method declaration");
		if (currentToken.getType() == TokenType.SEMICOLON) {
			accept(TokenType.SEMICOLON, "Internal  Parsing Error");
		} else if (currentToken.getType() == TokenType.OPEN_PAREN) {
			accept(TokenType.OPEN_PAREN, "Expected '('");
			if (currentToken.getType() != TokenType.CLOSE_PAREN) {
				parseParameterList();
			}
			accept(TokenType.CLOSE_PAREN, "Expected ')'");
			accept(TokenType.OPEN_CURLY, "Expected '{'");
			while (this.currentToken.getType() != TokenType.CLOSE_CURLY) {
				parseStatement();
			}
			accept(TokenType.CLOSE_CURLY, "Expected '}' to finish class declaration. "); 
		} else {
			Reporter.get().reportError("<Parser> Invalid class body. Failed to parse field or method declaration. Expected ';' or '('.");
		}
	}

	// ParameterList ::= Type id (, Type id)* 
	private void parseParameterList() {
		Reporter.get().log("<Parser> Parsing ParameterList Rule", 3);
		parseType();
		accept(TokenType.IDENTIFIER, "Expected parameter name following type");
		
		while(this.currentToken.getType() == TokenType.COMMA) {
			acceptNext();
			parseType();
			accept(TokenType.IDENTIFIER, "Expected identifier following type in parameter list.");
		}
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
		Reporter.get().log("<Parser> Parsing Reference Rule", 3);
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

	// Visibility ::= ( public | private )?
	private void parseVisibility() {
		Reporter.get().log("<Parser> Parsing Visibility Rule", 3);
		switch (currentToken.getType()) {
		case PUBLIC:
			accept(TokenType.PUBLIC, "Internal Parsing Error");
			break;
		case PRIVATE:
			accept(TokenType.PRIVATE, "Internal Parsing Error");
			break;
		default:
			break;
		}
	}

	// Access ::= static? 
	private void parseAccess() {
		Reporter.get().log("<Parser> Parsing Access Rule", 3);
		if (currentToken.getType() == TokenType.STATIC) {
			acceptNext();
		}
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
	private void parseStatement() {
		Reporter.get().log("<Parser> Parsing Statement Rule", 3);
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
				// Starter[type] = {id, int, boolean}
				// Starter[reference] = {id, this}
				//  If we find an identifier, we don't know if it is a reference or a type
				accept(TokenType.IDENTIFIER, "Internal  Parsing Error");
				if (currentToken.getType() == TokenType.IDENTIFIER || currentToken.getType() == TokenType.OPEN_BRACKET) {
					this.removeBrackets();
					accept(TokenType.IDENTIFIER, "Expected identifier");
					accept(TokenType.ASSIGNMENT, "Expected '='");
					parseExpression(); // TODO this is  also repeated code
					accept(TokenType.SEMICOLON, "Expected ';'");
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
				accept(TokenType.IDENTIFIER, "Expected identifier");
				accept(TokenType.ASSIGNMENT, "Expected '='");
				parseExpression(); // TODO this is  also repeated code
				accept(TokenType.SEMICOLON, "Expected ';'");
				break;
			default:
				Reporter.get().reportError("<Parser> Failed to parse statement" + this.currentToken);		
		}	
	}
	
	/*
	 * Expression ::=
	 *  (5) Reference
	 *  (6) | Reference [ Expression ]
	 *  (7) | Reference(ArgumentList?)
	 *  (4)| unop Expression
	 *  | Expression binop Expression
	 *  (9) | ( Expression )
	 *  (3) | num 
	 *  (1) | true  
	 *  (2) | false
	 *  (8) | new (id() | int[Expression] | id[Expression])
	 */
	private void parseExpression() {
		Reporter.get().log("<Parser> Parsing Expression Rule", 3);
		switch (this.currentToken.getType()) {
			case TRUE: // (1)
				accept(TokenType.TRUE, "Internal Parsing Error");
				break;
			case FALSE: // (2)
				accept(TokenType.FALSE, "Internal Parsing Error");
				break;
			case NUMBER_LITERAL: // (3)
				accept(TokenType.NUMBER_LITERAL, "Internal Parsing Error");
				break;
			case SUBTRACTION: // (4)
				accept(TokenType.SUBTRACTION, "Internal  Parsing Error");
				parseExpression();
				break;
			case LOGICAL_NEGATION: // (4)
				accept(TokenType.LOGICAL_NEGATION, "Internal  Parsing Error");
				parseExpression();
				break;
			case IDENTIFIER: // starters[reference] = {id, this}
			case THIS:
				parseReference(); // (5)
				if (currentToken.getType() == TokenType.OPEN_BRACKET) { // (6)
					acceptNext();
					parseExpression();
					accept(TokenType.CLOSE_BRACKET, "Expected ']' following '['");
				} else if (currentToken.getType() == TokenType.OPEN_PAREN) { // (7)
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
			case OPEN_PAREN: // (9)
				accept(TokenType.OPEN_PAREN, "Internal  Parsing Error");
				parseExpression();
				accept(TokenType.CLOSE_PAREN, "Expected ')' following '('");
				break;
			default:
				Reporter.get().reportError("<Parser> No Expression Cases Matched");
		}
		// TODO Eventually check if while can be  replaced with if
		while (currentToken.getType() == TokenType.GREATER_THAN ||
				currentToken.getType() == TokenType.LESS_THAN ||
				currentToken.getType() == TokenType.DOUBLE_EQUALS ||
				currentToken.getType() == TokenType.LESS_THAN_OR_EQUAL_TO ||
				currentToken.getType() == TokenType.GREATHER_THAN_OR_EQUAL_TO ||
				currentToken.getType() == TokenType.NOT_EQUAL_TO ||
				currentToken.getType() == TokenType.LOGICAL_AND ||
				currentToken.getType() == TokenType.LOGICAL_OR ||
				currentToken.getType() == TokenType.ADDITION ||
				currentToken.getType() == TokenType.SUBTRACTION || // TODO: okay to exclude '!'
				currentToken.getType() == TokenType.DIVISION ||
				currentToken.getType() == TokenType.MULTIPLICATION) {
			acceptNext();
			parseExpression();
		}
	}

	// ArgumentList ::= Expression(,Expression)*
	private void parseArguementList() {
		Reporter.get().log("<Parser> Parsing ArgumentList Rule", 3);
		parseExpression();
		while (this.currentToken.getType() == TokenType.COMMA) {
			accept(TokenType.COMMA, "Internal  Parsing Error");
			parseExpression();
		}
	}

	// Type ::= int | boolean | id | (int|id)[] 
	private void parseType() {
		Reporter.get().log("<Parser> Parsing Type Rule", 3);
		switch (this.currentToken.getType()) {
			case INT:
				accept(TokenType.INT, "Internal Parsing Error");
				this.removeBrackets();
				break;
			case BOOLEAN:
				accept(TokenType.BOOLEAN, "Internal Parsing Error");
				break;
			case IDENTIFIER:
				accept(TokenType.IDENTIFIER, "Internal Parsing Error");
				this.removeBrackets();
				break;
			default:
				Reporter.get().reportError("<Parser> Failed to parse type declaration");	
		}
	}
	
	// Handles ([])?
	private void removeBrackets() {
		Reporter.get().log("<Parser> Checking/Parsing Brackets", 3);
		if (currentToken.getType() == TokenType.OPEN_BRACKET) {
			accept(TokenType.OPEN_BRACKET, "Internal Parsing Error");
			accept(TokenType.CLOSE_BRACKET, "Expected ] after [");
		}
	}
	
	// Accepts the next token without checking its type
	private void acceptNext() {
		Reporter.get().log("<Parser> Accepting next terminal", 3);
		accept(this.currentToken.getType(), "Internal Parsing Error");
	}
	
	// Accepts the next token assuming it matches type, terminates program if type doesn't match
	private void accept(TokenType type, String errorReason) {
		Reporter.get().log("\n<Parser> Expecting a Token Type of " + type, 3);
		if (this.currentToken.getType() == type) {
			this.currentToken = this.scanner.scan();
		} else {
			Reporter.get().reportError("<Parser> " + errorReason + ". Got: " + currentToken);
		}
		Reporter.get().log("<Parser> Successfully found a Token Type of " + type, 3);
	}
}