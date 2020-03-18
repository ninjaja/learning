package task5Collections;

import java.util.HashMap;

public class HashMapEqualsAndHashcode {

    public static void main(String[] args) {

        HashMap<Key, Integer> map = new HashMap<>();
        KeyWithHashcodeAndEquals key = new KeyWithHashcodeAndEquals(2);
        KeyWithHashcodeAndEquals key0 = new KeyWithHashcodeAndEquals(3);
        map.put(key, 23);
        map.put(key0, 24);
        assert (map.get(key) == 23);

        key.setI(3);
        assert (map.size() == 2);
        assert (map.get(key) == 24); //combo: key became unreachable and then overridden with another element

        HashMap<Key, Integer> map1 = new HashMap<>();
        KeyWithRandomHashcode key1 = new KeyWithRandomHashcode(1);
        KeyWithRandomHashcode key2 = new KeyWithRandomHashcode(1);
        map1.put(key1, 2);
        map1.put(key2, 2);
        assert (map1.size() == 1);
        assert (map1.get(key1) == null); // key is unreachable
        assert (map1.get(key2) == null); // key is unreachable

        HashMap<Key, Integer> map2 = new HashMap<>();
        KeyWithFalseEquals key3 = new KeyWithFalseEquals(1);
        KeyWithFalseEquals key4 = new KeyWithFalseEquals(1);
        map2.put(key3, 2);
        map2.put(key4, 3);
        assert (map2.size() == 2);
        assert (map1.get(key3) == null); // key is unreachable
        assert (map1.get(key4) == null); // key is unreachable

    }

    interface Key {}

    /**
     * Mutable class with correctly overridden equals() and hashCode(). Objects of this class will be unreachable via get() if
     * a field is changes after adding to HashMap.
     */
    static class KeyWithHashcodeAndEquals implements Key {

        int i;

        public KeyWithHashcodeAndEquals(int i) {
            this.i = i;
        }

        public void setI(int i) {
            this.i = i;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            KeyWithHashcodeAndEquals that = (KeyWithHashcodeAndEquals) o;

            return i == that.i;
        }

        @Override
        public int hashCode() {
            return i;
        }
    }

    /**
     * Objects of this class will always be put to a single bucket thus HashMap will behave as LinkedList and after
     * exceeding TREEIFY_THRESHOLD (8) - as a TreeMap (since Java 8).
     */
    static class KeyWithSameHashcode implements Key {

        int i;

        public KeyWithSameHashcode(int i) {
            this.i = i;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            KeyWithSameHashcode that = (KeyWithSameHashcode) o;

            return i == that.i;
        }

        @Override
        public int hashCode() {
            return 0;
        }
    }

    /**
     * Objects of this class will always be unpredictable during attempts to get them from HashMap.
     */
    static class KeyWithRandomHashcode implements Key {

        int i;

        public KeyWithRandomHashcode(int i) {
            this.i = i;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            KeyWithRandomHashcode that = (KeyWithRandomHashcode) o;

            return i == that.i;
        }

        @Override
        public int hashCode() {
            return i++;
        }
    }

    /**
     * Objects of this class will always be unreachable during attempts to get them from HashMap. Even if logically
     * they are equal, they will be placed as different objects.
     */
    static class KeyWithFalseEquals implements Key {

        int i;

        public KeyWithFalseEquals(int i) {
            this.i = i;
        }

        @Override
        public boolean equals(Object o) {
            return false;
        }

        @Override
        public int hashCode() {
            return i;
        }
    }
}
