import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;

// Add points 1,1,2,2,1,1,1,1 respectively for each passed property (total 10)

/**
 * Tests the <code>smaller</code> and <code>bigger</code> methods in the Bst interface, but is only allowed to use methods before
 * <code>delete()</code>, i.e.:
 * <code>isEmpty(), smaller(), bigger(), has(), find(), put()</code>.
 *
 * @author Kelsey McKenna
 *
 */
public class PropertiesSmallerBigger extends IsolationTest {
	/**
	 * empty.smaller(?) should always be true
	 *
	 * @param k
	 *            a random key
	 */
	@Property
	public void emptyShouldBeSmaller(Integer k) {
		assertTrue(new Empty<Integer, String>().smaller(k));
		
		Marks.award(1, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * empty.bigger(?) should always be true
	 *
	 * @param k
	 *            a random key
	 */
	@Property
	public void emptyShouldBeBigger(Integer k) {
		assertTrue(new Empty<Integer, String>().bigger(k));
		
		Marks.award(1, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * Tests tree.smaller(k) and checks that is gets the correct answer.
	 *
	 * @param c
	 *            the tree to use
	 * @param k
	 *            a random key
	 * @param delta
	 *            a random integer
	 */
	@Property
	public void smallerTrue(@From(TreeGenerator.class) TreeWrapper c, Integer k, @InRange(min = "1", max = "1000") int delta) {
		Bst<Integer, String> testBst = c.get();

		// If the tree is empty, make sure that it thinks it is smaller than something
		if (Genius.isEmpty(testBst)) assertTrue(testBst.smaller(k));
		else {
			int largestK = Genius.largest(testBst).get().getKey();

			// If the largest key is Integer.MAX_VALUE, then it can't be smaller than anything
			if (largestK == Integer.MAX_VALUE) assertFalse(testBst.smaller(k));
			else {
				int check;

				// Check if tree.smaller(largestK + (1--1000))
				if (largestK + delta < largestK) check = Integer.MAX_VALUE;
				else check = largestK + delta;

				// The tree must be smaller than a key which is bigger than it's biggest key
				assertTrue(testBst.smaller(check));
			}
		}
		
		Marks.award(2, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * Tests that <code>smaller</code> is able to tell when the property is false.
	 *
	 * @param c
	 *            the tree to work on
	 * @param k
	 *            a random key
	 * @param delta
	 *            a random integer
	 */
	@Property
	public void smallerFalse(@From(TreeGenerator.class) TreeWrapper c, Integer k, @InRange(min = "1", max = "1000") int delta) {
		Bst<Integer, String> testBst = c.get();

		// If the tree is empty, make sure that it thinks it is smaller than something
		if (Genius.isEmpty(testBst)) assertTrue(testBst.smaller(k));
		else {
			int largestK = Genius.largest(testBst).get().getKey();

			// If the largest key is Integer.MAX_VALUE, then it can't be smaller than anything
			if (largestK == Integer.MAX_VALUE) assertFalse(testBst.smaller(k));
			else {
				int check;

				// Check if tree.smaller(largestK - (1--1000)) is false
				if (largestK - delta > largestK) check = Integer.MIN_VALUE;
				else check = largestK - delta;

				// The tree cannot be smaller than a key which is smaller than it's biggest key
				assertFalse(testBst.smaller(check));
			}
		}
		
		Marks.award(2, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * Tests tree.bigger(k) and checks that is gets the correct answer.
	 *
	 * @param c
	 *            the tree to use
	 * @param k
	 *            a random key
	 * @param delta
	 *            a random integer
	 */
	@Property
	public void biggerTrue(@From(TreeGenerator.class) TreeWrapper c, Integer k, @InRange(min = "1", max = "1000") int delta) {
		Bst<Integer, String> testBst = c.get();

		// If the tree is empty, make sure that it thinks it is smaller than something
		if (Genius.isEmpty(testBst)) assertTrue(testBst.bigger(k));
		else {
			int smallestK = Genius.smallest(testBst).get().getKey();

			// If the smallest key is Integer.MIN_VALUE, then it can't be bigger than anything
			if (smallestK == Integer.MIN_VALUE) assertFalse(testBst.smaller(k));
			else {
				int check;

				if (smallestK - delta > smallestK) check = Integer.MIN_VALUE;
				else check = smallestK - delta;

				// The tree must be bigger than a key which is smaller than it's smallest key
				assertTrue(testBst.bigger(check));
			}
		}
		
		Marks.award(1, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * Tests that <code>bigger</code> is able to tell when the property is false.
	 *
	 * @param c
	 *            the tree to work on
	 * @param k
	 *            a random key
	 * @param delta
	 *            a random integer
	 */
	@Property
	public void biggerFalse(@From(TreeGenerator.class) TreeWrapper c, Integer k, @InRange(min = "1", max = "1000") int delta) {
		Bst<Integer, String> testBst = c.get();

		// If the tree is empty, make sure that it thinks it is smaller than something
		if (Genius.isEmpty(testBst)) assertTrue(testBst.bigger(k));
		else {
			int smallestK = Genius.smallest(testBst).get().getKey();

			// If the smallest key is Integer.MIN_VALUE, then it can't be bigger than anything
			if (smallestK == Integer.MIN_VALUE) assertFalse(testBst.smaller(k));
			else {
				int check;

				if (smallestK + delta < smallestK) check = Integer.MAX_VALUE;
				else check = smallestK + delta;

				// The tree cannot be bigger than a key which is bigger than it's smallest key
				assertFalse(testBst.bigger(check));
			}
		}
		
		Marks.award(1, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * t.smaller(?) should not change t
	 *
	 * @param c
	 *            the tree to work on
	 * @param randKey
	 *            a random key
	 */
	@Property
	public void smallerShouldBeImmutable(@From(TreeGenerator.class) TreeWrapper c, Integer randKey) {
		testImmutability(c.get(), t -> t.smaller(randKey));
		
		Marks.award(1, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * t.bigger(?) should not change t
	 *
	 * @param c
	 *            the tree to work on
	 * @param randKey
	 *            a random key
	 */
	@Property
	public void biggerShouldBeImmutable(@From(TreeGenerator.class) TreeWrapper c, Integer randKey) {
		testImmutability(c.get(), t -> t.bigger(randKey));
		
		Marks.award(1, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

}
