package miniJava.SyntacticAnalyzer;

public class Parser {
	private Scanner scanner;
	private Token currentToken;

	public Parser(Scanner scanner) {
		this.scanner = scanner;
		this.currentToken = scanner.scan();
	}

	/* Program ::= (ClassDeclaration)* eot */
	public void parseProgram() {
		while (this.currentToken.getType() == TokenType.CLASS) {
			parseClass();
		}
		accept(TokenType.EOT, "Expected EOT after series of class declarations");
	}

	private void parseClass() {
		accept(TokenType.CLASS, "Expected class keyword to begin class declaration");
		accept(TokenType.IDENTIFIER, "Expected valid identifier following class keyword");
		accept(TokenType.OPEN_CURLY, "Expected '{'");
		// TODO: Is this fine to check for the end token or should i check for starters
		// of F|M*
		while (this.currentToken.getType() != TokenType.CLOSE_CURLY) {
			this.parseFieldOrMethodDeclaration();
		}
	}

	/* ( FieldDeclaration | MethodDeclaration )* */
	private void parseFieldOrMethodDeclaration() {
		parseVisibility();
		parseAccess();
		if (currentToken.getType() == TokenType.VOID) { // TODO something special should happen here
			accept(TokenType.VOID, "Internal Parsing Error");
			// Now we know it is a method declaration
			accept(TokenType.IDENTIFIER, "Expected identifier for method name");
			accept(TokenType.OPEN_PAREN, "Expected '(' in method declaration");
			parseParameterList();
			accept(TokenType.CLOSE_PAREN, "Expected ')' in method declaratioon");
		} else {
			parseType();
		}
		accept(TokenType.IDENTIFIER, "Expected identifier in field/method declaration");
		if (currentToken.getType() == TokenType.SEMICOLON) {

		} else {
			// TODO
		}
	}

	/*
	 * ParameterList ::= Type id ( , Type id )* starter[ParameterList] =
	 * starter[Type id] = starter[Type] = {int, bool, id}
	 */
	// TODO. Paremeter list is optional, can the rule just be replaced with and *
	// instead of >=1
	// TODO, is this approach right
	private void parseParameterList() {
		while (currentToken.getType() == TokenType.INT || currentToken.getType() == TokenType.BOOLEAN || currentToken.getType() == TokenType.IDENTIFIER) {
			if (currentToken.getType() == TokenType.BOOLEAN) {
				accept(TokenType.BOOLEAN, "Internal Parsing Error");
			} else if (currentToken.getType() == TokenType.INT) {
				accept(TokenType.INT, "Internal Parsing Error");
				if (currentToken.getType() == TokenType.OPEN_BRACKET) {
					accept(TokenType.OPEN_BRACKET, "Internal Parsing Error");
					accept(TokenType.CLOSE_BRACKET, "Expected ']' following '['");
				}
			} else if (currentToken.getType() == TokenType.IDENTIFIER) {
				accept(TokenType.IDENTIFIER, "Internal Parsing Error");
				if (currentToken.getType() == TokenType.OPEN_BRACKET) {
					accept(TokenType.OPEN_BRACKET, "Internal Parsing Error");
					accept(TokenType.CLOSE_BRACKET, "Expected ']' following '['");
				}
			}
		}
	}

	/*
	 * Reference ::= id | this | Reference.id -> Eliminate left recursion Reference
	 * ::= (id | this)(.id)*
	 * 
	 *  starter[Reference]
	 * =starter[(id | this)(.id)*]
	 * =starter[(id | this)]
	 * ={id, this}
	 */
	private void parseReference() {
		if (this.currentToken.getType() == TokenType.THIS) {
			accept(TokenType.THIS, "Internal Parsing Error");
		} else {
			accept(TokenType.IDENTIFIER, "TODO");
		}
		while (this.currentToken.getType() == TokenType.DOT) {
			accept(TokenType.DOT, "Internal Parsing Error");
			accept(TokenType.IDENTIFIER, "Expected Identifier after '.'");
		}
	}

	/* Visibility ::= ( public | private )? */
	private void parseVisibility() {
		switch (currentToken.getType()) {
		case PUBLIC:
			accept(TokenType.PUBLIC, "Internal Parsing Error");
		case PRIVATE:
			accept(TokenType.PRIVATE, "Internal Parsing Error");
		default:
			break;
		}
	}

