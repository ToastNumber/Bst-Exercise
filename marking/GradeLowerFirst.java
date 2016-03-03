import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;

// Award 5 marks to storeBstTest and 6 marks to overwriteKey (total 11)

/**
 * Integration tests for BstTable
 *
 * @author Auke
 */
public final class GradeLowerFirst extends IsolationTest {

	private Table<Integer, String> treeToTable(Bst<Integer, String> bst) {
		MyEntry<Integer, String>[] entries = Genius.toArray(bst);

		Table<Integer, String> table = new BstTable<Integer, String>();

		for (int i = 0; i < entries.length; i++) {
			table = table.put(entries[i].getKey(), entries[i].getValue());
		}

		return table;
	}

	@Property
	public void storeBstTest(@From(TreeGenerator.class) TreeWrapper c) {
		Bst<Integer, String> testBst = c.get();
		Table<Integer, String> table = treeToTable(testBst);
		MyEntry<Integer, String>[] entries = Genius.toArray(testBst);

		assertEquals(table.size(), Genius.size(testBst));
		assertEquals(table.isEmpty(), Genius.isEmpty(testBst));

		for (int i = 0; i < entries.length; i++) {
			Integer k = entries[i].getKey();
			assertTrue(table.containsKey(k));
			assertTrue(table.get(k).isPresent());
			assertEquals(table.get(k).get(), entries[i].getValue());
			Optional<Table<Integer, String>> updated = table.remove(k);
			assertTrue(updated.isPresent());
			assertFalse(updated.get().containsKey(k));
			assertEquals(updated.get().size(), entries.length - 1);
		}

		Collection<Integer> keys = table.keys();
		Collection<String> values = table.values();
		Collection<Integer> keysIn = new HashSet<Integer>();
		Collection<String> valuesIn = new HashSet<String>();

		for (int i = 0; i < entries.length; i++) {
			keysIn.add(entries[i].getKey());
			valuesIn.add(entries[i].getValue());
		}
		assertEquals(new HashSet<Integer>(keys), keysIn);
		assertEquals(new HashSet<String>(values), valuesIn);

		Marks.award(5, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	@Property
	public void overwriteKey(@From(TreeGenerator.class) TreeWrapper c, Integer randint, String v) {
		Bst<Integer, String> testBst = c.get();
		Table<Integer, String> table = treeToTable(testBst);
		Integer k;

		if (testBst.isEmpty()) k = randint;
		else {
			MyEntry<Integer, String> entry;
			entry = gen.randomEntry(testBst);
			k = entry.getKey();
		}

		Table<Integer, String> updated = table.put(k, v);

		if (testBst.isEmpty()) assertEquals(updated.size(), 1);
		else assertEquals(updated.size(), Genius.size(testBst));
		assertTrue(updated.containsKey(k));
		assertTrue(updated.get(k).isPresent());
		assertEquals(updated.get(k).get(), v);

		assertTrue(updated.remove(k).isPresent());
		updated = updated.remove(k).get();

		assertEquals(updated.size(), Math.max(0, Genius.size(testBst) - 1));

		Marks.award(6, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}
}

/*
 * boolean containsKey(Key v); // Self-explanatory.
 * Optional<Value> get(Key k); // Returns the value of a key, if it exists.
 * boolean isEmpty(); // Self-explanatory.
 * Table<Key,Value> put(Key k, Value v); // Table with added or replaced entry.
 * Optional<Table<Key,Value>> remove(Key k); // Removes the entry with given key, if present.
 * int size(); // Number of entries in the table.
 * Collection<Value> values(); // The collection of values in the table.
 * Collection<Key> keys(); // The set of keys in the table.
 */
