package solution;

import java.lang.reflect.Array;
import java.util.Optional;

public class Fork<Key extends Comparable<Key>, Value> implements Bst<Key, Value> {

	private final Key key;
	private final Value value;
	private final Bst<Key, Value> left, right;

	public Fork(Key key, Value value, Bst<Key, Value> left, Bst<Key, Value> right) {
		assert (left != null); // Refuse to work with null pointers.
		assert (right != null);

		assert (left.smaller(key)); // Refuse to violate the bst property.
		assert (right.bigger(key)); // So all our objects will really be bst's.

		this.key = key; // The usual stuff now.
		this.value = value;
		this.left = left;
		this.right = right;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean smaller(Key k) { // Checks whether all nodes smaller than k.
		return key.compareTo(k) < 0 && right.smaller(k);
	}

	@Override
	public boolean bigger(Key k) {
		return key.compareTo(k) > 0 && left.bigger(k);
	}

	@Override
	public boolean has(Key k) { // Checks whether k occurs in "this".
		return k.compareTo(key) == 0 || (k.compareTo(key) < 0 && left.has(k)) || right.has(k);

		// Equivalently:
		/*
		 * if (k.compareTo(key) == 0)
		 * return true;
		 * else
		 * if (k.compareTo(key) < 0) // Only one sub-tree needs to be searched.
		 * return left.has(key);
		 * else
		 * return right.has(key);
		 */
	}

	@Override
	public Optional<Value> find(Key k) {
		if (k.compareTo(key) == 0) return Optional.of(value);
		else if (k.compareTo(key) < 0) return left.find(k);
		else return right.find(k);
	}

	@Override
	public Bst<Key, Value> put(Key k, Value v) { // Returns a copy of this with k,v inserted or replaced.
		if (k.compareTo(key) == 0) return new Fork<>(k, v, left, right); // Replaced.
		else if (k.compareTo(key) < 0) return new Fork<>(key, value, left.put(k, v), right);
		else return new Fork<>(key, value, left, right.put(k, v));
	}

	@Override
	public Optional<Entry<Key, Value>> smallest() {
		if (left.isEmpty()) return Optional.of(new Entry<>(key, value));
		else return left.smallest();
	}

	@Override
	public Optional<Bst<Key, Value>> deleteSmallest() {
		if (left.isEmpty()) return Optional.of(right); // Smallest entry at is at root.
		else { // Smallest entry is at the left subtree.
			Optional<Bst<Key, Value>> l = left.deleteSmallest();
			assert (l.isPresent());
			return Optional.of(new Fork<>(key, value, l.get(), right));
		}
	}

	@Override
	public Optional<Entry<Key, Value>> largest() {
		if (right.isEmpty()) return Optional.of(new Entry<>(key, value));
		else return right.largest();
	}

	@Override
	public Optional<Bst<Key, Value>> deleteLargest() {
		if (right.isEmpty()) return Optional.of(left);
		else {
			Optional<Bst<Key, Value>> r = right.deleteLargest();
			assert (r.isPresent());
			return Optional.of(new Fork<>(key, value, left, r.get()));
		}
	}

	@Override
	public Optional<Bst<Key, Value>> delete(Key k) { // Returns a copy of self with e deleted.
		if (k.compareTo(key) == 0) {
			if (left.isEmpty()) return Optional.of(right);
			else if (right.isEmpty()) return Optional.of(left);
			else {
				// Get largest entry of left subtree.
				Optional<Entry<Key, Value>> ole = left.largest();
				// It must exist.
				assert (ole.isPresent());
				Entry<Key, Value> le = ole.get();
				// Now get subtree with largest deleted.
				Optional<Bst<Key, Value>> sld = left.deleteLargest();
				// It must exist.
				assert (sld.isPresent());
				Bst<Key, Value> newleft = sld.get();
				return Optional.of(new Fork<>(le.getKey(), le.getValue(), newleft, right));
			}
		} else if (k.compareTo(key) < 0) { // We have to delete from one of the subtrees.
			Optional<Bst<Key, Value>> l = left.delete(k);
			if (l.isPresent()) return Optional.of(new Fork<>(key, value, l.get(), right));
			else return Optional.empty();
		} else {
			Optional<Bst<Key, Value>> r = right.delete(k);
			if (r.isPresent()) return Optional.of(new Fork<>(key, value, left, r.get()));
			else return Optional.empty();
		}
	}

	// @Override
	// public String toString() {
	// return "Fork(" + key + "," + value + "," + left.toString() + "," + right.toString() + ")";
	// }

	@Override
	public String fancyToString() {
		return "\n\n" + fancyToString(0) + "\n\n";
	}

	@Override
	public String fancyToString(int depth) {
		int step = 4; // depth step
		String l = left.fancyToString(depth + step);
		String r = right.fancyToString(depth + step);
		return r + spaces(depth) + key + "," + value + "\n" + l;
	}

	private String spaces(int n) { // Helper method for the above:
		String s = "";
		for (int i = 0; i < n; i++)
			s = s + " ";
		return s;
	}

	@Override
	public int size() {
		return 1 + left.size() + right.size();
	}

	@Override
	public int height() {
		return 1 + Math.max(left.height(), right.height());
	}

	@Override
	public void printInOrder() {
		left.printInOrder();
		System.out.print(value + " ");
		right.printInOrder();
	}

	@Override
	public void saveInOrder(Entry<Key, Value> a[]) {
		// Saves the tree in the array a.
		// Returns the next available position in the array.
		saveInOrder(a, 0);
	}

	@Override
	public int saveInOrder(Entry<Key, Value> a[], int i) {
		// Saves in the array a starting from position i.
		// Returns the next available position in the array.

		int j = left.saveInOrder(a, i);
		a[j] = new Entry<>(key, value);
		int k = right.saveInOrder(a, j + 1);
		return k;
	}

	// See README for an explanation of the following:
	@Override
	public Bst<Key, Value> balanced() {
		// Entry<Key,Value>[] a = null;
		// @SuppressWarnings("unchecked")
		// a = (Entry<Key,Value>[]) Array.newInstance(a.getClass().getComponentType(), size());

		Entry<Key, Value> e = new Entry<>(key, value);
		@SuppressWarnings("unchecked")
		Entry<Key, Value>[] a = (Entry<Key, Value>[]) Array.newInstance(e.getClass(), size());

		this.saveInOrder(a);

		return balanced(a, 0, a.length);
	}

	private Bst<Key, Value> balanced(Entry<Key, Value>[] a, int start, int end) {
		assert (start <= end);

		if (start == end) return new Empty<>();

		assert (start < end);
		int middle = start + (end - start) / 2; // We avoid overflow this way.
		assert (start <= middle && middle < end);
		return new Fork<Key, Value>(a[middle].getKey(), a[middle].getValue(), balanced(a, start, middle), balanced(a, middle + 1, end));
	}

	@Override
	public String toString() {
		return fancyToString();
	}

	@Override
	public Optional<Key> getKey() {
		return Optional.of(key);
	}

	@Override
	public Optional<Value> getValue() {
		return Optional.of(value);
	}

	@Override
	public Optional<Bst<Key, Value>> getLeft() {
		return Optional.of(left);
	}

	@Override
	public Optional<Bst<Key, Value>> getRight() {
		return Optional.of(right);
	}
}
