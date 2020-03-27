package task12concurrent;

/**
 * Demo of deadlock: two threads synchronize on the same resources in different order and waiting for each other to
 * reveal them. Threads can work normally at some runs, but the chance of deadlock is very high.
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
public class DeadLockExample {

    private static String A = "Resource1";
    private static String B = "Resource2";

    public static void main(String[] args) {
        new Thread(new FirstService()).start();
        new Thread(new SecondService()).start();
    }

    static class FirstService implements Runnable {

        @Override
        public void run() {
            synchronized (A) {
                synchronized (B) {
                    System.out.println("First service task is done");
                }
            }
        }
    }

    static class SecondService implements Runnable {

        @Override
        public void run() {
            synchronized (B) {
                synchronized (A) {
                    System.out.println("Second service task is done");
                }
            }
        }
    }

    /*Excerpt from Threads Report on debug:
    *
    * "Thread-1@3586199096" prio=5 tid=0xc nid=NA waiting for monitor entry
  java.lang.Thread.State: BLOCKED
	  at task12concurrent.DeadLockExample$SecondService.run(DeadLockExample.java:35)
	  - locked <0xd5c0dee0> (a java.lang.String)
	  at java.lang.Thread.run(Thread.java:745)

    "Thread-0@3586187056" prio=5 tid=0xb nid=NA waiting for monitor entry
  java.lang.Thread.State: BLOCKED
	  at task12concurrent.DeadLockExample$FirstService.run(DeadLockExample.java:23)
	  - locked <0xd5c0dea0> (a java.lang.String)
	  at java.lang.Thread.run(Thread.java:745)
    * */
}
