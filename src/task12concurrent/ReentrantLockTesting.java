package task12concurrent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Demo of using ReentrantLock and its tryLock(long time, TimeUnit unit) method. Two threads try to change initial
 * common resource. Second run's output of the program (in comment below) shows that one of thread fails to change
 * resource if waiting time was specified too short.
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
public class ReentrantLockTesting {

    static String resource = "Initial resource";
    static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("HH:mm:ss ");
    static Lock lock;
    private static final int TIME_WAIT = 5000;
    private static final int TIME_SLEEP = 7000;

    public static void main(String[] args) {
        lock = new ReentrantLock();
        Thread thread1 = new Thread(new Locker("first", "First thread changed resource"));
        Thread thread2 = new Thread(new Locker("second", "Second thread changed resource"));

        printMessage(null);
        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Program execution ended");

        /* Console output example with TIME_WAIT = 7000 and TIME_SLEEP = 5000 :
            17:28:36 Initial resource
            17:28:36 Second thread changed resource
            17:28:41 second finished work
            17:28:41 First thread changed resource
            17:28:46 first finished work
            Program execution ended

          Console output example with TIME_WAIT = 5000 and TIME_SLEEP = 7000 :
            17:33:46 Initial resource
            17:33:46 Second thread changed resource
            17:33:53 second finished work
            17:33:58 first finished work
            Program execution ended
        */
    }

    static void printMessage (String message) {
        String text = dateFormat.format(LocalDateTime.now());
        text = Objects.isNull(message) ? text + resource : text + message;
        System.out.println(text);
    }

    static class Locker implements Runnable {

        String name;
        String text;

        public Locker(String name, String text) {
            this.name = name;
            this.text = text;
        }

        @Override
        public void run() {
            boolean locked = false;
            try {
                locked = lock.tryLock(TIME_WAIT, TimeUnit.MILLISECONDS);
                if (locked) {
                    resource = text;
                    printMessage(null);
                }
                Thread.sleep(TIME_SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                String text = name + " finished work";
                printMessage(text);
                if (locked) {
                    lock.unlock();
                }
            }
        }
    }
}
