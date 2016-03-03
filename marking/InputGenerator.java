
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * Randomly built binary trees, Introduction to Algorithms 3rd edition
 * by Charles E. Leiserson, Clifford Stein, Ronald Rivest, and Thomas H. Cormen, p. 299
 *
 * @author Ermano Arruda
 */
public class InputGenerator {

	private static final int DEFAULT_STRING_SIZE = 10;

	private ArrayList<MyEntry<Integer, String>> buffer;
	private int next;
	private Random rand;

	/**
	 * Constructs a default InputGenerator
	 *
	 */
	public InputGenerator() {

		this.buffer = new ArrayList<MyEntry<Integer, String>>();
		next = 0;
		rand = new Random();

	}

	/**
	 * Constructs an InputGenerator with a specified random seed
	 *
	 * @param seed
	 *            random seed for pseudo random generation
	 */
	public InputGenerator(long seed) {
		this();
		rand.setSeed(seed);
	}

	/**
	 * Gets the size of the entry buffer
	 *
	 * @return size of the entry buffer
	 */
	public int getSize() {
		return buffer.size();
	}

	/**
	 * Generates a random character which can be either upper case or lowercase in the range [a-zA-Z].
	 *
	 * @return random character
	 */
	public char generateChar() {

		char c = 'A';

		boolean upperCase = rand.nextFloat() >= 0.5;

		if (upperCase) {
			c = (char) (65 + rand.nextInt(26));
		} else {
			c = (char) (97 + rand.nextInt(26));
		}

		return c;

	}

	/**
	 * Generates a random string with specified size
	 *
	 * @param n
	 *            number of characters of the string
	 * @return the generated random string with size n
	 */
	public String generateString(int n) {
		String str = null;

		StringBuilder strBuilder = new StringBuilder();

		for (int i = 0; i < n; i++) {

			strBuilder.append(generateChar());
		}

		str = strBuilder.toString();

		return str;

	}

	/**
	 * @return <code>generateString(DEFAULT_STRING_SIZE)</code>
	 */
	public String generateString() {
		return generateString(DEFAULT_STRING_SIZE);
	}

	/**
	 * Generates a random integer
	 *
	 * @return random integer
	 */
	public Integer generateInt() {
		return rand.nextInt();
	}

	public Integer generateInt(int bound) {
		return rand.nextInt(bound);
	}

	public Boolean generateBoolean() {
		return rand.nextBoolean();
	}

	/**
	 * Generates a random entry with a random Integer key and a random String value
	 *
	 * @return returns a random entry
	 */
	public MyEntry<Integer, String> generateNextEntry() {

		MyEntry<Integer, String> entry = null;

		Integer key = generateInt();
		String value = generateString(DEFAULT_STRING_SIZE);

		entry = new MyEntry<Integer, String>(key, value);

		buffer.add(entry);

		return buffer.get(next++);

	}

	/**
	 * Generates an array of entries
	 *
	 * @param n
	 *            number of entries to be generated
	 * @return the generated entry list
	 */
	public MyEntry<Integer, String>[] generateEntries(int n) {
		@SuppressWarnings("unchecked")
		MyEntry<Integer, String>[] entries = (MyEntry<Integer, String>[]) Array.newInstance(MyEntry.class, n);

		for (int i = 0; i < n; i++) {
			entries[i] = generateNextEntry();
		}

		return entries;
	}

	/**
	 * Generates an array of entries sorted by key in ascending order
	 *
	 * @param n
	 *            number of entries to be generated
	 * @return the generated sorted entry list
	 */
	public MyEntry<Integer, String>[] generateSortedEntries(int n) {
		MyEntry<Integer, String>[] entries = generateEntries(n);

		Arrays.sort(entries, (MyEntry<Integer, String> e1, MyEntry<Integer, String> e2) -> e1.getKey().compareTo(e2.getKey()));

		return entries;

	}

	// TODO implement as described here: http://stackoverflow.com/a/25942329
	public <Key extends Comparable<Key>, Value> MyEntry<Key, Value> randomEntry(Bst<Key, Value> bst) {
		MyEntry<Key, Value>[] a = Genius.toArray(bst);
		return a[rand.nextInt(a.length)];
	}

	/**
	 * @param bst
	 *            the tree to look at
	 * @return an entry with a key greater than or equal to the largest key in bst
	 */
	public MyEntry<Integer, String> generateEntryAtLeastAsLarge(Bst<Integer, String> bst) {
		if (Genius.isEmpty(bst)) {
			return generateNextEntry();
		} else {
			MyEntry<Integer, String> largest = Genius.largest(bst).get();

			String v = generateString();

			if (largest.getKey() == Integer.MAX_VALUE) {
				return new MyEntry<>(Integer.MAX_VALUE, v);
			} else {
				return new MyEntry<>(largest.getKey() + generateInt(Math.abs(Integer.MAX_VALUE - largest.getKey())), v);
			}
		}
	}

	/**
	 * @param bst
	 *            the tree to look at
	 * @return an entry with a key smaller than or equal to the smallest key in bst
	 */
	public MyEntry<Integer, String> generateEntryAtLeastAsSmall(Bst<Integer, String> bst) {
		if (Genius.isEmpty(bst)) {
			return generateNextEntry();
		} else {
			MyEntry<Integer, String> smallest = Genius.smallest(bst).get();

			String v = generateString();

			if (smallest.getKey() == Integer.MIN_VALUE) {
				return new MyEntry<>(Integer.MIN_VALUE, v);
			} else {
				return new MyEntry<>(smallest.getKey() - generateInt(Math.abs(smallest.getKey() - Integer.MIN_VALUE)), v);
			}
		}
	}

}