package First;

import java.util.*;

public class PowerSetGenerator {

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        Set<String> columns = new HashSet<>(Arrays.asList(
                "a", "b","c","d","e","g","h","i","l","m","n","o","p","q","r","s","t","u","v","z",
                "1","2","3","4","5","6","7","8","9"));

        List<Set<String>> combinations = generateCombinations(columns);
        for (Set<String> combination : combinations) {
            System.out.println("Combination: " + combination);
            Thread.sleep(1);
        }
        long end = System.currentTimeMillis();
        System.out.println("Time to execute: " + (end - start) + "ms");
    }

    /**
    * Initial Check for an Empty Set:
    * The method begins by checking if the input set is empty.
    *  For an empty set, it returns a list containing only the empty set, as the power set of an empty set is
    *  a set containing just the empty set itself.
    *
    * Preparation for Recursion:
    * The input set is transformed into a list to facilitate ordered access to its elements.
    * The algorithm then identifies the first element of this list (referred to as the 'head') and
    * creates a new set containing the remaining elements (referred to as 'rest').
    *
    * Recursive Processing:
    * The method employs recursion, a fundamental programming technique where a function calls itself with a smaller portion of the problem.
    * In this case, the method recursively generates all combinations of the 'rest' set.
    *
    * Combination Formation:
    * For each subset returned by the recursive call, the algorithm constructs two new subsets:
         1) One subset includes the 'head' element along with the current subset from the recursion.
         2) The other subset is the current subset itself, without the 'head' element.
   * This step ensures that both possibilities – including or excluding the current element – are
   *  considered for each subset in the recursion.
   *
   * Accumulating Results:
   * All these newly formed subsets (both including and excluding the 'head' element) are added to a list.
   * This list accumulates the power set of the input set.
   *
   * Completion:
   * Once the recursion has processed all elements of the set, the method concludes by returning the accumulated list of subsets.
   *  This list represents the power set of the original input set.
   */
    public static List<Set<String>> generateCombinations(Set<String> set) {
        List<Set<String>> combinations = new ArrayList<>();
        if (set.isEmpty()) {
            combinations.add(new HashSet<>());
            return combinations;
        }

        List<String> list = new ArrayList<>(set);
        String head = list.get(0);
        Set<String> rest = new HashSet<>(list.subList(1, list.size()));

        for (Set<String> setCombination : generateCombinations(rest)) {
            Set<String> newSet = new HashSet<>();
            newSet.add(head);
            newSet.addAll(setCombination);
            combinations.add(newSet);
            combinations.add(setCombination);
        }

        return combinations;
    }
}
