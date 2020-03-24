package task2threads;

/**
 * Model to imitate two events:
 *  1. when a {@code Waiter} gives an order to cook by {@code Chef}
 *  2. when an order is cooked
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
public class Kitchen {

    private static final int MIN_ORDERS_TO_START_COOKING = 1;
    private static final int MAX_ORDERS_IN_PROGRESS = 10;
    static final int MAX_ORDERS_PER_DAY = 20;

    private int orders = 0;
    synchronized void tellWaiterOrderIsReady() {
        while (orders < MIN_ORDERS_TO_START_COOKING) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        orders--;
        System.out.println("Chef started cooking next order...");
        System.out.println("Number of orders waiting to be cooked: " + orders);
        notify();
    }

    synchronized void placeOrder() {
        while (orders >= MAX_ORDERS_IN_PROGRESS) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        orders++;
        System.out.println("A new order is placed");
        System.out.println("Number of orders waiting to be cooked: " + orders);
        notify();
    }
}
