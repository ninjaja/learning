package task5Collections;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Objects;

/**
 * Demo of how HashMap turns its bins from linked-list implementation to balanced Tree - this can be seen with the help
 * of reflection API by getting Hashmap's {@code table} field and check if its nodes types are TreeNode. For this to happen
 * number of objects in one bin should be not less than TREEIFY_THRESHOLD parameter (8) and number of populated bins
 * should be not less than MIN_TREEIFY_CAPACITY, otherwise simple resize of HashMap triggers. In this example treeifying
 * starts working on the iteration when j = 8 and i = 0.
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
public class HashMapTreeifyTesting {

    static HashMap<Key, Integer> map = new HashMap<>();

    public static void main(String[] args) {
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 64; i++) {
                map.put(new Key(i), i);
            }
        }
        try {
            Field tableField = map.getClass().getDeclaredField("table");
            tableField.setAccessible(true);
            Object[] tableArray = (Object[]) tableField.get(map);
            for (Object node : tableArray) {
                if (Objects.nonNull(node)) {
                    assert node.getClass().getName().equals("java.util.HashMap$TreeNode");
                }
            }
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
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
