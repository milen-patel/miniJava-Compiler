class HelloWorld{

     public static void main(String []args){
        A a = null; // Errors out if a is set to null unless A.b is set to static
        //System.out.println(a.b.j);
        a.b.getFive();
        System.out.println(10);
     }
}

class A {
    public B b;
}


class B {
    public static int j;
    public static void getFive() {
    	System.out.println(5);
    }
}