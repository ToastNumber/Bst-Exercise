import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;

// Add points 1,1,2,1 respectively for each passed property (total 5)

/**
 * Tests the <code>has</code> method in the Bst interface, but is only allowed to use methods before <code>delete()</code>, i.e.:
 * <code>isEmpty(), smaller(), bigger(), has(), find(), put()</code>.
 *
 * @author Kelsey McKenna
 *
 */
public class PropertiesHas extends IsolationTest {

	/**
	 * The empty tree should not have anything
	 *
	 * @param randKey
	 *            a random key
	 */
	@Property
	public void emptyShouldNotHave(Integer randKey) {
		assertFalse(new Empty<Integer, String>().has(randKey));
		Marks.award(1, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * A tree should know when it has an element that is in the tree
	 *
	 * @param c
	 *            the tree to work on
	 * @param randint
	 *            a random key
	 */
	@Property
	public void hasTrue(@From(TreeGenerator.class) TreeWrapper c, int randint) {
		Bst<Integer, String> testBst = c.get();
		int keyToCheck;

		if (testBst.isEmpty()) assertFalse(testBst.has(randint));
		else {
			keyToCheck = gen.randomEntry(testBst).getKey();
			assertEquals(Genius.has(testBst, keyToCheck), testBst.has(keyToCheck));
		}
		
		Marks.award(1, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * A tree should know when it doesn't have
	 *
	 * @param c
	 *            the tree to work on
	 * @param randint
	 *            a random key
	 */
	@Property
	public void hasFalse(@From(TreeGenerator.class) TreeWrapper c, int randint) {
		Bst<Integer, String> testBst = c.get();

		assertEquals(Genius.has(testBst, randint), testBst.has(randint));
		Marks.award(2, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * t.has(?) should not change t
	 * @param c the tree to work on
	 * @param randBool a random boolean
	 * @param randKey a random key
	 */
	@Property
	public void isImmutable(@From(TreeGenerator.class) TreeWrapper c, Boolean randBool, Integer randKey) {
		testImmutability(c.get(), t -> {
			if (Genius.isEmpty(t)) assertFalse(t.has(randKey));
			else {
				Integer k;

				if (randBool) k = gen.randomEntry(t).getKey();
				else k = randKey;

				t.has(k);
			}
		});
		
		Marks.award(1, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

}
