package anothher;

public class Runner {

		public static void main(String[] args) {
			A a = new A();
			System.out.println(a.b.staticVal); // Vald
			a = null;
			System.out.println(a.b.staticVal); // Invalid
			System.out.println(999999999);
		}
	}


