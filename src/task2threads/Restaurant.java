package task2threads;

/**
 * Demo of interaction between a {@code Waiter} and {@code Chef} threads
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
public class Restaurant {

    public static void main(String[] args) {
        Kitchen kitchen = new Kitchen();
        new Thread(new Waiter(kitchen)).start();
        new Thread(new Chef(kitchen)).start();
    }
}
