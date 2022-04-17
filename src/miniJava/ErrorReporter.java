package miniJava;

/*
 * Singleton used for logging messages and reporting errors
 */
public class ErrorReporter {
	public static final int SUCCESS_RETURN_CODE = 0;
	public static final int FAILURE_RETURN_CODE = 4;
	private static ErrorReporter reporter;
	private int reportingMinimum = 10;
	public int typeErrors = 0;
	
	/*
	 * Prints error to user and then ends program with failure code.
	 */
	public void reportError(String reason) {
		System.out.println("Error Encountered:");
		StackTraceElement [] trace = Thread.currentThread().getStackTrace();
		for (int i = trace.length - 2; i > 0 ; i--) {    
			System.out.println("\t" + trace[i]);
		}
		System.out.println(reason);
		System.exit(FAILURE_RETURN_CODE);
	}
	
	/*
	 * Reports an identification error and then stops the program.
	 */
	public void idError(int lineNumber, String reason) {
		System.out.println("*** line " + lineNumber + ": " + reason);
		System.exit(FAILURE_RETURN_CODE);
	}
	
	/*
	 * Reports a contextual analysis type checking error
	 */
	public void typeError(int lineNumber, String reason) {
		this.typeErrors++;
		System.out.println("*** line " + lineNumber + ": " + reason);
	}
	
	/*
	 * Logs input to the user. Will become useful if we want to silence logging.
	 */
	public void log(String reason, int severity) {
		if (severity >= this.reportingMinimum) {
			System.out.println(reason);
		}
	}
	
	/*
	 * Terminates program with successful return code.
	 * However, if there were type errors, then exits with code 4.
	 */
	public void endWithSuccess() {
		if (typeErrors > 0) {
			System.exit(FAILURE_RETURN_CODE);
		}
		System.exit(SUCCESS_RETURN_CODE);
	}
	
	public static ErrorReporter get() {
		if (reporter == null)
			reporter = new ErrorReporter();
		return reporter;
	}
}
