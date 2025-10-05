package metrics;

public class PerformanceTracker {
    private long comparisons;
    private long swaps;
    private long arrayAccesses;
    private long startTime;
    private long endTime;

    private String algorithmName;

    public PerformanceTracker() {
        this("Default Algorithm");
    }

    public PerformanceTracker(String name) {
        this.algorithmName = name;
        reset();
    }

    public void reset() {
        comparisons = 0;
        swaps = 0;
        arrayAccesses = 0;
        startTime = 0;
        endTime = 0;
    }

    public void startTimer() {
        startTime = System.nanoTime();
    }
    public void stopTimer() {
        endTime = System.nanoTime();
    }

    public void incrementComparisons() {
        comparisons++;
    }

    public void incrementSwaps() {
        swaps++;
    }

    public void incrementArrayAccesses(long count) {
        arrayAccesses += count;
    }

    public void incrementArrayAccesses() {
        arrayAccesses++;
    }

    public long getComparisons() { return comparisons; }
    public long getSwaps() { return swaps; }
    public long getArrayAccesses() { return arrayAccesses; }

    public long getTimeNanos() { return endTime - startTime; }
    public double getTimeMillis() {
        long durationNanos = endTime - startTime;
        if (durationNanos <= 0) return 0.0;
        return durationNanos / 1_000_000.0;
    }
    public double getDurationMs() { return getTimeMillis(); }

    public String getSummary() {
        return String.format(
                "[%s] Time: %.3f ms | Comparisons: %d | Swaps: %d | Array Accesses: %d",
                algorithmName, getTimeMillis(), comparisons, swaps, arrayAccesses
        );
    }
}
