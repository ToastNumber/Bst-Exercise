import static org.junit.Assert.assertArrayEquals;

import org.junit.AfterClass;

import org.junit.runner.RunWith;

import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;

/**
 * Represents a class for testing a method in isolation by using the Genius
 * class for external methods.
 *
 * @author Kelsey McKenna
 *
 */
@RunWith(JUnitQuickcheck.class)
public abstract class IsolationTest {
	public final InputGenerator gen = new InputGenerator();

	/**
	 * Tests the immutability of the tree by running the assertion. It will run the assertion and then
	 * check that the operations didn't change the elements in the tree.
	 *
	 * @param bst
	 *            the tree whose immutability will be tested
	 * @param f
	 *            the method to run.
	 * @param action
	 *            run the property and check if it is consistent
	 */
	public void testImmutability(Bst<Integer, String> bst, TreeAction action) {
		for (int i = 0; i < 2; ++i) {
			MyEntry<Integer, String>[] elements = Genius.toArray(bst);
			// Check that a certain property is correct
			action.run(bst);
			// Now check that bst is definitely unaltered
			assertArrayEquals(elements, Genius.toArray(bst));
		}
	}
	
	@AfterClass
	public static void printResults() {
		System.out.println(Marks.getSummary());
	}

}
