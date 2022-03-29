class classA { 
	private static int getX() { return 5; }
}
class classB {
	void func() {
		int x = classA.getX();
	}
}
