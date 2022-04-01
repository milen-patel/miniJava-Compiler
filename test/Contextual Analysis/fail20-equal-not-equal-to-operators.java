class A {
	void method() {
		int x = 0;
		x();
		int j = x() + 5;
	}
}
class classA {
	int a;
	int b;
	boolean c;
	boolean d;
	classB e;
	classB f;

	int[] arrInt;
	void testEqualityOperators() {
		boolean one = a == b;
		int two = a == b;
		boolean three = a != b;
		int four = a != b;
		boolean five = c == d;
		int six = c ==d ;
		boolean seven = e==f;
		boolean eight = e!=f;
		int nine = e==f || e !=f;
		boolean ten = null == arrInt;
		boolean eleven = null == null;
		boolean twelve = null != null;
		boolean thirteen = classA == classB;
		boolean fourteen = 5 == 6;
		boolean fifteen = 5 != 6;
		boolean sixteen = this.a == 5;
		boolean seventeen = b != classB.getX();
		boolean eighteen = testEqualityOperators == testEqualityOperators;
		boolean nineteen = null == classB;
	}
}


class classB {
	public static int getX() { if (5 == 5) { return 1; } return 6;}
}
