package task5Collections;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Demo of  {@code TreeSet} with default sorting and custom {@code Comparator}
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
public class TreeSetTesting {

    public static void main(String[] args) {
        Set<String> set = new TreeSet<>();
        Set<String> customSet = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.length() - o2.length();
            }
        });

        String[] array = {"abcd", "bac", "cd", "fe", "e"};
        Collections.addAll(set, array);
        Collections.addAll(customSet, array);

        assert ("[abcd, bac, cd, e, fe]").equals(set.toString());
        assert ("[e, cd, bac, abcd]").equals(customSet.toString());
    }
}
