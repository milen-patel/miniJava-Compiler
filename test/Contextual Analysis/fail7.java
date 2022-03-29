class A {
	boolean x;
	void func() {
		{
			int x = 5;
			{
				x = false;
			}
		}
	}
}
