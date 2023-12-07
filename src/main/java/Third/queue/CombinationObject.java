package Third.queue;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CombinationObject<E> implements Workload<E> {

    private final Set<E> set;
    private final long startRange;
    private final long endRange;

    public CombinationObject(Set<E> set, long startRange, long endRange) {
        this.set = set;
        this.startRange = startRange;
        this.endRange = endRange;
    }

    //This method was explained in Second form of PowerSetGenerator
    @Override
    public Set<E> start() {
        List<E> elements = new ArrayList<>(set);

        for (long i = startRange; i < endRange; i++) {
            Set<E> combination = new HashSet<>();
            for (int bit = 0; bit < elements.size(); bit++) {
                if ((i & (1L << bit)) != 0) {
                    combination.add(elements.get(bit));
                }
            }
            if (!combination.isEmpty()) {
                return combination;
            }

        }
    return null;
    }
}
