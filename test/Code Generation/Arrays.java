class TestArrays {
	int[] arr1;
	public static void main(String[] args) {
		int[] arr2 = new int[40];
		System.out.println(arr2[4]);

		TestArrays tA = new TestArrays();
		tA.init();
		System.out.println(tA.get(25));

		int[] arr3 = ArrayHelpers.getDecreasingArray(69);
		int i = arr3.length-1;
		while (i >= 0) {
			System.out.println(arr3[i]);
			i = i -1;
		}

		tA.testArrayOfObjects();
		tA.testArrayNesting();
		tA.testMergeArray();
	}

	public void init() {
		this.arr1 = new int[100];
		int i = 0;
		while (i < this.arr1.length/2) {
			this.arr1[i] = i;
			i = i + 1;
		}
	}

	public int get(int idx) {
		return arr1[idx];
	}

	public void testArrayOfObjects() {
		int size = 5;
		Wrapper[] arr = createArray(size);
		setVal(arr, 0, new Wrapper());
		setVal(arr, 1, new Wrapper());
		setVal(arr, 2, ArrayHelpers.get(arr, 0));
		setVal(arr, 3, arr[2]);	
		setVal(arr, 4, arr[1]);	
		Wrapper i0 = arr[0];
		i0.setVal(400);
		ArrayHelpers.printArray(arr);
		Wrapper i1 = ArrayHelpers.get(arr, 1);
		if (i1 != arr[1]) { int[] errorOut = new int[-345]; }
		if (arr[1] != arr[4]) { int[] errorOut = new int[-345]; }
		i1.setVal(200);
		ArrayHelpers.printArray(arr);

		this.wArr = arr;
		wArr = arr;
		this.clearWArr();
		ArrayHelpers.printArray(wArr);
	}

	private void clearWArr() {
		int i = 0;
		while (i < this.wArr.length) {
			Wrapper w = this.wArr[i];
			w.setVal(0);
			i = i + 1;
		}
	}

	private Wrapper[] wArr;

	private Wrapper[] createArray(int numElts) {
		return new Wrapper[numElts];
	}

	private void setVal(Wrapper[] arr, int idx, Wrapper val) {
		arr[idx]=val;
	}


	public void testArrayNesting() {
		AC_2.arr = new int[50];
		AC_1 x = null;

		int[] arr = x.ac_2.arr;
		arr[2] = 420;

		System.out.println(arr[2]);
		System.out.println(AC_2.arr[2]);
		System.out.println(arr.length);
		System.out.println(AC_2.arr.length);
	}

	public void testMergeArray() {
		int[] a = new int[10];
		int[] b = new int[5];
		a[0] = 1; b[0] = 2;
		a[1] = 2; b[1] = 4;
		a[2] = 3; b[2] = 6;
		a[3] = 4; b[3] = 8;
		a[4] = 5; b[4] = 10;
		a[5] = 6;
		a[6] = 7;
		a[7] = 8;
		a[8] = 9;
		a[9] = 10;
		int[] expected = new int[15];
		expected[0] = 1;
		expected[1] = 2;
		expected[2] = 2;
		expected[3] = 3;
		expected[4] = 4;
		expected[5] = 4;
		expected[6] = 5;
		expected[7] = 6;
		expected[8] = 6;
		expected[9] = 7;
		expected[10] = 8;
		expected[11] = 8;
		expected[12] = 9;
		expected[13] = 10;
		expected[14] = 10;
		ArrayHelpers.assertArraysEqual(expected, ArrayHelpers.mergeIntArrays(a,b));
		System.out.println(expected.length);
	}
}

class AC_1 {
	static AC_2 ac_2;
}

class AC_2 {
	static int[] arr;
}

class Wrapper { private int val; 
				public int getVal() { return this.val; } 
				public void setVal(int val) { this.val = val; }
			  }

class ArrayHelpers {
	public static int[] getDecreasingArray(int startingNumber) {
		int[] arr = new int[startingNumber+1];
		while (startingNumber >= 0) {
			arr[startingNumber] = startingNumber;
			startingNumber = startingNumber - 1;
		}
		return arr;
	}

	public static void printArray(Wrapper[] arr) {
		int i = 0;
		while (i < arr.length) {
			Wrapper curr = arr[i];
			System.out.println(curr.getVal());
			i = i + 1;
		}
	}

	public static void printIntArray(int[] arr) {
		int i = 0;
		while (i < arr.length) {
			System.out.println(arr[i]);
			i = i + 1;
		}
	}

	public static Wrapper get(Wrapper[] arr, int i) {
		return arr[i];
	}

	public static void assertArraysEqual(int[] a, int[] b) {
		if (a.length != b.length) die();
		int i = 0;
		while (i < a.length) {
			if (a[i] != b[i]) die();
			i = i + 1;
		}
	}

	public static void die() {
		int[] x = new int[-55555555];
	}

	public static int[] mergeIntArrays(int[] a, int[] b) {
		int[] res = new int[a.length + b.length]; 
		int aPtr = 0;
		int bPtr = 0;
		int resPtr = 0;
		while (aPtr < a.length && bPtr < b.length) {
			
			if (a[aPtr] <= b[bPtr]) {
				res[resPtr] = a[aPtr];
				aPtr = aPtr + 1;
			} else {
				res[resPtr] = b[bPtr];
				bPtr = bPtr + 1;
			}
			resPtr = resPtr + 1;
		}

		if (aPtr < a.length) {
			while (aPtr < a.length) {
				res[resPtr] = a[aPtr];
				resPtr = resPtr + 1;
				aPtr = aPtr + 1;
			}
		} else if (bPtr < b.length) {
			while (bPtr < b.length) {
				res[resPtr] = b[bPtr];
				resPtr = resPtr + 1;
				bPtr = bPtr + 1;
			}
		}
		return res;
	}
}