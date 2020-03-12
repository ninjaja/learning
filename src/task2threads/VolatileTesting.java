package task2threads;

/**
 * Demo of how {@code volatile} keyword can be used. With it this example triggers {@code Listener} after each
 * change of {@code count} value and console output is a sequence of both threads' output. While without {@code volatile}
 * console output will contain mostly (in some cases only) {@code ChangeMaker} output because JVM does not guess that
 * value is needed to be updated from main memory to cache.
 */
public class VolatileTesting {

    private static volatile int count = 0;

    public static void main(String[] args) {
        new Thread(new Listener()).start();
        new Thread(new ChangeMaker()).start();
    }

    static class Listener implements Runnable {

        @Override
        public void run() {
            int local = count;
            while (local < 5) {
                if (local != count) {
                    System.out.println("Received count change to: " + count);
                    local = count;
                }
            }
        }
    }

    static class ChangeMaker implements Runnable {

        @Override
        public void run() {
            int local = count;
            while (local < 5) {
                count = ++local;
                System.out.println("Changed count to: " + count);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
