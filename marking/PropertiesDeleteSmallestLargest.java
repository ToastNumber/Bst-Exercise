import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;

// Add points 0.5,0.3, 1.0,0.4, 0.5,0.3 respectively for each passed property (total 3)

public class PropertiesDeleteSmallestLargest extends IsolationTest {

	/**
	 * The result of empty.deleteSmallest() should be Optional.empty
	 */
	@Property
	public void cannotDeleteSmallestFromEmpty() {
		assertFalse(new Empty<Integer, String>().deleteSmallest().isPresent());
		Marks.award(0.5, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * The result of empty.deleteLargest() should be Optional.empty
	 */
	@Property
	public void cannotDeleteLargestFromEmpty() {
		assertFalse(new Empty<Integer, String>().deleteLargest().isPresent());
		Marks.award(0.3, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * deleteSmallest should return the correct tree.
	 *
	 * @param c
	 *            the tree to work on.
	 */
	@Property
	public void deleteSmallestShouldBeCorrect(@From(TreeGenerator.class) TreeWrapper c) {
		Bst<Integer, String> bst = c.get();

		// This is already checked in previous test
		if (!Genius.isEmpty(bst)) {
			MyEntry<Integer, String>[] original = Genius.toArray(bst);

			Optional<Bst<Integer, String>> updated = bst.deleteSmallest();

			assertTrue(updated.isPresent());
			bst = updated.get();

			MyEntry<Integer, String>[] newElements = Genius.toArray(bst);
			// Make sure that only one element has been deleted
			assertEquals(original.length - 1, newElements.length);

			// Check that elements 1--n of the original tree are equal
			// to 0--(n-1) of the new tree
			for (int i = 0; i < newElements.length; ++i) {
				assertEquals(original[i + 1], newElements[i]);
			}
		}

		Marks.award(1, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * deleteLargest should return the correct tree.
	 *
	 * @param c
	 *            the tree to work on.
	 */
	@Property
	public void deleteLargestShouldBeCorrect(@From(TreeGenerator.class) TreeWrapper c) {
		Bst<Integer, String> bst = c.get();

		if (!Genius.isEmpty(bst)) {
			MyEntry<Integer, String>[] original = Genius.toArray(bst);

			Optional<Bst<Integer, String>> updated = bst.deleteLargest();

			assertTrue(updated.isPresent());
			bst = updated.get();

			MyEntry<Integer, String>[] newElements = Genius.toArray(bst);
			// Make sure that only one element has been deleted
			assertEquals(original.length - 1, newElements.length);

			// Check that elements 0--(n-1) of the original tree are equal
			// to 0--(n-1) of the new tree
			for (int i = 0; i < newElements.length; ++i) {
				assertEquals(original[i], newElements[i]);
			}
		}

		Marks.award(0.4, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * t.deleteSmallest() should not change t.
	 *
	 * @param c
	 *            the tree to work on.
	 */
	@Property
	public void deleteSmallestShouldBeImmutable(@From(TreeGenerator.class) TreeWrapper c) {
		testImmutability(c.get(), t -> t.deleteSmallest());

		Marks.award(0.5, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * t.deleteLargest() should not change t.
	 *
	 * @param c
	 *            the tree to work on.
	 */
	@Property
	public void deleteLargestShouldBeImmutable(@From(TreeGenerator.class) TreeWrapper c) {
		testImmutability(c.get(), t -> t.deleteLargest());

		Marks.award(0.3, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}
}
