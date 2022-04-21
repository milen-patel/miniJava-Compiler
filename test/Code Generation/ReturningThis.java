class Runner {
	public static void main(String[] args) {
		DataClass x = DataClass.getInstance();
		if (x == x.get(x)) {
			System.out.println(0);
		}
		if (x == x.setVal(x, x.a)) {
			System.out.println(1);
		}
		DataClass y = x.get(x);
		if (x.get(x) == y.get(y) && x == y && x == x.get(x)) {
			System.out.println(2);
		}
		if (x.self == y.get(y)) {
			System.out.println(3);
		}
		System.out.println(4);
	}
}


class DataClass {
	int a;
	int b;
	DataClass self;
	
	public static DataClass getInstance() {
		DataClass d = new DataClass();
		d.a = 10;
		d.b = 10;
		d.self = d;
		return d;
	}
	
	public DataClass get(DataClass d) {
		return this;
	}
	
	public DataClass setVal(DataClass d, int a) {
		d.a = a;
		return this.get(this);
	}
}