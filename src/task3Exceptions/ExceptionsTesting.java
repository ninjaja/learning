package task3Exceptions;

public class ExceptionsTesting {
/*срабатывание дочерних классов исключений, но 9я строка не выполнится и RuntimeException не отловится*/

    public static void main(String[] args) {
        int[] array = new int[3];
        try {
            int j = array[3];
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
