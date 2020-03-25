package task10streams;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Demo of performance difference between stream and parallelStream. As can be seen in console outputs put in comments below:
 * 1. For few elements performance difference is non-essential.
 * 2. It depends on data structure. Even though on ArrayList parallelStream performs better than sequential, LinkedList
 * can be a challenge for it.
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
public class StreamsTesting {

    static ArrayList<Long> numbersArrayList = new ArrayList<>();
    static LinkedList<Long> numbersLinkedList = new LinkedList<>();
    static ArrayList<Long> moreNumbersArrayList = new ArrayList<>();
    static LinkedList<Long> moreNumbersLinkedList = new LinkedList<>();

    public static void main(String[] args) {
        for (long i = 0; i < 100_000; i++) {
            numbersArrayList.add(i);
        }

        for (long i = 0; i < 100_000; i++) {
            numbersLinkedList.add(i);
        }

        for (long i = 0; i < 20_000_000; i++) {
            moreNumbersArrayList.add(i);
        }

        for (long i = 0; i < 20_000_000; i++) {
            moreNumbersLinkedList.add(i);
        }

        testSequentialSum(numbersLinkedList);
        testParallelSum(numbersLinkedList);
        testSequentialSum(moreNumbersArrayList);
        testParallelSum(moreNumbersLinkedList);

        // Sequential stream sum of 100000 elements in java.util.ArrayList done in: 0.063 sec
        // Parallel stream sum of 100000 elements in java.util.ArrayList done in: 0.068 sec
        // Sequential stream sum of 100000 elements in java.util.LinkedList done in: 0.093 sec
        // Parallel stream sum of 100000 elements in java.util.LinkedList done in: 0.099 sec

        // Sequential stream sum of 20000000 elements in java.util.ArrayList done in: 48.632 sec
        // Parallel stream sum of 20000000 elements in java.util.ArrayList done in: 7.142 sec
        // Sequential stream sum of 20000000 elements in java.util.LinkedList done in: 44.62 sec
        // Parallel stream sum of 20000000 elements in java.util.LinkedList done in: 51.038 sec
    }

    private static void testParallelSum(List<Long> list) {
        long startTime = System.currentTimeMillis();
        list.parallelStream().reduce(Long::sum);
        long endTime = System.currentTimeMillis();
        float timeInSeconds = (endTime - startTime) / 1000f;
        System.out.println("Parallel stream sum of " + list.size() + " elements in " + list.getClass().getName() + " " +
                "done in: " + timeInSeconds + " sec");
    }

    private static void testSequentialSum(List<Long> list) {
        long startTime = System.currentTimeMillis();
        list.stream().reduce(Long::sum);
        long endTime = System.currentTimeMillis();
        float timeInSeconds = (endTime - startTime) / 1000f;
        System.out.println("Sequential stream sum of " + list.size() + " elements in " + list.getClass().getName() +
                " done in: " + timeInSeconds + " sec");
    }
}
