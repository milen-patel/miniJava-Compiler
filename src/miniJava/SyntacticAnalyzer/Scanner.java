package miniJava.SyntacticAnalyzer;

import miniJava.ErrorReporter;

public class Scanner {
	// Represents the current front-most character of the input stream
	private char currentChar;

	// Use to construct spelling of each Token as it's being parsed
	private StringBuffer currentTokenSpelling;

	// Used to traverse input file with ease
	private InputReader input;

	public Scanner(InputReader input) {
		ErrorReporter.get().log("<Scanner> Scanner Class Created", 1);
		this.input = input;

		// Ensure that 'currentChar' has the first character of the input sequence
		pullNextChar();
	}

	/*
	 * Returns the next token in the input stream
	 */
	public Token scan() {
		ErrorReporter.get().log("<Scanner> Asked to pull next token",1);
		//this.currentTokenSpelling = new StringBuffer(); TODO add back after PA1 or change reporter level in pull whitespace
		this.pullWhiteSpace();
		this.currentTokenSpelling = new StringBuffer();
		int startPos = this.input.getScannerPosition();
		int lineNumber = this.input.getLineNumber();
		
		TokenKind t = this.scanNextToken();
		SourcePosition pos = new SourcePosition(startPos, input.getScannerPosition(), lineNumber);
		Token token = new Token(t, this.currentTokenSpelling.toString(), pos);
		
		if (token.getType() == TokenKind.COMMENT) {
			return scan(); // TODO: is this safe
		}
		ErrorReporter.get().log("<Scanner> Done pulling token. Result: " + token.toString(), 1);
		return token;
	}

	/*
	 * Fills the StringBuffer with the spelling of the next available token and
	 * returns the type of the token.
	 */
	private TokenKind scanNextToken() {
		ErrorReporter.get().log("<Scanner> scanNextToken called:" +this.currentTokenSpelling.toString(), 0);
		if (this.isCurrentCharNumeric()) {
			// Found an integer literal
			this.scanNumber();
			return TokenKind.NUMBER_LITERAL;
		} else if (this.currentChar == ';') {
			this.pullNextChar();
			return TokenKind.SEMICOLON;
		} else if (this.currentChar == '.') {
			this.pullNextChar();
			return TokenKind.DOT;
		} else if (this.currentChar == '(') {
			this.pullNextChar();
			return TokenKind.OPEN_PAREN;
		} else if (this.currentChar == ')') {
			this.pullNextChar();
			return TokenKind.CLOSE_PAREN;
		} else if (this.currentChar == '{') {
			this.pullNextChar();
			return TokenKind.OPEN_CURLY;
		} else if (this.currentChar == '}') {
			this.pullNextChar();
			return TokenKind.CLOSE_CURLY;
		} else if (this.currentChar == '[') {
			this.pullNextChar();
			return TokenKind.OPEN_BRACKET;
		} else if (this.currentChar == ']') {
			this.pullNextChar();
			return TokenKind.CLOSE_BRACKET;
		} else if (this.currentChar == ',') {
			this.pullNextChar();
			return TokenKind.COMMA;
		} else if (this.currentChar == '+') {
			this.pullNextChar();
			return TokenKind.ADDITION;
		} else if (this.currentChar == '-') {
			this.pullNextChar();
			return TokenKind.SUBTRACTION;
		} else if (this.currentChar == '*') {
			this.pullNextChar();
			return TokenKind.MULTIPLICATION;
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
			return TokenKind.EOT;
		}

		ErrorReporter.get().reportError("<Scanner> Invalid Input Stream didn't match any token rule");
		return TokenKind.ERROR;
	}

	/*
	 * Parses '&&' and '||', throws an error if the first two characters in stream
	 * don't match.
	 */
	private TokenKind parseLogicalOperator() {
		if (!this.isLogicalOperatorStart())
			ErrorReporter.get().reportError("<Scanner> Internal Scanning Error");

		char firstChar = this.currentChar;
		this.pullNextChar();

		if (this.currentChar != firstChar) {
			ErrorReporter.get().reportError("<Scanner> Cannot use | or & without || or &&");
		}

		if (this.currentChar == '|') {
			this.pullNextChar();
			return TokenKind.LOGICAL_OR;
		} else {
			this.pullNextChar();
			return TokenKind.LOGICAL_AND;
		}
	}

