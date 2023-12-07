package Third.queue;

import java.util.Set;

    /**
    * The Workload<E> interface is a template for classes that need to implement
    *  a workload or task, which, when started, yields a set of results.
    */

public interface Workload<E> {
    Set<E> start();
}
