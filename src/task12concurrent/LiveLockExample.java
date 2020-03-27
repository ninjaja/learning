package task12concurrent;

/**
 * Simple live lock demo: two cars are trying to pass a narrow bridge and waiting for each other. Console output is an
 * infinite sequence of honking from both cars.
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
public class LiveLockExample {

    public static void main(String[] args) {
        Car car1 = new Car("First driver");
        Car car2 = new Car("Second driver");
        new Thread(() -> car1.passBridgeOnlyAfter(car2)).start();
        new Thread(() -> car2.passBridgeOnlyAfter(car1)).start();
    }

    static class Car {

        String name;
        boolean isWaiting = true;

        public Car(String name) {
            this.name = name;
        }

        void passBridgeOnlyAfter(Car car) {
            while (car.isWaiting) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(name + ": Beeeep!!!");
            }
            System.out.println(name + "has passed the bridge");
            isWaiting = false;
        }
    }
}
