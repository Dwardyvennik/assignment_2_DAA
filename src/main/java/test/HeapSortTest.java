package test;

import algorithms.HeapSort;
import metrics.PerformanceTracker;
import java.util.Arrays;
import java.util.Random;

public class HeapSortTest {
    private static final Random random = new Random();

    public static void main(String[] args) {
        System.out.println("=== Комплексный тест HeapSort (Самопроверка) ===");

        testBasicFunctionality();
        testEdgeCases();
        testPerformance();
        testComparison();

        System.out.println("\n all test is correct!");
    }

    private static void testBasicFunctionality() {
        System.out.println("\n--- basic functionality test ---");

        testCase("empty array", new int[]{});
        testCase("one element", new int[]{5});
        testCase("already sort", new int[]{1, 2, 3, 4, 5});
        testCase("reveerse already sort", new int[]{5, 4, 3, 2, 1});
        testCase("duplicate", new int[]{7, 7, 7, 7});
        testCase("with duplicate", new int[]{3, 1, 4, 1, 5, 9, 2});
        testCase("negative numbers", new int[]{-3, -1, -5, 0, 2});
    }

    private static void testEdgeCases() {
        System.out.println("\n--- edge cases test ---");

        testCase("Two elements sorted", new int[]{1, 2});
        testCase("two elements reverse", new int[]{2, 1});
        testCase("all zero", new int[]{0, 0, 0, 0});
        testCase("large num", new int[]{1000, 500, 2000, 1500});
        testCase("negative and normal", new int[]{-5, 3, -2, 0, 7, -1});
    }
    private static void testPerformance() {
        System.out.println("\n--- Large array correctness tests ---");

        int[] sizes = {100, 1000, 10000};
        for (int size : sizes) {
            int[] arr = generateRandomArray(size);
            PerformanceTracker tracker = new PerformanceTracker();
            HeapSort sorter = new HeapSort(tracker);

            long startTime = System.nanoTime();
            sorter.sort(arr);
            long endTime = System.nanoTime();

            double timeMs = (endTime - startTime) / 1_000_000.0;
            boolean sorted = HeapSort.isSorted(arr);

            System.out.printf("Размер %6d: %8.3f мс, sorted: %s%n",
                    size, timeMs, sorted ? "✓" : "✗");

            if (!sorted) {
                System.err.println("ERROR: Array is not sorted.");
            }
        }
    }
    private static void testComparison() {
        System.out.println("\n- cross validation  -");

        int size = 10000;
        int[] arr1 = generateRandomArray(size);
        int[] arr2 = arr1.clone();

        // HeapSort
        PerformanceTracker tracker = new PerformanceTracker();
        HeapSort sorter = new HeapSort(tracker);

        long start1 = System.nanoTime();
        sorter.sort(arr1);
        long time1 = System.nanoTime() - start1;

        // Arrays.sort
        long start2 = System.nanoTime();
        Arrays.sort(arr2);
        long time2 = System.nanoTime() - start2;

        boolean resultsMatch = Arrays.equals(arr1, arr2);

        System.out.printf("HeapSort:   %8.3f мс%n", time1 / 1_000_000.0);
        System.out.printf("Arrays.sort:%8.3f мс%n", time2 / 1_000_000.0);
        System.out.printf("Результаты совпадают: %s%n", resultsMatch ? "✓" : "✗");
    }

    private static void testCase(String description, int[] input) {
        int[] original = input.clone();
        int[] arr = input.clone();

        PerformanceTracker tracker = new PerformanceTracker();
        HeapSort sorter = new HeapSort(tracker);

        sorter.sort(arr);
        boolean isCorrect = HeapSort.isSorted(arr);
        boolean preservesElements = arraysHaveSameElements(original, arr);
        String status = (isCorrect && preservesElements) ? "✓ PASS" : "✗ FAIL";
        System.out.printf("%s: %s%n", status, description);

        if (!isCorrect || !preservesElements) {
            System.out.printf("   Вход:  %s%n", Arrays.toString(original));
            System.out.printf("   Выход: %s%n", Arrays.toString(arr));
        }
    }

    private static boolean arraysHaveSameElements(int[] arr1, int[] arr2) {
        if (arr1.length != arr2.length) return false;

        int[] copy1 = arr1.clone();
        int[] copy2 = arr2.clone();
        Arrays.sort(copy1);
        Arrays.sort(copy2);
        return Arrays.equals(copy1, copy2);
    }
    private static int[] generateRandomArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt(size * 10);
        }
        return arr;
    }
}
