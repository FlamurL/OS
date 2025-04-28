import java.util.Arrays;
import java.util.Random;

public class Grades {

    static double average = 0;
    static final BoundedRandomGenerator random = new BoundedRandomGenerator();
    private static final int ARRAY_LENGTH = 10000000;
    private static final int NUM_THREADS = 10;

    // Global sum and a lock object for synchronization
    static double totalSum = 0;
    static final Object lock = new Object();

    static void init() {
        totalSum = 0;
        average = 0;
    }

    // DO NOT CHANGE
    public static int[] getSubArray(int[] array, int start, int end) {
        return Arrays.copyOfRange(array, start, end);
    }

    public static void main(String[] args) {

        init();

        int[] arr = ArrayGenerator.generate(ARRAY_LENGTH);

        int partitionSize = ARRAY_LENGTH / NUM_THREADS;
        Thread[] threads = new Thread[NUM_THREADS];

        for (int i = 0; i < NUM_THREADS; i++) {
            int start = i * partitionSize;
            int end = (i == NUM_THREADS - 1) ? ARRAY_LENGTH : (start + partitionSize); // last thread takes any leftovers

            CalculateThread calculateThread = new CalculateThread(arr, start, end);
            threads[i] = new Thread(() -> calculateThread.calculateAverageGradeParallel());
            threads[i].start();
        }

        // Wait for all threads to complete
        for (int i = 0; i < NUM_THREADS; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        average = totalSum / ARRAY_LENGTH;

        // DO NOT CHANGE
        System.out.println("Your calculated average grade is: " + average);
        System.out.println("The actual average grade is: " + ArrayGenerator.actualAvg);

        SynchronizationChecker.checkResult();
    }

    static class CalculateThread {

        private int[] arr;
        int startSearch;
        int endSearch;

        public CalculateThread(int[] arr, int startSearch, int endSearch) {
            this.arr = arr;
            this.startSearch = startSearch;
            this.endSearch = endSearch;
        }

        public Double calculateAverageGrade() {
            return Arrays.stream(arr).average().getAsDouble();
        }

        public void calculateAverageGradeParallel() {
            double localSum = 0;
            for (int i = startSearch; i < endSearch; i++) {
                localSum += arr[i];
            }

            // Safely add to the global totalSum
            synchronized (lock) {
                totalSum += localSum;
            }
        }
    }

    /******************************************************
     // DO NOT CHANGE THE CODE BELOW TO THE END OF THE FILE
     *******************************************************/

    static class BoundedRandomGenerator {
        static final Random random = new Random();
        static final int RANDOM_BOUND_UPPER = 10;
        static final int RANDOM_BOUND_LOWER = 6;

        public int nextInt() {
            return random.nextInt(RANDOM_BOUND_UPPER - RANDOM_BOUND_LOWER) + RANDOM_BOUND_LOWER;
        }
    }

    static class ArrayGenerator {

        private static double actualAvg = 0;

        static int[] generate(int length) {
            int[] array = new int[length];

            for (int i = 0; i < length; i++) {
                int grade = Grades.random.nextInt();
                actualAvg += grade;
                array[i] = grade;
            }

            actualAvg /= array.length;

            return array;
        }
    }

    static class SynchronizationChecker {
        public static void checkResult() {
            if (ArrayGenerator.actualAvg != average) {
                throw new RuntimeException("The calculated result is not equal to the actual average grade!");
            }
        }
    }
}