	/* Access ::= static? */
	private void parseAccess() {
		if (currentToken.getType() == TokenType.STATIC) {
			accept(TokenType.STATIC, "Internal Parsing Error");
		}
	}
	
	/*
	 * Statement ::=
	 * 	{ Statement* }
	 * | Typeid=Expression;
	 * | Reference = Expression ;
	 * | Reference [Expression]=Expression;
	 * | Reference (ArgumentList?);
	 * | return Expression? ;
	 * | if ( Expression ) Statement (else Statement)?
	 * | while ( Expression ) Statement
	 */
	private void parseStatement() {
		if (currentToken.getType() == TokenType.RETURN) {
			accept(TokenType.RETURN, "Internal Parsing Error");
			// TODO, same as another issue. Do you check if theres a starter or check for semicolon?
			if (this.currentToken.getType() == TokenType.SEMICOLON) {
				// Case 1: No return expression
				accept(TokenType.SEMICOLON, "Internal Parsing Error");
			} else {
				parseExpression();
				accept(TokenType.SEMICOLON, "Expected semicolon to terminate statement");
			}
			return;
		}
		
		
	}
	
	/*
	 * Expression ::=
	 *  (5) Reference
	 *  (6) | Reference [ Expression ]
	 *  (7) | Reference(ArgumentList?)
	 *  (4)| unop Expression
	 *  | Expression binop Expression
	 *  | ( Expression )
	 *  (3) | num 
	 *  (1) | true  
	 *  (2) | false
	 *  | new (id() | int[Expression] | id[Expression])
	 */
	private void parseExpression() {
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
				accept(TokenType.SUBTRACTION, "IPE");
				parseExpression();
				break;
			case LOGICAL_NEGATION: // (4)
				accept(TokenType.LOGICAL_NEGATION, "IPE");
				parseExpression();
				break;
			case IDENTIFIER: // starters[reference] = {id, this}
			case THIS:
				parseReference(); // (5)
				if (currentToken.getType() == TokenType.OPEN_BRACKET) { // (6)
					accept(TokenType.OPEN_BRACKET, "IPE");
					parseExpression();
					accept(TokenType.CLOSE_BRACKET, "Expected ']' following '['");
				} else if (currentToken.getType() == TokenType.OPEN_PAREN) { // (7)
					accept(TokenType.OPEN_PAREN, "IPE");
					// TODO Same question should i check f or a starter of arglist or check for ')'
					if (currentToken.getType() == TokenType.CLOSE_PAREN) {
						accept(TokenType.CLOSE_PAREN, "IPE");
					} else {
						parseArguementList();
						accept(TokenType.CLOSE_PAREN, "Expected ')' following '('");
					}
				}
				break;
			case NEW:
				
				
		}
	}

	private void parseArguementList() {
		// TODO Auto-generated method stub
		
	}

	/* Type ::= int | boolean | id | (int|id)[] */
	private void parseType() {
		if (currentToken.getType() == TokenType.INT) {
			accept(TokenType.INT, "Internal Parsing Error");
			if (currentToken.getType() == TokenType.OPEN_BRACKET) {
				accept(TokenType.OPEN_BRACKET, "Internal Parsing Error");
				accept(TokenType.CLOSE_BRACKET, "Expected ] after [");
			}
		} else if (currentToken.getType() == TokenType.BOOLEAN) {
			accept(TokenType.BOOLEAN, "Internal Parsing Error");
			if (currentToken.getType() == TokenType.OPEN_BRACKET) {
				accept(TokenType.OPEN_BRACKET, "Internal Parsing Error");
				accept(TokenType.CLOSE_BRACKET, "Expected ] after [");
			}
		} else if (currentToken.getType() == TokenType.IDENTIFIER) {
			accept(TokenType.IDENTIFIER, "Internal Parsing Error");
		} else {
			Reporter.get().reportError("Parsing error: expected typed declaration");
		}
	}

	private void accept(TokenType type, String errorReason) {
		if (this.currentToken.getType() == type) {
			this.currentToken = this.scanner.scan();
		} else {
			Reporter.get().reportError(errorReason + ". Got: " + currentToken.getType() + ". Line: "
					+ this.currentToken.getStartPosition());
		}
	}

}