	/*
	 * Parses the following: != ! < > == = <= >=
	 */
	private TokenKind parseRelationalOperatorOrAssignment() {
		if (!this.isRelationalOperatorOrAssignment())
			ErrorReporter.get().reportError("<Scanner> Internal Scanning Error");

		if (this.currentChar == '>') {
			this.pullNextChar();
			if (this.currentChar == '=') {
				this.pullNextChar();
				return TokenKind.GREATHER_THAN_OR_EQUAL_TO; // >=
			} else {
				return TokenKind.GREATER_THAN; // >
			}
		}
		if (this.currentChar == '<') {
			this.pullNextChar();
			if (this.currentChar == '=') {
				this.pullNextChar();
				return TokenKind.LESS_THAN_OR_EQUAL_TO; // <=
			} else {
				return TokenKind.LESS_THAN; // <
			}
		}

		if (this.currentChar == '=') {
			this.pullNextChar();
			if (this.currentChar == '=') {
				this.pullNextChar();
				return TokenKind.DOUBLE_EQUALS; // ==
			} else {
				return TokenKind.ASSIGNMENT; // =
			}
		}

		if (this.currentChar == '!') {
			this.pullNextChar();
			if (this.currentChar == '=') {
				this.pullNextChar();
				return TokenKind.NOT_EQUAL_TO;
			} else {
				return TokenKind.LOGICAL_NEGATION;
			}
		}

		ErrorReporter.get().reportError("<Scanner> Internal Scanning Error");
		return TokenKind.ERROR;
	}

	/*
	 * Once we finish reading in a string, we must determine if that string was a
	 * reserved java keyword. If so, the token type should be changed accordingly.
	 * If no reserved words are matched then we have an identifier.
	 */
	private TokenKind handleReservedWords(String word) {
		// TODO after PA1 - refactor to HashMap
		ErrorReporter.get().log("<Scanner> Checking if identifier is a reserved word", 0);
		if (word.contentEquals("class")) {
			return TokenKind.CLASS;
		} else if (word.contentEquals("void")) {
			return TokenKind.VOID;
		} else if (word.contentEquals("public")) {
			return TokenKind.PUBLIC;
		} else if (word.contentEquals("private")) {
			return TokenKind.PRIVATE;
		} else if (word.contentEquals("static")) {
			return TokenKind.STATIC;
		} else if (word.contentEquals("int")) {
			return TokenKind.INT;
		} else if (word.contentEquals("boolean")) {
			return TokenKind.BOOLEAN;
		} else if (word.contentEquals("this")) {
			return TokenKind.THIS;
		} else if (word.contentEquals("return")) {
			return TokenKind.RETURN;
		} else if (word.contentEquals("if")) {
			return TokenKind.IF;
		} else if (word.contentEquals("while")) {
			return TokenKind.WHILE;
		} else if (word.contentEquals("else")) {
			return TokenKind.ELSE;
		} else if (word.contentEquals("true")) {
			return TokenKind.TRUE;
		} else if (word.contentEquals("false")) {
			return TokenKind.FALSE;
		} else if (word.contentEquals("new")) {
			return TokenKind.NEW;
		} else if (word.contentEquals("null")) {
			return TokenKind.NULL;
		} else {
			return TokenKind.IDENTIFIER;
		}
	}

	/*
	 * If we encounter a '/' symbol, it may either be a division operand or the
	 * start of a comment. This function will return the correct token Type
	 */
	private TokenKind handleDivisionOrComment() {
		// Remove the initial slash
		this.pullNextChar();

		// Option 1: End of line comment '//'. Delete until end of line is encountered.
		if (this.currentChar == '/') {
			while (this.currentChar != '\n' && this.currentChar != '\r') {
				this.pullNextChar();
				if (this.input.eofEncountered()) {
					return TokenKind.COMMENT;
				}
			}
			return TokenKind.COMMENT;
		}

		// Option 2: Block Comment '/* ... */'
		// TODO: Give this logic another check
		if (this.currentChar == '*') {
			this.pullNextChar();
			while (true) {
				if (this.currentChar == '*') {
					this.pullNextChar();
					if (this.currentChar == '/') {
						this.pullNextChar();
						return TokenKind.COMMENT;
					}

				} else {
					this.pullNextChar();
				}
				if (this.input.eofEncountered()) {
					ErrorReporter.get().reportError(
							"<Scanner> Invalid Comment: Encountered EOF without block comment end. You must end a comment if you open one.");
				}
			}
		}

		// If neither option, then we have a division token
		return TokenKind.DIVISION;
	}

	/*
	 * Pulls the next character from the input sequence, appends current character
	 * to StringBuffer
	 */
	private void pullNextChar() {
		ErrorReporter.get().log("<Scanner> Pulling next character from input stream...currentChar: " + this.currentChar, 0);

		if (this.currentTokenSpelling != null) {
			this.currentTokenSpelling.append(this.currentChar);
			ErrorReporter.get().log(
					"<Scanner> Adding current character to string buffer...Current Spelling: " + this.currentTokenSpelling.toString(), 0);
		}

		this.currentChar = this.input.nextChar();
		ErrorReporter.get().log("<Scanner> New currentChar: " + this.currentChar, 0);
	}

	/*
	 * Once a number has been detected, this will parse out the rest of the number
	 */
	private void scanNumber() {
		while (this.isCurrentCharNumeric()) {
			this.pullNextChar();
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
		ErrorReporter.get().log("<Scanner> Pull Whitespace Called", 0);
		while (matchCurrentCharacter(' ') || matchCurrentCharacter('\n') || matchCurrentCharacter('\t')
				|| matchCurrentCharacter('\r')) {
			ErrorReporter.get().log("<Scanner> Whitespace Encountered..pulling", 0);
			this.pullNextChar();
		}
	}

}
