import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;

// No points awarded or deducted.

public class PropertiesConstructor extends IsolationTest {

	/**
	 * This tests that the constructor of the student's Fork class actually saves
	 * the values it is given, and that the getters return the right results.
	 * It's not obvious here, but methods like Genius.toArray(...) rely on these being correct.
	 *
	 * @param entries
	 *            the entries used to build up the tree
	 */
	@Property
	public void constructorShouldSaveAllValues(Map<Integer, String> entries) {
		Bst<Integer, String> bst = new Empty<>();

		for (java.util.Map.Entry<Integer, String> entry : entries.entrySet())
			bst = Genius.put(bst, entry.getKey(), entry.getValue());

		Map<Integer, String> actualEntries = new HashMap<Integer, String>();
		for (MyEntry<Integer, String> entry : Genius.toArray(bst))
			actualEntries.put(entry.getKey(), entry.getValue());

		assertEquals(entries, actualEntries);

		Marks.award(0, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * Checks that the student's Fork constructor throws an assertion error when it's given bad trees
	 *
	 * @param v
	 *            a random value
	 * @param c1
	 *            a random tree
	 * @param c2
	 *            a random tree
	 */
	@Property
	public void constructorShouldNotAcceptBadSubtrees(String v, @From(TreeGenerator.class) TreeWrapper c1, @From(TreeGenerator.class) TreeWrapper c2) {

		Bst<Integer, String> t1 = c1.get(), t2 = c2.get();
		Bst<Integer, String> toLeft, toRight;

		// Try placing big-middle-small
		if (!Genius.isEmpty(t1) && !Genius.isEmpty(t2)) {
			Integer leftKey = t1.getKey().get();
			Integer rightKey = t2.getKey().get();
			Integer k = leftKey / 2 + rightKey / 2;

			// Put the subtrees in the wrong place
			if (leftKey < rightKey) {
				toLeft = t2;
				toRight = t1;
			} else {
				toLeft = t1;
				toRight = t2;
			}

			try {
				new Fork<>(k, v, toLeft, toRight);
				assertTrue(false); // if the Fork constructor is correct, shouldn't reach here.
			} catch (AssertionError shouldHappen) {
				assertTrue(true);
			}
		} else if (!Genius.isEmpty(t1)) {
			// Try placing the same key in one of the subtrees
			try {
				new Fork<>(t1.getKey().get(), v, t1, new Empty<>());
				assertTrue(false); // if the Fork constructor is correct, shouldn't reach here.
			} catch (AssertionError expected) {
				assertTrue(true);
			}
		} else if (!Genius.isEmpty(t2)) {
			try {
				new Fork<>(t2.getKey().get(), v, t2, t2);
				assertTrue(false); // if the Fork constructor is correct, shouldn't reach here.
			} catch (AssertionError expected) {
				assertTrue(true);
			}
		} // else we Can't check anything with what we're given

		Marks.award(0, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

}
