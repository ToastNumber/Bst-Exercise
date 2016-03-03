import static org.junit.Assert.assertArrayEquals;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;

// Add points 2,9,2 respectively for each passed property (total 13)

public class PropertiesPut extends IsolationTest {

	/**
	 * Putting one element in the empty tree should produce the correct result
	 *
	 * @param k
	 *            the key to insert
	 * @param v
	 *            the value to insert
	 */
	@Property 
	public void putOneInEmptyTreeShouldHaveCorrectEntry(Integer k, String v) {
		Bst<Integer, String> testBst = new Empty<>();
		Bst<Integer, String> geniusBst = new Empty<>();

		testBst = testBst.put(k, v);
		geniusBst = geniusBst.put(k, v);

		assertArrayEquals(Genius.toArray(geniusBst), Genius.toArray(testBst));
		
		Marks.award(2, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * Put a bunch of entries in a random tree and make sure the tree is in a correct state
	 *
	 * @param c
	 *            the tree to work on
	 * @param n
	 *            the number of items to insert
	 */
	@Property
	public void putManyThenShouldHaveCorrectEntries(@From(TreeGenerator.class) TreeWrapper c, @InRange(min = "1", max = "10") Integer n) {
		Bst<Integer, String> testBst = c.get();
		Bst<Integer, String> geniusTree = testBst;

		for (int i = 0; i < n; ++i) {
			MyEntry<Integer, String> entry = gen.generateNextEntry();
			testBst = testBst.put(entry.getKey(), entry.getValue());
			geniusTree = geniusTree.put(entry.getKey(), entry.getValue());
		}

		assertArrayEquals(Genius.toArray(geniusTree), Genius.toArray(testBst));
		
		Marks.award(9, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * t.put(?,?) should not change t
	 *
	 * @param c
	 *            the tree to work on
	 * @param randKey
	 *            a random key
	 * @param randValue
	 *            a random value
	 */
	@Property
	public void shouldBeImmutable(@From(TreeGenerator.class) TreeWrapper c, Integer randKey, String randValue) {
		testImmutability(c.get(), t -> t.put(randKey, randValue));
		
		Marks.award(2, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

}
