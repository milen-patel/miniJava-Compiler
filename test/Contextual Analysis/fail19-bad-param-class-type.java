class cA {}
class cB { 
	int getX(cA instance) {
		return 5;
	}
	void foo() {
		this.getX(5);
	}
}
