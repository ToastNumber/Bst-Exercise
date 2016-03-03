
/**
 * InputGenerator class for BSTs.
 *
 * @author Ermano Arruda
 */
public class MyEntry<Key extends Comparable<Key>, Value> {

	private final Key key;
	private final Value value;

	public MyEntry(Key key, Value value) {
		this.key = key;
		this.value = value;
	}

	public Key getKey() {
		return key;
	}

	public Value getValue() {
		return value;
	}

	@Override
	public String toString() {
		return String.format("(%s, %s)", key.toString(), value.toString());
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof MyEntry)) return false;
		else {
			@SuppressWarnings("unchecked")
			MyEntry<Key, Value> other = (MyEntry<Key, Value>) obj;
			return key.equals(other.getKey()) && value.equals(other.getValue());
		}
	}

	@Override
	public int hashCode() {
		return 37 * key.hashCode() + value.hashCode();
	}
}
