package cli;

import algorithms.HeapSort;
import metrics.PerformanceTracker;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class BenchmarkRunner {

    private static final Random rand = new Random();

    public static void main(String[] args) {
        // Размеры массивов для тестирования (согласно требованию: 10^2 до 10^5)
        int[] sizes = {100, 1000, 5000, 10000, 50000, 100000};

        System.out.println("--- Запуск бенчмарков HeapSort с различными распределениями ---");
        System.out.println("Результаты будут записаны в heapsort_benchmark.csv");

        try (PrintWriter writer = new PrintWriter(new FileWriter("heapsort_benchmark.csv"))) {
            // Заголовок CSV
            writer.println("Size,Distribution,Time(ms),Comparisons,Swaps,ArrayAccesses");

            for (int n : sizes) {
                // 1. Тестирование на случайных данных
                runTest(writer, n, "Random", generateRandomArray(n));

                // 2. Тестирование на отсортированных данных (Лучший случай)
                runTest(writer, n, "Sorted", generateSortedArray(n));

                // 3. Тестирование на обратно отсортированных данных (Один из худших случаев)
                runTest(writer, n, "ReverseSorted", generateReverseSortedArray(n));

                // 4. Тестирование на массиве с дубликатами (почти отсортированном)
                runTest(writer, n, "Duplicates", generateDuplicatesArray(n));
            }

            System.out.println("\n✅ Бенчмарки завершены. Результаты сохранены.");

        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл:");
            e.printStackTrace();
        }
    }

    /**
     * Запускает один тест, собирает метрики и записывает их в CSV.
     */
    private static void runTest(PrintWriter writer, int size, String distribution, int[] originalArr) {
        // Используем клон, чтобы гарантировать, что сортировка начинается с чистого массива
        int[] arr = originalArr.clone();

        PerformanceTracker tracker = new PerformanceTracker("HeapSort-" + distribution);
        // Создаем экземпляр HeapSort и передаем ему трекер
        HeapSort sorter = new HeapSort(tracker);

        // Запуск таймера и сортировки
        tracker.startTimer();
        sorter.sort(arr); // Вызываем нестатический метод
        tracker.stopTimer();

        // Проверка корректности
        if (!HeapSort.isSorted(arr)) {
            System.err.printf("❌ ОШИБКА: Массив размера %d (%s) не отсортирован!%n", size, distribution);
        }

        double timeMs = tracker.getTimeMillis();

        // Запись в CSV
        writer.printf("%d,%s,%.3f,%d,%d,%d%n",
                size, distribution, timeMs,
                tracker.getComparisons(),
                tracker.getSwaps(),
                tracker.getArrayAccesses());

        // Вывод в консоль
        System.out.printf("n=%-6d | %-15s -> %.3f ms | Comps: %d | Swaps: %d | Accesses: %d%n",
                size, distribution, timeMs, tracker.getComparisons(), tracker.getSwaps(), tracker.getArrayAccesses());
    }

    // --- Методы генерации входных данных ---

    private static int[] generateRandomArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(size * 10);
        }
        return arr;
    }

    private static int[] generateSortedArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = i;
        }
        return arr;
    }

    private static int[] generateReverseSortedArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = size - i;
        }
        return arr;
    }

    private static int[] generateDuplicatesArray(int size) {
        int[] arr = new int[size];
        // Создаем массив, где большинство значений повторяются
        int uniqueRange = size / 10;
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(uniqueRange);
        }
        return arr;
    }
}
