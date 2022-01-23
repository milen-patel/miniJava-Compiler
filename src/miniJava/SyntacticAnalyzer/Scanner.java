package miniJava.SyntacticAnalyzer;

public class Scanner {
	// Represents the current front-most character of the input stream
	private char currentChar;

	// Use to construct spelling of each Token as it's being parsed
	private StringBuffer currentTokenSpelling;

	// Used to traverse input file with ease
	private InputReader input;

	public Scanner(InputReader input) {
		Reporter.get().log("Scanner Class Created", 1);
		this.input = input;

		// Ensure that 'currentChar' has the first character of the input sequence
		pullNextChar();
	}

	/*
	 * Returns the next token in the input stream
	 */
	public Token scan() {
		this.pullWhiteSpace();
		this.currentTokenSpelling = new StringBuffer();
		int startPos = this.input.getScannerPosition();

		TokenPosition pos = new TokenPosition(startPos, input.getScannerPosition());
		Token token = new Token(this.scanNextToken(), this.currentTokenSpelling.toString(), pos);
		if (token.getType() == TokenType.COMMENT) {
			return scan(); // TODO: is this safe
		}
		return token;
	}

	/*
	 * Fills the StringBuffer with the spelling of the next available token and
	 * returns the type of the token.
	 */
	private TokenType scanNextToken() {
		if (this.isCurrentCharNumeric()) {
			// Found an integer literal
			this.scanNumber();
			return TokenType.NUMBER_LITERAL;
		} else if (this.currentChar == ';') {
			this.pullNextChar();
			return TokenType.SEMICOLON;
		} else if (this.currentChar == '.') {
			this.pullNextChar();
			return TokenType.DOT;
		} else if (this.currentChar == '(') {
			this.pullNextChar();
			return TokenType.OPEN_PAREN;
		} else if (this.currentChar == ')') {
			this.pullNextChar();
			return TokenType.CLOSE_PAREN;
		} else if (this.currentChar == '{') {
			this.pullNextChar();
			return TokenType.OPEN_CURLY;
		} else if (this.currentChar == '}') {
			this.pullNextChar();
			return TokenType.CLOSE_CURLY;
		} else if (this.currentChar == '[') {
			this.pullNextChar();
			return TokenType.OPEN_BRACKET;
		} else if (this.currentChar == ']') {
			this.pullNextChar();
			return TokenType.CLOSE_BRACKET;
		} else if (this.currentChar == ',') {
			this.pullNextChar();
			return TokenType.COMMA;
		} else if (this.currentChar == '+') {
			this.pullNextChar();
			return TokenType.ADDITION;
		} else if (this.currentChar == '-') {
			this.pullNextChar();
			return TokenType.SUBTRACTION;
		} else if (this.currentChar == '*') {
			this.pullNextChar();
			return TokenType.MULTIPLICATION;
		} else if (this.currentChar == '/') {
			return this.handleDivisionOrComment();
		} else if (this.isLogicalOperatorStart()) {
			return this.parseLogicalOperator();
		} else if (this.isRelationalOperatorOrAssignment()) {
			return this.parseRelationalOperatorOrAssignment();
		} else if (this.isCurrentCharAlpabetical()) {
			this.pullNextChar();
			while (isValidIdentifierBody()) {
				this.pullNextChar();
			}
			return this.handleReservedWords(this.currentTokenSpelling.toString());
		} else if (this.input.eofEncountered()) {
			return TokenType.EOT;
		}

		Reporter.get().reportError("Invalid Input Stream didn't match any token rule");
		return TokenType.ERROR;
	}

	/*
	 * Parses '&&' and '||', throws an error if the first two characters in stream
	 * don't match.
	 */
	private TokenType parseLogicalOperator() {
		if (!this.isLogicalOperatorStart())
			Reporter.get().reportError("Internal Scanning Error");

		char firstChar = this.currentChar;
		this.pullNextChar();

		if (this.currentChar != firstChar) {
			Reporter.get().reportError("Cannot use | or & without || or &&");
		}

		if (this.currentChar == '|') {
			this.pullNextChar();
			return TokenType.LOGICAL_OR;
		} else {
			this.pullNextChar();
			return TokenType.LOGICAL_AND;
		}
	}

