package task4Autoboxing;

/**
 * Demo of integers boxing/unboxing and of integer pool
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
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
        assert a != a1;

        //boxing c on initialization, then reference comparison
        assert a != c;

        //unboxing a, then compare base types:
        assert a != b;

        //unboxing b, then compare base types:
        assert b != c;

        //pool of Integers capacity demo (-128..127)
        assert b == b1;
        b = b + c;
        b1 = b1 + c;
        assert b == b1;
        assert e == e1; //pool is not for base types
        e = e + c;
        e1 = e1 + c;
        assert e != e1; //out of pool range
    }
}
