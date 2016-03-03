import static org.junit.Assert.assertEquals;

import java.lang.reflect.Array;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;

// Add points 1,8,1 respectively for each passed property (total 10)

public class PropertiesSaveInOrder extends IsolationTest {

	/**
	 * empty.saveInOrder(a) shouldn't change a
	 *
	 * @param delta
	 *            a random size for the array a
	 */
	@Property
	public void emptyShouldDoNothing(@InRange(min = "1", max = "10") int delta) {
		@SuppressWarnings("unchecked")
		Entry<Integer, String>[] a = (Entry<Integer, String>[]) Array.newInstance(Entry.class, delta);

		for (int i = 0; i < a.length; ++i)
			a[i] = null;

		new Empty<Integer, String>().saveInOrder(a);

		for (int i = 0; i < a.length; ++i)
			assertEquals(a[i], null);
			
		Marks.award(1, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());	
	}

	/**
	 * saveInOrder should work for a random tree
	 *
	 * @param c
	 *            the tree to work on
	 */
	@Property
	public void saveInOrderForRandomTree(@From(TreeGenerator.class) TreeWrapper c) {
		Bst<Integer, String> bst = c.get();

		int size = Genius.size(bst);

		@SuppressWarnings("unchecked")
		Entry<Integer, String>[] a = (Entry<Integer, String>[]) Array.newInstance(Entry.class, size);
		bst.saveInOrder(a);

		MyEntry<Integer, String>[] b = Genius.toArray(bst);

		for (int i = 0; i < size; ++i) {
			assertEquals(b[i].getKey(), a[i].getKey());
			assertEquals(b[i].getValue(), a[i].getValue());
		}
		
		Marks.award(8, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * t.saveInOrder(?) should not change t
	 *
	 * @param c
	 *            the tree to work on
	 */
	@SuppressWarnings("unchecked")
	@Property
	public void shouldBeImmutable(@From(TreeGenerator.class) TreeWrapper c) {
		testImmutability(c.get(), t -> {
			Entry<Integer, String>[] tEntries = (Entry<Integer, String>[]) Array.newInstance(Entry.class, Genius.size(t));
			t.saveInOrder(tEntries);
		});
		
		Marks.award(1, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

}
