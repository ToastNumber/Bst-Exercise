import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;

// Add points 2,4,4,2 respectively for each passed property (total 12)

/**
 * Test
 *
 * @author Kelsey McKenna
 *
 */
public class PropertiesFind extends IsolationTest {

	/**
	 * empty.find(?) should be Optional.empty()
	 *
	 * @param k
	 *            a random key
	 */
	@Property
	public void emptyShouldNotFind(Integer k) {
		assertFalse(new Empty<Integer, String>().find(k).isPresent());
		Marks.award(2, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * Check that t.find(k) returns the correct answer.
	 *
	 * @param c
	 *            the tree to work on
	 * @param k
	 *            a random key
	 */
	@Property
	public void findTrue(@From(TreeGenerator.class) TreeWrapper c, Integer k) {
		Bst<Integer, String> testBst = c.get();

		if (Genius.isEmpty(testBst)) {
			assertFalse(testBst.find(k).isPresent());
		} else {
			Integer key = gen.randomEntry(testBst).getKey();

			Optional<String> gFind = Genius.find(testBst, key);
			Optional<String> tFind = testBst.find(key);

			assertTrue(tFind.isPresent());
			assertEquals(gFind.isPresent(), tFind.isPresent());
		}
		
		Marks.award(4, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * t.find(?) should return the correct result when looking for a random key,
	 * which will probably not be in the tree
	 *
	 * @param c
	 *            the tree to work on
	 * @param k
	 *            a random key
	 */
	@Property
	public void findFalse(@From(TreeGenerator.class) TreeWrapper c, Integer k) {
		Bst<Integer, String> testBst = c.get();

		if (Genius.isEmpty(testBst)) {
			assertFalse(testBst.find(k).isPresent());
		} else {
			Optional<String> gFind = Genius.find(testBst, k);
			Optional<String> tFind = testBst.find(k);

			assertEquals(gFind.isPresent(), tFind.isPresent());

			if (gFind.isPresent()) assertEquals(gFind.get(), tFind.get());
		}
	
		Marks.award(4, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	@Property
	public void shouldBeImmutable(@From(TreeGenerator.class) TreeWrapper c, Boolean randBool, Integer randKey) {
		testImmutability(c.get(), t -> {
			if (Genius.isEmpty(t)) assertFalse(t.find(randKey).isPresent());
			else {
				Integer k;

				if (randBool) k = gen.randomEntry(t).getKey();
				else k = randKey;

				t.find(k);
			}
		});
		
		Marks.award(2, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}
}
