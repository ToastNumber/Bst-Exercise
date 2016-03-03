import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Optional;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;

// Award 0.5,0.3, 1.0,0.4, 0.5,0.3 respectively (total 3)

public class PropertiesSmallestLargest extends IsolationTest {

	/**
	 * empty.smallest() should be Optional.empty()
	 */
	@Property
	public void emptyShouldHaveNoSmallestElement() {
		assertFalse(new Empty<Integer, String>().smallest().isPresent());

		Marks.award(0.5, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * empty.largest() should be Optional.empty()
	 */
	@Property
	public void emptyShouldHaveNoLargestElement() {
		assertFalse(new Empty<Integer, String>().largest().isPresent());

		Marks.award(0.3, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * Checks that t.smallest() always returns the correct answer
	 *
	 * @param c
	 *            the tree to work on
	 */
	@Property
	public void smallestIsAsGoodAsGenius(@From(TreeGenerator.class) TreeWrapper c) {
		Bst<Integer, String> testBst = c.get();

		Optional<MyEntry<Integer, String>> gSmallest = Genius.smallest(testBst);
		Optional<Entry<Integer, String>> tSmallest = testBst.smallest();

		assertEquals(gSmallest.isPresent(), tSmallest.isPresent());

		if (gSmallest.isPresent()) {
			assertEquals(gSmallest.get().getKey(), tSmallest.get().getKey());
			assertEquals(gSmallest.get().getValue(), tSmallest.get().getValue());
		}

		Marks.award(1, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * Checks that t.largest() always returns the correct answer
	 *
	 * @param c
	 *            the tree to work on
	 */
	@Property
	public void largestIsAsGoodAsGenius(@From(TreeGenerator.class) TreeWrapper c) {
		Bst<Integer, String> testBst = c.get();

		Optional<MyEntry<Integer, String>> gLargest = Genius.largest(testBst);
		Optional<Entry<Integer, String>> tLargest = testBst.largest();

		assertEquals(gLargest.isPresent(), tLargest.isPresent());

		if (gLargest.isPresent()) {
			assertEquals(gLargest.get().getKey(), tLargest.get().getKey());
			assertEquals(gLargest.get().getValue(), tLargest.get().getValue());
		}

		Marks.award(0.4, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * t.smallest() should not change t
	 *
	 * @param c
	 *            the tree to work on
	 */
	@Property
	public void smallestShouldBeImmutable(@From(TreeGenerator.class) TreeWrapper c) {
		testImmutability(c.get(), t -> t.smallest());

		Marks.award(0.5, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * t.largest() should not change t
	 *
	 * @param c
	 *            the tree to work on
	 */
	@Property
	public void largestShouldBeImmutable(@From(TreeGenerator.class) TreeWrapper c) {
		testImmutability(c.get(), t -> t.largest());

		Marks.award(0.3, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

}
