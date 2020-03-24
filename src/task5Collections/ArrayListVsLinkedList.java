package task5Collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Demo of {@code List} implementations performance
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
public class ArrayListVsLinkedList {

    private static final int AMOUNT = 200_000;
    private static final int AMOUNT_BY_INDEX = 100_000;

    private static List<Integer> arrayList = new ArrayList<>();
    private static List<Integer> linkedList = new LinkedList<>();

    public static void main(String[] args) {
        addElements(arrayList);
        addElements(linkedList);
        addElementsByIndex(arrayList);
        addElementsByIndex(linkedList);
        getElements(arrayList);
        getElements(linkedList);
        removeElements(arrayList);
        removeElements(linkedList);
    }

    private static void addElements(List<Integer> list) {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < AMOUNT; i++) {
            list.add(i);
        }
        long endTime = System.currentTimeMillis();
        float timeInSeconds = (endTime - startTime)/1000f;
        System.out.println(list.getClass().getName() + " add " + AMOUNT + " elements time: " + timeInSeconds);

    }

    private static void addElementsByIndex(List<Integer> list) {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < AMOUNT_BY_INDEX; i++) {
            list.add(i, i);
        }
        long endTime = System.currentTimeMillis();
        float timeInSeconds = (endTime - startTime)/1000f;
        System.out.println(list.getClass().getName() + " add " + AMOUNT_BY_INDEX + " elements by index time: " + timeInSeconds);

    }

    private static void getElements(List<Integer> list) {
        long startTime = System.currentTimeMillis();
        for (int i : list) {
            list.get(i);
        }
        long endTime = System.currentTimeMillis();
        float timeInSeconds = (endTime - startTime)/1000f;
        System.out.println(list.getClass().getName() + " get elements time: " + timeInSeconds);
    }

    private static void removeElements(List<Integer> list) {
        long startTime = System.currentTimeMillis();
        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
        long endTime = System.currentTimeMillis();
        float timeInSeconds = (endTime - startTime)/1000f;
        System.out.println(list.getClass().getName() + " remove elements time: " + timeInSeconds);
    }

    /*
    java.util.ArrayList add 200000 elements time: 0.011
    java.util.LinkedList add 200000 elements time: 0.017
    java.util.ArrayList add 100000 elements by index time: 8.736
    java.util.LinkedList add 100000 elements by index time: 8.649
    java.util.ArrayList get elements time: 0.013
    java.util.LinkedList get elements time: 41.678
    java.util.ArrayList remove elements time: 10.92
    java.util.LinkedList remove elements time: 0.007
    */


}
