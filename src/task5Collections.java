import java.math.BigDecimal;
import java.util.*;

public class task5Collections {

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

        System.out.println(treeSet.size()); //1
        System.out.println(hashSet.size()); //2
        System.out.println(treeMap.size()); //1
        System.out.println(hashMap.size()); //2
    }
}
