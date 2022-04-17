package testShitOut;

public class Runner {

	public static void main(String[] args) {
		System.out.println(A.b);
		A a = new A();
		System.out.println(a.b);
		a = null;
		System.out.println(a.b.val);
		a.b.val = 55;
		System.out.println(a.b.val);

	}

}