	/*
	 * Parses the following: != ! < > == = <= >=
	 */
	private TokenType parseRelationalOperatorOrAssignment() {
		if (!this.isRelationalOperatorOrAssignment())
			Reporter.get().reportError("Internal Parsing Error");

		if (this.currentChar == '>') {
			this.pullNextChar();
			if (this.currentChar == '=') {
				this.pullNextChar();
				return TokenType.GREATHER_THAN_OR_EQUAL_TO; // >=
			} else {
				return TokenType.GREATER_THAN; // >
			}
		}
		if (this.currentChar == '<') {
			this.pullNextChar();
			if (this.currentChar == '=') {
				this.pullNextChar();
				return TokenType.LESS_THAN_OR_EQUAL_TO; // <=
			} else {
				return TokenType.LESS_THAN; // <
			}
		}

		if (this.currentChar == '=') {
			this.pullNextChar();
			if (this.currentChar == '=') {
				this.pullNextChar();
				return TokenType.DOUBLE_EQUALS; // ==
			} else {
				return TokenType.ASSIGNMENT; // =
			}
		}

		if (this.currentChar == '!') {
			this.pullNextChar();
			if (this.currentChar == '=') {
				this.pullNextChar();
				return TokenType.NOT_EQUAL_TO;
			} else {
				return TokenType.LOGICAL_NEGATION;
			}
		}

		Reporter.get().reportError("Internal Scanning Error");
		return TokenType.ERROR;
	}

	/*
	 * Once we finish reading in a string, we must determine if that string was a
	 * reserved java keyword. If so, the token type should be changed accordingly.
	 * If no reserved words are matched then we have an identifier.
	 */
	private TokenType handleReservedWords(String word) {
		Reporter.get().log("Checking if identifier is a reserved word", 0);
		if (word.contentEquals("class")) {
			return TokenType.CLASS;
		} else if (word.contentEquals("void")) {
			return TokenType.VOID;
		} else if (word.contentEquals("public")) {
			return TokenType.PUBLIC;
		} else if (word.contentEquals("private")) {
			return TokenType.PRIVATE;
		} else if (word.contentEquals("static")) {
			return TokenType.STATIC;
		} else if (word.contentEquals("int")) {
			return TokenType.INT;
		} else if (word.contentEquals("boolean")) {
			return TokenType.BOOLEAN;
		} else if (word.contentEquals("this")) {
			return TokenType.THIS;
		} else if (word.contentEquals("return")) {
			return TokenType.RETURN;
		} else if (word.contentEquals("if")) {
			return TokenType.IF;
		} else if (word.contentEquals("while")) {
			return TokenType.WHILE;
		} else if (word.contentEquals("else")) {
			return TokenType.ELSE;
		} else if (word.contentEquals("true")) {
			return TokenType.TRUE;
		} else if (word.contentEquals("false")) {
			return TokenType.FALSE;
		} else if (word.contentEquals("new")) {
			return TokenType.NEW;
		} else {
			return TokenType.IDENTIFIER;
		}
	}

	/*
	 * If we encounter a '/' symbol, it may either be a division operand or the
	 * start of a comment. This function will return the correct token Type
	 */
	private TokenType handleDivisionOrComment() {
		// Remove the initial slash
		this.pullNextChar();

		// Option 1: End of line comment '//'. Delete until end of line is encountered.
		if (this.currentChar == '/') {
			while (this.currentChar != '\n') {
				this.pullNextChar();
			}
			return TokenType.COMMENT;
		}

		// Option 2: Block Comment '/* ... */'
		// TODO: this might be risky, why not whhile (sb.toString.indexOf(*/) != 0)
		if (this.currentChar == '*') {
			while (true) {
				this.pullNextChar();
				if (this.currentChar == '*') {
					this.pullNextChar();
					if (this.currentChar == '/') {
						this.pullNextChar();
						return TokenType.COMMENT;
					}

				}
				if (this.currentChar == '\u0004') {
					Reporter.get().reportError(
							"Invalid Comment: Encountered EOF without block comment end. You must end a comment if you open one.");
				}
			}
		}

		// If neither option, then we have a division token
		return TokenType.DIVISION;
	}

