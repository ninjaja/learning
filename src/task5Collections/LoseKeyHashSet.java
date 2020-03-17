package task5Collections;

import java.util.HashSet;
import java.util.Set;

/**
 * Demo of how an element can be lost in HashSet when it is mutable.
 */
public class LoseKeyHashSet {

    private static final String KEY_INITIAL = "keyInitial";

    public static void main(String[] args) {
        Set<MutableKey> set = new HashSet<>();

        MutableKey key = new MutableKey(KEY_INITIAL);

        set.add(key);
        printSetAndKeyValue(set, key);

        key.setValue("keyChanged");
        printSetAndKeyValue(set, key); // here element is not found

        key.setValue(KEY_INITIAL);
        printSetAndKeyValue(set, key); // element is found again

    }

    static void printSetAndKeyValue(Set<MutableKey> set, MutableKey key) {
        System.out.println("set: " + set);
        System.out.println("set.contains(" + key + "): " + set.contains(key));
        System.out.println("");
    }

    static class MutableKey {
        String value;

        public MutableKey(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MutableKey mutableKey = (MutableKey) o;

            return value != null ? value.equals(mutableKey.value) : mutableKey.value == null;
        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
