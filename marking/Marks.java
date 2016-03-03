import java.util.HashMap;
import java.util.Map;

/**
 * Represents a class for recording marks. Each test method name has
 * a mark associated with it.
 */
public class Marks {
	public static Map<String, Double> marks = new HashMap<>();
	public static Map<String, Integer> passCount = new HashMap<>();

	/**
	 * Allocate the mark to the given test name
	 * @param
	 *		mark the mark to be given
	 * @param
	 *		identifier the name of the test method
	 */
	public static void award(double mark, String identifier) {
		marks.put(identifier, mark);

		if (passCount.containsKey(identifier)) {
			passCount.put(identifier, passCount.get(identifier) + 1);
		} else passCount.put(identifier, 1);
	}

	public static double total() {
		double svar = 0;
		for (String identifier : marks.keySet()) {
			double mark = marks.get(identifier);

			if (passCount.get(identifier) == 100)
				svar += mark;
		}

		return svar;
	}

	public static String getSummary() {
		String svar = "\n";

		for (String s : marks.keySet()) {
			if (passCount.get(s) == 100) {
				double mark = marks.get(s);

				if (Math.abs(mark - Math.round(mark)) < 0.01)
					svar += String.format("%s = award %d%n", s, Math.round(mark));
				else
					svar += String.format("%s = award %.1f%n", s, mark);
			}
			else
				svar += String.format("%s = award 0%n", s);
		}

		double total = total();

		if (Math.abs(total - Math.round(total)) < 0.1) {
			return svar += String.format("Sum for this unit test: %d%n", Math.round(total));
		}
		else {
			return svar += String.format("Sum for this unit test: %.1f%n", total);
		}
	}
}
