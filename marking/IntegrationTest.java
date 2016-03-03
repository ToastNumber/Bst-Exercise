import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.junit.AfterClass;
import org.junit.runner.RunWith;

import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;

/**
 * Represents a class for testing the interaction between methods.
 *
 * @author Kelsey McKenna
 *
 */
@RunWith(JUnitQuickcheck.class)
public abstract class IntegrationTest {
	public final InputGenerator gen = new InputGenerator();

	public final TreeAssertion isEmptyAssertion = t -> assertEquals(Genius.isEmpty(t), t.isEmpty());
	public final TreeAssertion smallerAssertion = t -> {
		int randKey = gen.generateInt();
		assertEquals(Genius.smaller(t, randKey), t.smaller(randKey));
	};
	public final TreeAssertion biggerAssertion = t -> {
		int randKey = gen.generateInt();
		assertEquals(Genius.bigger(t, randKey), t.bigger(randKey));
	};
	public final TreeAssertion hasAssertion = t -> {
		if (Genius.isEmpty(t)) assertFalse(t.has(gen.generateInt()));
		else {
			MyEntry<Integer, String> entry;

			if (gen.generateBoolean()) entry = gen.randomEntry(t);
			else entry = gen.generateNextEntry();

			t.has(entry.getKey());
		}
	};
	public final TreeAssertion findAssertion = t -> {
		if (Genius.isEmpty(t)) assertFalse(t.find(gen.generateInt()).isPresent());
		else {
			MyEntry<Integer, String> entry;

			if (gen.generateBoolean()) entry = gen.randomEntry(t);
			else entry = gen.generateNextEntry();

			t.find(entry.getKey());
		}
	};
	public final Function<Bst<Integer, String>, Bst<Integer, String>> putAssertion = t -> {
		MyEntry<Integer, String> e = gen.generateNextEntry();

		Bst<Integer, String> gUpdated = Genius.put(t, e.getKey(), e.getValue());
		Bst<Integer, String> tUpdated = t.put(e.getKey(), e.getValue());

		assertArrayEquals(Genius.toArray(gUpdated), Genius.toArray(tUpdated));

		return tUpdated;
	};
	// If trying to delete a non-existent element, it just returns the original tree
	public final Function<Bst<Integer, String>, Bst<Integer, String>> deleteAssertion = t -> {

		if (Genius.isEmpty(t)) return t;
		else {
			Integer k;

			// Try to delete a (probably) non-existent element
			if (gen.generateBoolean()) {
				k = gen.generateInt();

				boolean kInTree = Genius.has(t, k);

				Optional<Bst<Integer, String>> tUpdated = t.delete(k);
				//
				assertEquals(kInTree, tUpdated.isPresent());

				if (tUpdated.isPresent()) return tUpdated.get();
				else return t;
			} else { // try to delete an existent element
				k = gen.randomEntry(t).getKey();
				Bst<Integer, String> gUpdated = Genius.delete(t, k).get();
				Optional<Bst<Integer, String>> tUpdated = t.delete(k);

				// We should have been able to delete the element
				assertTrue(tUpdated.isPresent());

				assertArrayEquals(Genius.toArray(gUpdated), Genius.toArray(tUpdated.get()));

				return tUpdated.get();
			}
		}
	};
	public final TreeAssertion smallestAssertion = t -> {
		Optional<MyEntry<Integer, String>> gSmall = Genius.smallest(t);
		Optional<Entry<Integer, String>> tSmall = t.smallest();

		assertEquals(gSmall.isPresent(), tSmall.isPresent());

		if (gSmall.isPresent()) {
			assertEquals(gSmall.get().getKey(), tSmall.get().getKey());
			assertEquals(gSmall.get().getValue(), tSmall.get().getValue());
		}
	};
	public final TreeAssertion deleteSmallestAssertion = t -> {
		Optional<Bst<Integer, String>> gSmall = Genius.deleteSmallest(t);
		Optional<Bst<Integer, String>> tSmall = t.deleteSmallest();

		assertEquals(gSmall.isPresent(), tSmall.isPresent());

		if (gSmall.isPresent())
			assertArrayEquals(Genius.toArray(gSmall.get()), Genius.toArray(tSmall.get()));
	};
	public final TreeAssertion largestAssertion = t -> {
		Optional<MyEntry<Integer, String>> gLarge = Genius.largest(t);
		Optional<Entry<Integer, String>> tLarge = t.largest();

		assertEquals(gLarge.isPresent(), tLarge.isPresent());

		if (gLarge.isPresent()) {
			assertEquals(gLarge.get().getKey(), tLarge.get().getKey());
			assertEquals(gLarge.get().getValue(), tLarge.get().getValue());
		}
	};
	public final TreeAssertion deleteLargestAssertion = t -> {
		Optional<Bst<Integer, String>> gLarge = Genius.deleteLargest(t);
		Optional<Bst<Integer, String>> tLarge = t.deleteLargest();

		assertEquals(gLarge.isPresent(), tLarge.isPresent());

		if (gLarge.isPresent())
			assertArrayEquals(Genius.toArray(gLarge.get()), Genius.toArray(tLarge.get()));
	};
	public final TreeAssertion sizeAssertion = t -> assertEquals(Genius.size(t), t.size());
	public final TreeAssertion heightAssertion = t -> assertEquals(Genius.height(t), t.height());
	public final TreeAssertion saveInOrderAssertion = t -> {
		int size = Genius.size(t);

		MyEntry<Integer, String>[] entries = Genius.toArray(t);

		@SuppressWarnings("unchecked")
		Entry<Integer, String>[] a = (Entry<Integer, String>[]) Array.newInstance(Entry.class, size);
		t.saveInOrder(a);

		for (int i = 0; i < entries.length; ++i) {
			assertEquals(entries[i].getKey(), a[i].getKey());
			assertEquals(entries[i].getValue(), a[i].getValue());
		}
	};
	public final Function<Bst<Integer,String>, Bst<Integer,String>> balancedAssertion = t -> {
		Bst<Integer, String> correctBal = Genius.balanced(t);
		Bst<Integer, String> testBal = t.balanced();

		// Make sure they have the same elements
		assertArrayEquals(Genius.toArray(correctBal), Genius.toArray(testBal));

		return testBal;
	};

	/**
	 * Tests that the given methods interact together by running
	 * the methods using assertions
	 *
	 * @param bst
	 *            the bst to work on
	 * @param numOperations
	 *            the number of (randomly chosen) operations to perform
	 * @param assertions
	 *            methods that should not change the tree, e.g. <code>size</code>
	 * @param functions
	 *            methods that should change the tree, e.g. <code>put</code>
	 */
	public void testMethodsWorkTogether(Bst<Integer, String> bst, int numOperations, List<TreeAssertion> assertions, List<Function<Bst<Integer, String>, Bst<Integer, String>>> functions) {

		final int numFunctions = functions.size();
		final int totalMethods = assertions.size() + numFunctions;

		for (int i = 0; i < numOperations; ++i) {
			int index = gen.generateInt(totalMethods);
			if (index < numFunctions) bst = functions.get(index).apply(bst);
			else assertions.get(index - numFunctions).run(bst);
		}

		Marks.award(1, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	@AfterClass
	public static void printResults() {
		System.out.println(Marks.getSummary());
	}
}
