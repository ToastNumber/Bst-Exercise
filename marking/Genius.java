import java.lang.reflect.Array;
import java.util.Optional;

/**
 * Provides methods of performing correct operations on a student's Bst implementation,
 * and some other utility functions.
 *
 * @author Kelsey McKenna
 *
 */
public class Genius {

	public static <Key extends Comparable<Key>, Value> boolean isEmpty(Bst<Key, Value> bst) {
		return bst.getClass() == Empty.class;
	}

	public static <Key extends Comparable<Key>, Value> boolean smaller(Bst<Key, Value> bst, Key k) {
		if (isEmpty(bst)) return true;
		else {
			Key key = bst.getKey().get(); // We know the key exists because this is a fork
			return key.compareTo(k) < 0 && smaller(bst.getRight().get(), k);
		}
	}

	public static <Key extends Comparable<Key>, Value> boolean bigger(Bst<Key, Value> bst, Key k) {
		if (isEmpty(bst)) return true;
		else {
			Key key = bst.getKey().get(); // We know the key exists because this is a fork
			return key.compareTo(k) > 0 && bigger(bst.getLeft().get(), k);
		}
	}

	public static <Key extends Comparable<Key>, Value> boolean has(Bst<Key, Value> bst, Key k) {
		if (isEmpty(bst)) return false;
		else {
			Key key = bst.getKey().get();

			return k.compareTo(key) == 0 || (k.compareTo(key) < 0 && has(bst.getLeft().get(), k) || has(bst.getRight().get(), k));
		}
	}

	public static <Key extends Comparable<Key>, Value> Optional<Value> find(Bst<Key, Value> bst, Key k) {
		if (isEmpty(bst)) return Optional.empty();
		else {
			Key key = bst.getKey().get();

			if (k.compareTo(key) == 0) return Optional.of(bst.getValue().get());
			else if (k.compareTo(key) < 0) return find(bst.getLeft().get(), k);
			else return find(bst.getRight().get(), k);
		}
	}

	public static <Key extends Comparable<Key>, Value> Bst<Key, Value> put(Bst<Key, Value> bst, Key k, Value v) {
		if (isEmpty(bst)) {
			return new Fork<Key, Value>(k, v, new Empty<>(), new Empty<>());
		} else {
			Key key = bst.getKey().get();
			Value value = bst.getValue().get();
			Bst<Key, Value> left = bst.getLeft().get();
			Bst<Key, Value> right = bst.getRight().get();

			if (k.compareTo(key) == 0) return new Fork<>(k, v, left, right); // Replaced.
			else if (k.compareTo(key) < 0) return new Fork<>(key, value, put(left, k, v), right);
			else return new Fork<>(key, value, left, put(right, k, v));
		}
	}

	public static <Key extends Comparable<Key>, Value> Optional<MyEntry<Key, Value>> smallest(Bst<Key, Value> bst) {
		if (isEmpty(bst)) return Optional.empty();
		else {
			Bst<Key, Value> left = bst.getLeft().get();

			if (isEmpty(left)) return Optional.of(new MyEntry<>(bst.getKey().get(), bst.getValue().get()));
			else return smallest(left);
		}
	}

	public static <Key extends Comparable<Key>, Value> Optional<Bst<Key, Value>> deleteSmallest(Bst<Key, Value> bst) {
		if (isEmpty(bst)) return Optional.empty();
		else {
			Bst<Key, Value> left = bst.getLeft().get();
			Bst<Key, Value> right = bst.getRight().get();

			if (isEmpty(left)) return Optional.of(right); // Smallest entry at is at root.
			else { // Smallest entry is at the left subtree.
				Optional<Bst<Key, Value>> l = deleteSmallest(left);
				assert (l.isPresent());
				return Optional.of(new Fork<>(bst.getKey().get(), bst.getValue().get(), l.get(), right));
			}
		}
	}

	public static <Key extends Comparable<Key>, Value> Optional<MyEntry<Key, Value>> largest(Bst<Key, Value> bst) {
		if (isEmpty(bst)) return Optional.empty();
		else {
			Bst<Key, Value> right = bst.getRight().get();
			if (right.isEmpty()) return Optional.of(new MyEntry<>(bst.getKey().get(), bst.getValue().get()));
			else return largest(right);
		}
	}

	public static <Key extends Comparable<Key>, Value> Optional<Bst<Key, Value>> deleteLargest(Bst<Key, Value> bst) {
		if (isEmpty(bst)) return Optional.empty();
		else {
			Bst<Key, Value> right = bst.getRight().get();
			Bst<Key, Value> left = bst.getLeft().get();

			if (isEmpty(right)) return Optional.of(left);
			else {
				Optional<Bst<Key, Value>> r = deleteLargest(right);
				assert (r.isPresent());
				return Optional.of(new Fork<>(bst.getKey().get(), bst.getValue().get(), left, r.get()));
			}
		}
	}

