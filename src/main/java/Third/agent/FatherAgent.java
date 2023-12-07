package Third.agent;

import Third.queue.CombinationObject;
import Third.queue.Workload;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *  The FatherAgent class is designed to manage a set of elements and potentially perform combinatorial operations on this set.
 *  It uses concurrent processing to handle these tasks efficiently and includes database interaction capabilities.
 *
 * Concurrent Processing of Tasks
 * To handle these operations efficiently, especially when the number of combinations is large, FatherAgent employs concurrent processing.
 * This is indicated by the use of an ExecutorService, specifically a work-stealing pool.
 * Each thread independently processes a portion of the work, leading to faster overall execution.
 *
 * Division of Tasks
 * FatherAgent likely divides the problem into smaller, manageable tasks that can be executed concurrently.
 * This division is based on splitting the set of all possible combinations into smaller segments, with each segment being processed by a separate task.
 * For instance, if there are thousands of combinations to process, FatherAgent could create several tasks, each responsible for a subset of these combinations.
 * This division allows the work to be distributed evenly and processed in parallel.
 */
public class FatherAgent<E> {

    private final Set<E> originalSet;
    private final ExecutorService executor;
    private final Connection connection;

    public FatherAgent(Set<E> originalSet, Connection connection) {
        this.originalSet = originalSet;
        this.connection = connection;
        this.executor = Executors.newWorkStealingPool();
    }

    public void delegateWork() throws InterruptedException {
        long totalCombinations = (1L << originalSet.size());
        System.out.println("Total combinations: " + totalCombinations);

        long combinationCounter = 0;

        //Its similar logic to second form of PowerSetGenerator
        int blockSize = Math.max(0, (int) (totalCombinations / (Runtime.getRuntime().availableProcessors() * 4)));
        while (combinationCounter <= totalCombinations) {
            long start = combinationCounter + blockSize;
            combinationCounter += blockSize;
            long end = Math.min(start + blockSize, totalCombinations);

            Workload<E> workload = new CombinationObject<>(originalSet, start, end);

            executor.submit(() -> {
                String insertQuery = "INSERT INTO combinations (combination) VALUES (?)";
                try(PreparedStatement pstmt = connection.prepareStatement(insertQuery)){
                    pstmt.setString(1,new ChildAgent<>(workload).call().toString());
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });



            Thread.sleep(1);
        }

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
}
