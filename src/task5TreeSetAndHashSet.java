import java.math.BigDecimal;
import java.util.*;

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

        System.out.println(a.equals(b));
        System.out.println(a.compareTo(b) == 0);

        System.out.println(treeSet.size()); //1, TreeSet uses TreeMap inside, which uses compareTo() method,
            // which returns 0 for BigDecimals for objects that are
            // equal in value but have a different scale (like 2.0 and 2.00)

        System.out.println(hashSet.size()); //2, HashSet uses HashMap inside, which uses equals() method, which returns false in this case
        System.out.println(treeMap.size()); //1
        System.out.println(hashMap.size()); //2
    }
}
