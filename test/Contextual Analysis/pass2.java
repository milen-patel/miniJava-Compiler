class Singleton {
	static Singleton s;
	Singleton getS() {
		s = new Singleton();
		s = this;
		return this.s;
	}
}
