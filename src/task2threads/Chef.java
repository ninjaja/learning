package task2threads;

public class Chef implements Runnable {

    private static final int MAX_COOKING_DURATION_IN_MILLIS = 10000;

    Kitchen kitchen;

    public Chef(Kitchen kitchen) {
        this.kitchen = kitchen;
    }

    @Override
    public void run() {
        for (int i = Kitchen.MAX_ORDERS_PER_DAY; i > 0; i--) {
            kitchen.tellWaiterOrderIsReady();
            try {
                Thread.sleep((long) (Math.random() * MAX_COOKING_DURATION_IN_MILLIS));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Order is ready. Orders left to cook before closing: " + (i-1));
        }
    }
}
