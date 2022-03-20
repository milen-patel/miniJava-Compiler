class Context {
	private static int x;
}
class Id {
	Context c;
	void func() {
		int p = Context.x;
		int q = c.x;
	}
	void Id() {}
}
class messyReferenceShit {
	A a;
	int qq;
	void func() {
		//int x = this;
		int x = Helpers.f1;
		int x2 = Helpers.f2;
		int x3 = Helpers.f3;
		int x4 = Helpers.f4;
		int x5 = NonExistent.f3;
		int x6 = System.out2.println.invalid; 
		int x7 = this.func2;
		int x8 = this.qq.invalid;
		System.out.println(5);
		messyReferenceShit x9 = this;
		int x10 = Helpers.xx.bb;
	}
	static void func2() {
		messyReferenceShit mrs = this; // static method cannot reference 'this' keyword
	}
}


class Helpers {
	private static A xx;
	static int tmp;
	int r;
	void f1() {
		int x = this.xx.a.a;
		int y = this.DOESNOTEXIST;
		int z = Helpers.z;
		int q = q+5;
		int r = this.r;
		int p = this.xx;
		r = r.q;
		r[5] = 55;
		int j = tmp;
		{
			int inner = 5;
			int d = inner;
		}
		int d = inner;
		return pefw;
		if (invalid)
			ns =se;
		else
			fx = this;
		int h = new NULL();
		int k = new invalid();
		int b = new invalid[5];
		int o = new Helpers[5];
		//int trytihs = (this).xx;
	}
	static void f2() {}
	private static void f3() {}
	private void f4() {}
}
// Null Tests Fail
//class null {}
//class A { void f(null o) {} }
//class A { void f(int null) {} }

// Null Tests Pass
/*
 *
 */
class B {
	B a;
}
class A {
/*
 *
 */
	B a;
	NULL b;
	A[] x;
	int a;
	int f;
	private static boolean bb;
	// Duplicate Parameter
	static void f2(int dupl, boolean dupl) {
		A d = this;
	}
	void f1(int z, int z) {}
	void f(C x) {
		int
		   	x 
			= 
			null;
		int x = 
			5
			*
			2
			+
			null
			/
			3
			&&
			null;
		A x = null;
		{
			int x = 5;
			{
				int x = 5;
				{
					int x = 5;
					int noError = 5;
				}
				{
					int x = 5;
					int noError = 5;
				}
			}
		}
	}
}
class NULL {}
//class A {}


/*
class HelloWorld {
	public int getName() {
		int x = 5;

		{

			
			
			int a = 5;
		}
		{
			{
				{
					{
						int a = 5;
					}
				}
			}
		}
	}
}
*/
