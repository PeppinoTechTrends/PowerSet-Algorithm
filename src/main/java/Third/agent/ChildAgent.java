package Third.agent;

import Third.queue.Workload;

import java.util.Set;
import java.util.concurrent.Callable;

/**
 * ChildAgent is designed to execute these smaller tasks.
 * Each instance of ChildAgent could be handling a portion of the workload delegated by the FatherAgent.
 */
public class ChildAgent<E> implements Callable<Set<E>> {
    private final Workload<E> workload;

    public ChildAgent(Workload<E> workload) {
        this.workload = workload;
    }

    @Override
    public Set<E> call() {
        return  workload.start();
    }
}
