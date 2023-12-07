package Second;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class PowerSetGenerator {

    public static void main(String[] args) {
        Set<String> set = new HashSet<>(Arrays.asList(
                "a", "b", "c", "d", "e", "g", "h", "i", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "z",
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
                "21", "22", "22", "23", "24", "25"));

        long start = System.currentTimeMillis();
        processCombinations(set);
        long end = System.currentTimeMillis();

        System.out.println("Time to execute: " + (end - start) + "ms");
    }

    /**
     * Using Bitwise Shifts to Calculate 2^n:
     * <p>
     * The expression 1L << set.size() is a bitwise operation. Here's how it computes 2^n :
     * 1L is a long literal with a value of 1. In binary, this is represented as a single 1 followed by zeros (e.g., ...0001).
     * << is the left shift operator. It shifts the bits of the number on the left (1 in this case) to the left by the number of positions
     * specified on the right (which is columns.size() here).
     * Shifting 1 to the left n times is equivalent to multiplying 1 by 2^n, because each left shift doubles the number (just as each right shift halves it).
     * For example, shifting 1 left by 3 places (1 << 3) results in 1000 in binary, which is 8 in decimal, or 2^3.
     */

    private static void processCombinations(Set<String> set) {
        ExecutorService executor = Executors.newWorkStealingPool();
        long totalCombinations = 1L << set.size();
        System.out.println("Total combinations: " + totalCombinations);

        try {
            dispatchTasks(executor, set, totalCombinations);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            shutdownExecutor(executor);
        }
    }


    /**
     * Determine the Number of Processors:
     * Identify the number of available processors. This will be used to determine how many tasks to create and how to distribute the workload.
     * <p>
     * Calculate Block Size for Each Task:
     * Compute the size of each block of combinations that each task will handle.
     * This is done by dividing the total number of combinations by the number of available processors, ensuring that the workload is evenly distributed.
     * If the total number of combinations is not perfectly divisible, the last task may handle a slightly different number of combinations.
     * <p>
     * Create and Dispatch Tasks:
     * Iterate a number of times equal to the number of available processors.
     * For each iteration, calculate the specific range of combinations that the task should generate. This can be done by multiplying the iterator index by the block size to find the start of the range and adding the block size to find the end of the range.
     * Create a task for each range and submit it to an executor service.
     * Each task is responsible for generating the combinations within its assigned range.
     * <p>
     * Handle Task Completion:
     * After submitting all tasks, wait for their completion.
     * This can be done by keeping a list of futures and calling get() on each future, which blocks until the task is completed.
     */
    private static void dispatchTasks(ExecutorService executor, Set<String> columns, long totalCombinations) throws InterruptedException {
        long combinationCounter = 0;
        int blockSize = Math.max(0, (int) (totalCombinations / (Runtime.getRuntime().availableProcessors() * 4)));

        for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
            long startRange = combinationCounter + blockSize;
            long endRange = Math.min(startRange + blockSize, totalCombinations);
            executor.submit(new CombinationTask(new ArrayList<>(columns), startRange, endRange));
            Thread.sleep(1);
        }
    }

    private static void shutdownExecutor(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(1, TimeUnit.HOURS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            executor.shutdownNow();
        }
    }


    static class CombinationTask implements Callable<Void> {
        private final List<String> elements;
        private final long startRange;
        private final long endRange;

        public CombinationTask(List<String> elements, long startRange, long endRange) {
            this.elements = elements;
            this.startRange = startRange;
            this.endRange = endRange;
        }

        /**
         * The call method in question is part of a task designed to generate subsets (combinations) of a given set of elements.
         * It employs a combination of looping and bitwise operations to systematically determine which elements to include in each subset.
         * Here's a conceptual explanation of its working mechanism:
         * <p>
         * Iterating Over a Specified Range:
         * The method processes a specific range of numbers.
         * Each number in this range represents a potential combination of the set's elements. The range is determined by start and end ,
         * ensuring that the task handles only a portion of all possible combinations,
         * suitable for execution in a multi-threaded environment.
         * <p>
         * Generating Combinations Using Bitwise Operations:
         * For each number in the specified range, the method generates a combination.
         * It does this by examining the binary representation of the number.
         * Each bit in the number corresponds to an element in the set:
         * if a bit is set (1), the corresponding element is included in the combination;
         * if a bit is not set (0), the element is not included.
         * <p>
         * Building Each Combination:
         * The method loops through each element of the set.
         * Using bitwise operations, it checks whether the corresponding bit for each element is set in the current number.
         * If so, the element is added to the current combination. This process effectively translates the binary number into a set of elements.
         * <p>
         * Outputting the Combinations:
         * Each generated combination, represented as a set of elements, is printed out.
         * This step visualizes the subsets formed from the original set of elements.
         */
        @Override
        public Void call() {
            for (long i = startRange; i < endRange; i++) {
                Set<String> combination = new HashSet<>();
                for (int bit = 0; bit < elements.size(); bit++) {
                    if ((i & (1L << bit)) != 0) {
                        combination.add(elements.get(bit));
                    }
                }
                if (!combination.isEmpty()) {
                    System.out.println(combination);
                }
            }
            return null;
        }
    }
}

