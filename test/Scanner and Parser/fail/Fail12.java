// Test Empty Class
class CLASS {}
class INT {
	// Test FieldDeclaration Rule
	int 6invalid;
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

	public/*hey whhats up*/int[] d;
	public static int[] d;
	public HelloWorld ea;
	public static HelloWorld eb;
	public Helloword[] ec;
	//public statc HelloWorld[] ed;
	//
}
class HelloWorld {
	public int getName() {
		int a = 5;
		{int a = 5;}
		{{{{int a = 5;}}}}
	}
}
