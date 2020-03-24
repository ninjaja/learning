package task2threads;

/**
 * Imitation of a waiter in a restaurant who receives orders with random intervals.
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
public class Waiter implements Runnable {

    private Kitchen kitchen;

    public Waiter(Kitchen kitchen) {
        this.kitchen = kitchen;
    }

    @Override
    public void run() {
        for (int i = 0; i < Kitchen.MAX_ORDERS_PER_DAY; i++) {
            kitchen.placeOrder();
            try {
                Thread.sleep((long) (Math.random() * 5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
