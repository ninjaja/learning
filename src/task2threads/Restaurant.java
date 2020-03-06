package task2threads;

public class Restaurant {

    public static void main(String[] args) {
        Kitchen kitchen = new Kitchen();
        new Thread(new Waiter(kitchen)).start();
        new Thread(new Chef(kitchen)).start();
    }
}
