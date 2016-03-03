import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;

// Add points 1,4,5,1 respectively for each passed property (total 11)

public class PropertiesBalanced extends IsolationTest {

	/**
	 * Checked that the empty tree is still empty after being balanced.
	 */
	@Property
	public void emptyBalancedShouldBeEmpty() {
		assertTrue(Genius.isEmpty(new Empty<Integer, String>().balanced()));
		Marks.award(1, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * Check that the resulting tree is the correct height after balancing.
	 *
	 * @param c
	 */
	@Property
	public void balancingShouldProduceCorrectHeight(@From(TreeGenerator.class) TreeWrapper c) {
		Bst<Integer, String> bst = c.get();

		Bst<Integer, String> correctBal = Genius.balanced(bst);
		Bst<Integer, String> testBal = bst.balanced();

		// Make sure they are the same height
		assertTrue(Genius.height(correctBal) == Genius.height(testBal));

		Marks.award(4, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * After balancing the tree, it should have the same entries.
	 *
	 * @param c the tree to work on
	 */
	@Property
	public void balancingShouldNotChangeEntries(@From(TreeGenerator.class) TreeWrapper c) {
		Bst<Integer, String> testBst = c.get();
		Bst<Integer, String> geniusBalanced = Genius.balanced(testBst);
		Bst<Integer, String> testBalanced = testBst.balanced();

		assertArrayEquals(Genius.toArray(geniusBalanced), Genius.toArray(testBalanced));
		Marks.award(5, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * The tree should be immutable, i.e. t should be unchanged after calling t.balanced().
	 *
	 * @param c
	 *            the tree to work on
	 */
	@Property
	public void shouldBeImmutable(@From(TreeGenerator.class) TreeWrapper c) {
		testImmutability(c.get(), t -> t.balanced());
		Marks.award(1, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

}
