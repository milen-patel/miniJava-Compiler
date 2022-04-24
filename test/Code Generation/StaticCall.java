class HelloWorld{

     public static void main(String []args){
        A a = null; // Errors out if a is set to null unless A.b is set to static
        //System.out.println(a.b.j);
        a.b.getFive();
        C1 c = null;
        c.c2.c2.c2.c2.c2.c2.c2.c2.c2.c2.c2.c2.c2.y = 10;
        System.out.println(c.c2.c2.c2.c2.c2.c2.c2.c2.c2.c2.c2.c2.c2.y);
      //  c.c2.c3.y = 5;
       // System.out.println(c.c2.c3.y);
        System.out.println(15);
        
     }
}

class A {
    public static B b;
}


class B {
    public static int j;
    public static void getFive() {
    	System.out.println(5);
    }
}





class C1 {
	static C1 c2;
	static int y;
	C1 c3;
}
