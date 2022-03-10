// Null Tests Fail
//class null {}
//class A { void f(null o) {} }
//class A { void f(int null) {} }

// Null Tests Pass
class A {
	void f() {
		int x = null;
		int x = 5*2+null/3&&null;
		A x = null;
	}
}
class NULL {}


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
