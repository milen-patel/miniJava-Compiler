package miniJava.SyntacticAnalyzer;

import java.io.FileInputStream;
import java.io.IOException;

public class Scanner {
	// something like '45fwefpj5' would pass, what should happen?
	private char currentChar;
	private StringBuffer currentTokenSpelling;
	private InputReader input;

	public Scanner(InputReader input) {
		this.input = input;
		pullNextChar();
	}

	private void pullWhiteSpace() {
		// TODO: Should i delete first whitespace check as per PA1 description
		while (this.currentChar == ' ' || this.currentChar == '\n' || this.currentChar == '\t'
				|| this.currentChar == '\r') { 
			this.pullNextChar();
		}
	}

	public Token scan() {
		this.pullWhiteSpace();
		this.currentTokenSpelling = new StringBuffer();
		int startPos = this.input.getScannerPosition();
		Token t = new Token(this.scanNextToken(), this.currentTokenSpelling.toString(), startPos, input.getScannerPosition());
		return t;
	}

	public TokenType scanNextToken() {
		// Integer Literal
		if (this.isCurrentCharNumeric()) {
			this.scanNumber();
			return TokenType.INT;
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
			this.pullNextChar();
			return TokenType.DIVISION;
		} else if (this.isLogicalOperatorStart()) {
			return this.parseLogicalOperator();
		} else if (this.isRelationalOperatorOrAssignment()) {
			return this.parseRelationalOperatorOrAssignment();
		}

		else if (this.isCurrentCharAlpabetical()) {
			this.pullNextChar();
			while (this.isCurrentCharAlpabetical() || this.isCurrentCharNumeric() || this.isCurrentCharUnderscore()) { //refactor
				this.pullNextChar();
			}
			return this.handleReservedWords(this.currentTokenSpelling.toString());
		}

		if ((this.currentChar) == 4) { //todo double check this is valid and casting issue
			this.pullNextChar();
			return TokenType.EOT;
		}

		throw new RuntimeException("Invalid Input Stream didn't match any token rule");
	}

	private boolean isLogicalOperatorStart() {
		return this.currentChar == '|' || this.currentChar == '&';
	}

	// Parses '&&' and '||', throws an exception if the first two characters in
	// stream don't match
	private TokenType parseLogicalOperator() {
		if (!this.isLogicalOperatorStart())
			throw new RuntimeException("Not a logical operator"); // except or exit?
		char firstChar = this.currentChar;
		this.pullNextChar();
		if (this.currentChar != firstChar) {
			throw new RuntimeException("Cannot use | or & without || or &&");
		}
		if (this.currentChar == '|') {
			this.pullNextChar();
			return TokenType.LOGICAL_OR;
		} else {
			this.pullNextChar();
			return TokenType.LOGICAL_AND;
		}
	}

	private boolean isRelationalOperatorOrAssignment() {
		return this.currentChar == '>' || this.currentChar == '<' || this.currentChar == '=' || this.currentChar == '!';
	}

	// != ! < > == = <= >=
	private TokenType parseRelationalOperatorOrAssignment() {
		if (!this.isRelationalOperatorOrAssignment())
			throw new RuntimeException("This shouldn't have been called");
		if (this.currentChar == '>') {
			this.pullNextChar();
			if (this.currentChar == '=') {
				return TokenType.GREATHER_THAN_OR_EQUAL_TO; // >=
			} else {
				return TokenType.GREATER_THAN; // >
			}
		}
		if (this.currentChar == '<') {
			this.pullNextChar();
			if (this.currentChar == '=') {
				return TokenType.LESS_THAN_OR_EQUAL_TO; // <=
			} else {
				return TokenType.LESS_THAN; // <
			}
		}

		if (this.currentChar == '=') {
			this.pullNextChar();
			if (this.currentChar == '=') {
				return TokenType.DOUBLE_EQUALS; // ==
			} else {
				return TokenType.ASSIGNMENT; // =
			}
		}

		if (this.currentChar == '!') {
			this.pullNextChar();
			if (this.currentChar == '=') {
				return TokenType.NOT_EQUAL_TO;
			} else {
				return TokenType.LOGICAL_NEGATION;
			}
		}

		throw new RuntimeException("Shoudlnt be here");
	}

	private boolean isCurrentCharNumeric() {
		return this.currentChar >= '0' && this.currentChar <= '9';
	}

	private boolean isCurrentCharAlpabetical() {
		return isCurrentCharLowercase() || isCurrentCharUppercase();
	}

	private boolean isCurrentCharLowercase() {
		return this.currentChar >= 'a' && this.currentChar <= 'z';
	}

	private boolean isCurrentCharUppercase() {
		return this.currentChar >= 'A' && this.currentChar <= 'Z';
	}
	
	private boolean isCurrentCharUnderscore() {
		return this.currentChar == '_';
	}

	private void scanNumber() { // figure out how this is being used
		while (this.isCurrentCharNumeric()) {
			this.pullNextChar();
		}
	}

	private TokenType handleReservedWords(String word) {
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
		} else {
			// TODO do identifier check
			if (word == null || word.length() == 0)
				throw new RuntimeException(""); // TODO find a better way to deal with this
			return TokenType.IDENTIFIER;
		}
	}

	

	public void pullNextChar() {
			if (this.currentTokenSpelling != null)
				this.currentTokenSpelling.append(this.currentChar);
			int next = this.input.next();
			if (next == -1) { // explore this more
				this.currentChar = '\u0004';
			} else {
				this.currentChar = (char) next;
			}
	}

}
