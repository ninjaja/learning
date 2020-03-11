import java.math.BigDecimal;
import java.util.*;

/**
 * Demo of how {@code TreeSet} and {@code HashSet} compare objects before storing them, in particular {@code BigDecimal}.
 * TreeSet uses TreeMap inside, which uses compareTo() method, which returns 0 for BigDecimals for objects that are
 * equal in value but have a different scale (like 2.0 and 2.00).
 * HashSet uses HashMap inside, which uses equals() method, which returns false in this case
 */
public class task5TreeSetAndHashSet {

    public static void main(String[] args) {
        Set<BigDecimal> treeSet = new TreeSet<>();
        Set<BigDecimal> hashSet = new HashSet<>();
        Map<BigDecimal, String> treeMap = new TreeMap<>();
        Map<BigDecimal, String> hashMap = new HashMap<>();

        BigDecimal a = new BigDecimal("1.0");
        BigDecimal b = new BigDecimal("1");

        treeSet.add(a);
        treeSet.add(b);

        hashSet.add(a);
        hashSet.add(b);

        treeMap.put(a, null);
        treeMap.put(b, null);

        hashMap.put(a, null);
        hashMap.put(b, null);

        assert !a.equals(b);
        assert a.compareTo(b) == 0;

        assert treeSet.size() == 1;
        assert treeMap.size() == 1;
        assert hashSet.size() == 2;
        assert hashMap.size() == 2;

    }
}
