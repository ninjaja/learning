package task3Exceptions;

public class ExceptionsTesting2 {
/*RuntimeException отловится, но его предок Exception - нет*/

    public static void main(String[] args) {
        int[] array = new int[3];
        try {
            int j = array[2];
            throw new RuntimeException();

        } catch (IndexOutOfBoundsException e) {
            System.out.println("inside IndexOutOfBoundsException catch block");
        } catch (RuntimeException ex) {
            System.out.println("inside RTE catch block");
        } catch (Exception ex1) {
            System.out.println("inside Exception catch block");
        } finally {
            System.out.println("inside finally block");
        }
        System.out.println("inside program block");
    }
}
