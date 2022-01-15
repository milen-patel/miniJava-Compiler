package miniJava.SyntacticAnalyzer;

import java.io.FileInputStream;
import java.io.IOException;

public class Scanner {
	private char currentChar;
	private StringBuffer currentTokenSpelling;
	private FileInputStream inputStream;

	public Scanner(FileInputStream fileInputStream) {
		this.inputStream = fileInputStream; // TODO: Is it fine to double have scanners
		pullNextChar();
	}

	private void pullWhiteSpace() {
		// Reference: ASCII definition of whitespace
		while (this.currentChar == ' ' || this.currentChar == '\n' || this.currentChar == '\t'
				|| this.currentChar == '\r' || this.currentChar == '\f') {
			this.pullNextChar();
		}
	}

	public Token scan() {
		this.pullWhiteSpace();
		this.currentTokenSpelling = new StringBuffer();
		Token t = new Token(this.scanNextToken(), this.currentTokenSpelling.toString(), 0, 0);
		return t;
	}

	public TokenType scanNextToken() {
		// Integer Literal
		if (this.isNextNumber()) {
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

		else if (this.isAlphabetical()) {
			this.pullNextChar();
			while (this.isAlphabetical() || this.isNextNumber()) {
				this.pullNextChar();
			}
			return this.handleReservedWords(this.currentTokenSpelling.toString());
		}

		if ((this.currentChar) == 4) {
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

	private boolean isNextNumber() {
		return this.currentChar >= '0' && this.currentChar <= '9';
	}

	private boolean isAlphabetical() {
		return isLowerCase() || isUpperCase();
	}

	private boolean isLowerCase() {
		return this.currentChar >= 'a' && this.currentChar <= 'z';
	}

	private boolean isUpperCase() {
		return this.currentChar >= 'A' && this.currentChar <= 'Z';
	}

	private void scanNumber() {
		while (this.isNextNumber()) {
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
			return TokenType.IDENTIFIER;
		}
	}

	private boolean inputHasNext() {
		try {
			return this.inputStream.available() >= 0;
		} catch (IOException e) {
			return false;
		}
	}

	public void pullNextChar() {
		try {
			if (this.currentTokenSpelling != null)
				this.currentTokenSpelling.append(this.currentChar);
			int next = this.inputStream.read();
			if (next == -1) {
				this.currentChar = '\u0004';
			} else {
				this.currentChar = (char) next;
			}
		} catch (IOException e) {
			System.out.println("Input stream has no more tokens."); // TODO are empty files valid?
			System.exit(miniJava.Compiler.FAILURE_RETURN_CODE);
		}
	}

}