	/*
	 * Pulls the next character from the input sequence, appends current character
	 * to StringBuffer
	 */
	private void pullNextChar() {
		Reporter.get().log("Pulling next character from input stream...Current: " + this.currentChar, 0);

		if (this.currentTokenSpelling != null) {
			Reporter.get().log(
					"Adding current character to string buffer...Current: " + this.currentTokenSpelling.toString(), 0);
			this.currentTokenSpelling.append(this.currentChar);
		}

		this.currentChar = this.input.nextChar();
		Reporter.get().log("New currentChar: " + this.currentChar, 0);
	}

	/*
	 * Once a number has been detected, this will parse out the rest of the number
	 */
	private void scanNumber() {
		while (this.isCurrentCharNumeric()) {
			this.pullNextChar();
		}

		// TODO: Depends on answer to question
		// Now that we have finished parsing the number, check that a letter doesn't
		// immediately follow
		if (this.isCurrentCharAlpabetical()) {
			//Reporter.get().reportError("Identifiers cannot start with numbers");
		}
	}

	/*
	 * Determines if the current character may be the start of a logical 'AND' or
	 * logical 'OR'
	 */
	private boolean isLogicalOperatorStart() {
		return this.matchCurrentCharacter('|') || this.matchCurrentCharacter('&');
	}

	/* Determines if the current character represents a number */
	private boolean isCurrentCharNumeric() {
		return this.currentChar >= '0' && this.currentChar <= '9';
	}

	/* Determines if the current character is a letter */
	private boolean isCurrentCharAlpabetical() {
		return isCurrentCharLowercase() || isCurrentCharUppercase();
	}

	/* Determines if the current character is a lower case letter */
	private boolean isCurrentCharLowercase() {
		return this.currentChar >= 'a' && this.currentChar <= 'z';
	}

	/* Determines if the current character is an upper case letter */
	private boolean isCurrentCharUppercase() {
		return this.currentChar >= 'A' && this.currentChar <= 'Z';
	}

	/*
	 * Determines if the current character is a valid symbol in an identifier's 2nd
	 * position onwards
	 */
	private boolean isValidIdentifierBody() {
		return this.isCurrentCharAlpabetical() || this.isCurrentCharNumeric() || this.isCurrentCharUnderscore();
	}

	/*
	 * Determines if the current character may be a relational operator or a single
	 * equals sign
	 */
	private boolean isRelationalOperatorOrAssignment() {
		return matchCurrentCharacter('>') || matchCurrentCharacter('<') || matchCurrentCharacter('=')
				|| matchCurrentCharacter('!');
	}

	/* Determines if the current character is an underscore */
	private boolean isCurrentCharUnderscore() {
		return this.matchCurrentCharacter('_');
	}

	/* Determines if the current character matches the argument */
	private boolean matchCurrentCharacter(char c) {
		return this.currentChar == c;
	}

	/*
	 * Pulls characters from the input sequence until the first non-whitespace token
	 * is encountered.
	 */
	private void pullWhiteSpace() {
		Reporter.get().log("Pull Whitespace Called", 0);
		while (matchCurrentCharacter(' ') || matchCurrentCharacter('\n') || matchCurrentCharacter('\t')
				|| matchCurrentCharacter('\r')) {
			Reporter.get().log("Whitespace Encountered..pulling", 0);
			this.pullNextChar();
		}
	}

}
