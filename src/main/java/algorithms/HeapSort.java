package algorithms;

import metrics.PerformanceTracker;

public class HeapSort {
    private final PerformanceTracker tracker;

    /*
     * @param tracker object for recording performance data.
     */
    public HeapSort(PerformanceTracker tracker) {
        this.tracker = tracker;
    }
    // sort in non-decreasing order.
    public void sort(int[] arr) {
        if (arr == null || arr.length <= 1) return;
        int n = arr.length;

        // max-heap
        // use non sttic  heapify
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }

        for (int i = n - 1; i > 0; i--) {
            // mv root to the end
            swap(arr, 0, i);
            // restore the heap property on reduced part.
            heapify(arr, i, 0);
        }
    }
    // n-heap size
    private void heapify(int[] arr, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        // check L child
        if (left < n) {
            tracker.incrementComparisons();
            if (arr[left] > arr[largest]) {
                largest = left;
            }
        }

        if (right < n) {
            tracker.incrementComparisons(); // Comparison accounting
            if (arr[right] > arr[largest]) {
                largest = right;
            }
        }

        // if the large element is not the root, swap and continue sifting.
        if (largest != i) {
            swap(arr, i, largest);
            heapify(arr, n, largest);
        }
    }
    private void swap(int[] arr, int i, int j) {
        tracker.incrementSwaps();
        tracker.incrementArrayAccesses(4);

        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    public static boolean isSorted(int[] arr) {
        if (arr == null || arr.length <= 1) return true;
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1]) return false;
        }
        return true;
    }
}
