package task5Collections;

import java.util.HashMap;

/**
 * This class forces a HashMap to turn its bins from linked-list implementation to balanced Tree. For this to happen
 * number of objects in one bin should be not less than TREEIFY_THRESHOLD parameter (8) and number of populated bins
 * should be not less than MIN_TREEIFY_CAPACITY, otherwise simple resize of HashMap triggers. In this example treeifying
 * starts working on the iteration when j = 8 and i = 0.
 */
public class HashMapTreeifyTesting {

    static HashMap<Key, Integer> map = new HashMap<>();

    public static void main(String[] args) {
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 64; i++) {
                map.put(new Key(i), i);
            }
        }
    }

    static class Key implements Comparable {

        final int field;

        public Key(int field) {
            this.field = field;
        }

        @Override
        public int hashCode() {
            return field;
        }

        @Override
        public int compareTo(Object o) {
            return 0;
        }
    }


}
