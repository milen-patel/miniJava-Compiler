package miniJava;

public class Compiler {
	public static final int SUCCESS_RETURN_CODE = 0;
	public static final int FAILURE_RETURN_CODE = 4;
	public static void main(String[] args) {
		if (args == null)
			throw new RuntimeException("Bad Command Line Arguements");
		if (args.length != 1)
			throw new RuntimeException("Expected one command line arguement");
		// Should we throw exception or print error and exit with certain code
		
		
		System.exit(SUCCESS_RETURN_CODE);
	}

}
