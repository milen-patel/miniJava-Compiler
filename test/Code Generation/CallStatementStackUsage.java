class testCallStmtStackUsage {
	public static void main(String[] args) {
		testCallStmtStackUsage v = new testCallStmtStackUsage();
		int i = 0;
		while (i < 50) { v.getFive(); i = i + 1; }
		int p = 47; int q = 48; int r = 49;
		// Should pause here and check that stack is empty minus p,q,r
		System.out.println(6789);
	}

	public int getFive() {
		return 5;
	}
}