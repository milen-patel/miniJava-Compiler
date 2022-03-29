class A {
	void fun() {
		int x = 5;
		{
			int j = 6;
			x = 5;
			{
				j = 6;
				x = j + x;
			}
		}
	}
}
