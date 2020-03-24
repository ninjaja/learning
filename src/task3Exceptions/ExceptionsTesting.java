package task3Exceptions;

/**
 * Class to demonstrate 2 cases:
 *  1. How exceptions are handled by child-classes rather than parents
 *  2. How finally-block can change variable value even after returning the result
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */

public class ExceptionsTesting {

    private static int k = 0;

    public static void main(String[] args) {
        int[] array = new int[3];
        try {
            int j = array[3];
            throw new RuntimeException(); // doesn't get here
        } catch (Exception e) {
            assert processException(e) == 2;
        }
        assert testFinally() == 10;
    }

    private static int processException(Exception ex) {
        try {
            throw ex;
        } catch (IndexOutOfBoundsException e) {
            k = 2;
            return k;
        } catch (RuntimeException e) {
            k = 3;
            return k;
        } catch (Exception e) {
            k = 5;
            return k;
        } finally {
            k = 10;
        }
    }

    private static int testFinally() {
        return k;
    }
}
