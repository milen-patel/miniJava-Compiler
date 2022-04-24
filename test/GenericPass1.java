class MainBoy {
	public static void main(String[] args) {
		A a = new A();
		System.out.println(a.b.staticVal); // Vald
		a = null;
		//System.out.println(a.b.staticVal); // Invalid
		System.out.println(999999999);
	}
}

class A {
	B b;
	int val;
	static int staticVal;
}

class B {
	C c;
	int val;
	static int staticVal;
}

class C {
	int val;
	static int staticVal;
}