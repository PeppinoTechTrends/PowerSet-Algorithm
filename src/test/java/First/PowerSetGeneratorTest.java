package First;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class PowerSetGeneratorTest {

    @Test
    public void testGenerateCombinations() {
        Set<String> inputSet = new HashSet<>(Arrays.asList("a", "b"));
        List<Set<String>> expectedCombinations = Arrays.asList(
                Collections.emptySet(),
                Collections.singleton("a"),
                Collections.singleton("b"),
                new HashSet<>(Arrays.asList("a", "b"))
        );

        List<Set<String>> actualCombinations = PowerSetGenerator.generateCombinations(inputSet);

        assertNotNull(actualCombinations, "Il risultato non dovrebbe essere null");
        assertEquals(expectedCombinations.size(), actualCombinations.size(), "La dimensione delle combinazioni generate dovrebbe corrispondere");

        // Verifica che tutte le combinazioni attese siano presenti
        for (Set<String> expectedSet : expectedCombinations) {
            assertTrue(actualCombinations.contains(expectedSet), "La combinazione attesa dovrebbe essere presente");
        }
    }
}
