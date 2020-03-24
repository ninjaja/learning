package task5Collections;

import java.util.TreeSet;

/**
 * Demo of how TreeSet can be re-ordered for changed values of mutable objects.
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */

public class UpdatableTreeSetTesting {

    public static void main(String[] args) {
        UpdatableTreeSet<Foo> set = new UpdatableTreeSet<>();
        Foo foo1 = new Foo("one", 1);
        Foo foo2 = new Foo("two", 2);
        Foo foo3 = new Foo("three", 3);
        set.add(foo1);
        set.add(foo2);
        set.add(foo3);
        assert (set.toString().equals("one two three ")); // correct ordering

        foo1.setA(3);
        foo2.setA(1);
        foo3.setA(2);
        assert (set.toString().equals("one two three ")); // ordering is broken after values changes
        set.update(); // updating set manually
        assert (set.toString().equals("two three one ")); // ordering fixed

        set.update(foo1, 0); // embedded update of elements
        set.update(foo2, 4);
        set.update(foo3, 3);
        assert (set.toString().equals("one three two ")); // set is re-ordered without additional actions

    }

    /**
     * Class implementing TreeSet update logic in two ways: after objects were changed from outside and from within
     * the {@code UpdatableTreeSet} via {@code boolean update(T element, int value} method
     * @param <T> the type of elements maintained by this set
     */
    static class UpdatableTreeSet<T extends Updatable> extends TreeSet<T> {
        boolean update(T element, int value) {
            if (remove(element)) {
                element.setA(value);
                return add(element);
            } else {
                return false;
            }
        }

        void update() {
            UpdatableTreeSet newSet = new UpdatableTreeSet();
            for (T foo : this) {
                newSet.add(foo);
            }
            this.clear();
            this.addAll(newSet);
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            for(T foo : this) {
                builder.append(foo.getName()).append(" ");
            }
            return builder.toString();
        }
    }

    interface Updatable {
        void setA(int value);
        int getA();
        String getName();
    }

    static class Foo implements Updatable, Comparable<Foo> {

        String name;
        int a;

        public Foo(String name, int a) {
            this.name = name;
            this.a = a;
        }

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Foo foo = (Foo) o;

            if (a != foo.a) return false;
            return name != null ? name.equals(foo.name) : foo.name == null;
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + a;
            return result;
        }

        @Override
        public int compareTo(Foo foo) {
            return this.getA() - foo.getA();
        }
    }
}
