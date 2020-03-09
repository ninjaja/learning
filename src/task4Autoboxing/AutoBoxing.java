package task4Autoboxing;

public class AutoBoxing {

    public static void main(String[] args) {

        Integer a = new Integer(2);
        Integer a1 = new Integer(2);
        int b = 2;
        int b1 = 2;
        Integer c = 2;
        int e = 128;
        int e1 = 128;

        //reference comparison:
        System.out.println("a == a1 " + (a == a1));

        //boxing c on initialization, then reference comparison
        System.out.println("a == c " + (a == c));

        //unboxing a, then compare base types:
        System.out.println("a == b " + (a == b));
        System.out.println("b == c " + (b == c));

        //pool of integers capacity demo (-128..127)
        System.out.println("b == b1 " + (b == b1));
        System.out.println("e == e1 " + (e == 1));
    }
}
