import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import java.util.Optional;

import org.junit.Ignore;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;

// Add points 2,6,2 respectively for each passed property (total 10)

public class PropertiesDelete extends IsolationTest {

	@Property
	public void cannotDeleteFromEmpty(Integer randKey) {
		Bst<Integer, String> testBst = new Empty<>();
		assertFalse(testBst.delete(randKey).isPresent());
		Marks.award(2, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * The should return the correct result after deleting an entry
	 * @param c the tree to use
	 * @param randKey a random key to use
	 */
	@Property
	public void deleteRandomEntryShouldHaveCorrectEntries(@From(TreeGenerator.class) TreeWrapper c, Integer randKey) {
		Bst<Integer, String> testBst = c.get();

		if (Genius.isEmpty(testBst)) assertFalse(testBst.delete(randKey).isPresent());
		else {
			MyEntry<Integer, String> entryToDelete = gen.randomEntry(testBst);
			Optional<Bst<Integer, String>> gUpdated = Genius.delete(testBst, entryToDelete.getKey());
			Optional<Bst<Integer, String>> tUpdated = testBst.delete(entryToDelete.getKey());

			// They should both either be Optional.of(...) or Optional.empty()
			assertEquals(gUpdated.isPresent(), tUpdated.isPresent());

			if (gUpdated.isPresent()) {
				// Check that they are both Fork or both Empty
				assertEquals(Genius.isEmpty(gUpdated.get()), Genius.isEmpty(tUpdated.get()));

				// If they are both Forks, then check they have the same entries
				if (!Genius.isEmpty(gUpdated.get())) assertArrayEquals(Genius.toArray(gUpdated.get()), Genius.toArray(tUpdated.get()));
			}
		}
		
		Marks.award(6, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	@Property
	public void shouldBeImmutable(@From(TreeGenerator.class) TreeWrapper c, Boolean randBool, Integer randKey) {
		testImmutability(c.get(), t -> {
			if (Genius.isEmpty(t)) t.delete(randKey);
			else {
				int k;

				if (randBool) k = gen.randomEntry(t).getKey();
				else k = randKey;

				t.delete(k);
			}
		});
		
		Marks.award(2, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}


}
