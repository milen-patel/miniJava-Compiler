class Runner {
	public static void main(String[] args) {
		A A = new A();
		A.y = 55;
		A.foo(A); // Prints 55, 55
		A.z = new A();
		A x = new A();
		x = x.foo(x); // Prints 55, 55
		if (A.z == x.z) {
			System.out.println(55);
		}
	}
}

class A { 	
	static A z;
	int y;
    public A foo(A A) {
    	System.out.println(A.y);
    	if (A.z == z) {
    		System.out.println(55);
    	}
    	return this;
    }  // A may have both definitions in the same scope
}
