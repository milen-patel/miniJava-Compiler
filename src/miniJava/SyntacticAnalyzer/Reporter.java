package miniJava.SyntacticAnalyzer;

/*
 * Singleton used for logging messages and reporting errors
 */
public class Reporter {
	public static final int SUCCESS_RETURN_CODE = 0;
	public static final int FAILURE_RETURN_CODE = 4;
	private static Reporter reporter;
	private int reportingMinimum = 10;
	/*
	 * Prints error to user and then ends program with failure code.
	 */
	public void reportError(String reason) {
		System.out.println(reason);
		System.exit(FAILURE_RETURN_CODE);
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
	 */
	public void endWithSuccess() {
		System.out.println("Program looks good!");
		System.exit(SUCCESS_RETURN_CODE);
	}
	
	public static Reporter get() {
		if (reporter == null)
			reporter = new Reporter();
		return reporter;
	}
}
