class classA { private static int x; }
class classB {
	void func() {
		int x = classA.x;
	}
}
