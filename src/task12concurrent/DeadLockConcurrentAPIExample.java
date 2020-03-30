package task12concurrent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Demo of a deadlock with the use of java.util.concurrent API
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
public class DeadLockConcurrentAPIExample {

    private static String resource1 = "Resource1";
    private static String resource2 = "Resource2";
    private static Lock lock1 = new ReentrantLock();
    private static Lock lock2 = new ReentrantLock();

    public static void main(String[] args) {
        new Thread(new Thread1()).start();
        new Thread(new Thread2()).start();
    }

    static class Thread1 implements Runnable {
        @Override
        public void run() {
            lock1.lock();
            System.out.println("Thread1 holding: " + resource1);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock2.lock();
            lock1.unlock();
            System.out.println("Thread1 holding: " + resource2);
            lock2.unlock();
        }
    }

    static class Thread2 implements Runnable {
        @Override
        public void run() {
            lock2.lock();
            System.out.println("Thread2 holding: " + resource1);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock1.lock();
            lock2.unlock();
            System.out.println("Thread2 holding: " + resource2);
            lock1.unlock();
        }
    }
}
