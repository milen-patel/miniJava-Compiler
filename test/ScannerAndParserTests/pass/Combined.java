class messyReferenceShit {
	A a;
	void func() {
		//int x = this;
		int x = Helpers.f1;
		int x2 = Helpers.f2;
		int x3 = Helpers.f3;
		int x4 = Helpers.f4;
		int x5 = NonExistent.f3;
		int x6 = func2.x;
		System.out.println(5);
	}
	static void func2() {
		messyReferenceShit mrs = this; // static method cannot reference 'this' keyword
	}
}


class Helpers {
	void f1() {}
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
class A {
/*
 *
 */
	B a;
	NULL b;
	A[] x;
	int a;
	int f;
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
