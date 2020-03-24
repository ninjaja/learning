package task2threads;

/**
 * Simple demo of how {@code synchronized} keyword can be used
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
public class SynchronizedTesting {

    public static void main(String[] args) {
        new Thread(new TestSync("Thread1")).start();
        new Thread(new TestSync("Thread2")).start();
    }

    private synchronized void increment(String threadName) {
        int count = 0;
        for (int j = 0; j < 200; j++) {
            System.out.printf("%s : %s%n", threadName, ++count);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class TestSync implements Runnable {

        SynchronizedTesting instance = new SynchronizedTesting();
        String name;

        public TestSync(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            instance.increment(name);
        }
    }
}
