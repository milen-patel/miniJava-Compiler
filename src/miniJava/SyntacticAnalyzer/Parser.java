package miniJava.SyntacticAnalyzer;

public class Parser {
	private Scanner scanner;
	private Token currentToken;

	public Parser(Scanner scanner) {
		this.scanner = scanner;
		this.currentToken = scanner.scan();
	}

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
	}

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

	private void accept(TokenType type, String errorReason) {
		if (this.currentToken.getType() == type) {
			this.currentToken = this.scanner.scan();
		} else {
			Reporter.get().reportError(errorReason + ". Got: " + currentToken.getType());
		}
	}

}
