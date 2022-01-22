// Test Empty Class
class CLASS {}
class INT {
	// Test FieldDeclaration Rule
	int a;
	static int b;
	boolean c;
	static boolean c;
	OtherClass d;
	static OtherClass d;

	public int a;
	public static int b;
	public boolean c;
	public static boolean c;
	public OtherClass d;
	public static OtherClass d;

	private int a;
	private static int b;
	private boolean c;
	private static boolean c;
	private OtherClass d;
	private static OtherClass d;

	int[/* Comments can go*/] x/*wherever*/;
	static int[] b;
	OtherClass[] d;
	static OtherClass[] d;
	public int[] a;
	public static int[] b;
	public OtherClass[] d;
	public static OtherClass[] d;
	private int[] a;
	private static int[] b;
	private OtherClass[] d;
	private static OtherClass[] d;

}
class HelloWorld {
	public int getName() {
		int a = 5;
		{int a = 5;}
		{{{{int a = 5;}}}}
	}
}
