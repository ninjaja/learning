package task3Exceptions;

public class ExceptionsTesting {

    static int k = 0;
    public static void main(String[] args) {
        int[] array = new int[3];
        try {
            int j = array[3];
            throw new RuntimeException(); // doesn't get here
        } catch (Exception e) {
            System.out.println(processException(e));
        }
        System.out.println(testFinally()); // finally block worked AFTER return statement
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
