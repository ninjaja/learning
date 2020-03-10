package task4Autoboxing;

public class AutoBoxing {

    public static void main(String[] args) {

        Integer a = new Integer(2);
        Integer a1 = new Integer(2);
        int b = 127;
        int b1 = 127;
        Integer c = 2;
        Integer e = 127;
        Integer e1 = 127;

        //reference comparison:
        System.out.println("a == a1 " + (a == a1));

        //boxing c on initialization, then reference comparison
        System.out.println("a == c " + (a == c));

        //unboxing a, then compare base types:
        System.out.println("a == b " + (a == b));

        //unboxing b, then compare base types:
        System.out.println("b == c " + (b == c));

        //pool of Integers capacity demo (-128..127)
        System.out.println("b == b1 " + (b == b1)); //true
        b = b + c;
        b1 = b1 + c;
        System.out.println("b == b1 " + (b == b1)); //true, pool is not for base types
        System.out.println("e == e1 " + (e == e1));
        e = e + c;
        e1 = e1 + c;
        System.out.println("e == e1 " + (e == e1)); //false, out of pool range
    }
}