	public static <Key extends Comparable<Key>, Value> Optional<Bst<Key, Value>> delete(Bst<Key, Value> bst, Key k) {
		if (isEmpty(bst)) return Optional.empty();
		else {
			Key key = bst.getKey().get();
			Value value = bst.getValue().get();
			Bst<Key, Value> right = bst.getRight().get();
			Bst<Key, Value> left = bst.getLeft().get();

			if (k.compareTo(key) == 0) {
				if (isEmpty(left)) return Optional.of(right);
				else if (isEmpty(right)) return Optional.of(left);
				else {
					// Get largest entry of left subtree.
					Optional<MyEntry<Key, Value>> ole = largest(left);
					// It must exist.
					assert (ole.isPresent());
					MyEntry<Key, Value> le = ole.get();
					// Now get subtree with largest deleted.
					Optional<Bst<Key, Value>> sld = deleteLargest(left);
					// It must exist.
					assert (sld.isPresent());
					Bst<Key, Value> newleft = sld.get();
					return Optional.of(new Fork<>(le.getKey(), le.getValue(), newleft, right));
				}
			} else if (k.compareTo(key) < 0) { // We have to delete from one of the subtrees.
				Optional<Bst<Key, Value>> l = delete(left, k);
				if (l.isPresent()) return Optional.of(new Fork<>(key, value, l.get(), right));
				else return Optional.empty();
			} else {
				Optional<Bst<Key, Value>> r = delete(right, k);
				if (r.isPresent()) return Optional.of(new Fork<>(key, value, left, r.get()));
				else return Optional.empty();
			}
		}
	}

	public static <Key extends Comparable<Key>, Value> int size(Bst<Key, Value> bst) {
		if (isEmpty(bst)) return 0;
		else {
			return 1 + size(bst.getLeft().get()) + size(bst.getRight().get());
		}
	}

	public static <Key extends Comparable<Key>, Value> int height(Bst<Key, Value> bst) {
		if (isEmpty(bst)) return -1;
		else {
			return 1 + Math.max(height(bst.getLeft().get()), height(bst.getRight().get()));
		}
	}

	public static <Key extends Comparable<Key>, Value> void saveInOrder(Bst<Key, Value> bst, MyEntry<Key, Value> a[]) {
		// Saves the tree in the array a.
		// Returns the next available position in the array.
		saveInOrder(bst, a, 0);
	}

	public static <Key extends Comparable<Key>, Value> int saveInOrder(Bst<Key, Value> bst, MyEntry<Key, Value> a[], int i) {
		if (isEmpty(bst)) return i;
		else {
			int j = saveInOrder(bst.getLeft().get(), a, i);
			a[j] = new MyEntry<>(bst.getKey().get(), bst.getValue().get());
			int k = saveInOrder(bst.getRight().get(), a, j + 1);
			return k;
		}
	}

	public static <Key extends Comparable<Key>, Value> Bst<Key, Value> balanced(Bst<Key, Value> bst) {
		if (isEmpty(bst)) return bst;
		else {
			MyEntry<Key, Value> e = new MyEntry<>(bst.getKey().get(), bst.getValue().get());
			@SuppressWarnings("unchecked")
			MyEntry<Key, Value>[] a = (MyEntry<Key, Value>[]) Array.newInstance(e.getClass(), size(bst));

			saveInOrder(bst, a);

			return balanced(bst, a, 0, a.length);
		}
	}

	private static <Key extends Comparable<Key>, Value> Bst<Key, Value> balanced(Bst<Key, Value> bst, MyEntry<Key, Value>[] a, int start, int end) {
		assert (start <= end);

		if (start == end) return new Empty<>();

		assert (start < end);
		int middle = start + (end - start) / 2; // We avoid overflow this way.
		assert (start <= middle && middle < end);
		return new Fork<Key, Value>(a[middle].getKey(), a[middle].getValue(), balanced(bst, a, start, middle),
			balanced(bst, a, middle + 1, end));
	}

	/**
	 * @param bst
	 *            the bst to convert
	 * @return an array containing the elements of the bst in order
	 */
	@SuppressWarnings("unchecked")
	public static <Key extends Comparable<Key>, Value> MyEntry<Key, Value>[] toArray(Bst<Key, Value> bst) {
		MyEntry<Key, Value>[] a = (MyEntry<Key, Value>[]) Array.newInstance(MyEntry.class, Genius.size(bst));
		Genius.saveInOrder(bst, a);
		return a;
	}

}
