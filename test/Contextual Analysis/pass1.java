class classA {
	static int a;
	static int getA() { return a; }
	static int staticFunc() {
		int x = a;
		int y = getA();
	}
}
