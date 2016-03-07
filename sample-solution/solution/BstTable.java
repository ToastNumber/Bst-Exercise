package solution;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class BstTable<Key extends Comparable<Key>, Value> implements Table<Key, Value> {
	private final Bst<Key, Value> bst;

	public BstTable() {
		this.bst = new Empty<>();
	}

	public BstTable(Bst<Key, Value> bst) {
		this.bst = bst;
	}

	@Override
	public boolean containsKey(Key k) {
		return bst.has(k);
	}

	@Override
	public Optional<Value> get(Key k) {
		return bst.find(k);
	}

	@Override
	public boolean isEmpty() {
		return bst.isEmpty();
	}

	@Override
	public Table<Key, Value> put(Key k, Value v) {
		return new BstTable<>(bst.put(k, v));
	}

	@Override
	public Optional<Table<Key, Value>> remove(Key k) {
		Optional<Bst<Key, Value>> temp = bst.delete(k);
		if (temp.isPresent()) return Optional.of(new BstTable<>(temp.get()));
		else return Optional.empty();
	}

	@Override
	public int size() {
		return bst.size();
	}

	@Override
	public Collection<Value> values() {
		List<Value> answer = new ArrayList<>();

		for (Entry<Key, Value> entry : entries())
			answer.add(entry.getValue());

		return answer;
	}

	@Override
	public Collection<Key> keys() {
		List<Key> answer = new ArrayList<>();

		for (Entry<Key, Value> entry : entries())
			answer.add(entry.getKey());

		return answer;
	}

	private Entry<Key, Value>[] entries() {
		@SuppressWarnings("unchecked")
		Entry<Key, Value>[] a = (Entry<Key, Value>[]) Array.newInstance(Entry.class, size());

		bst.saveInOrder(a);

		return a;
	}

}
