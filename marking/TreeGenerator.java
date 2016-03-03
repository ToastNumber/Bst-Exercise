import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

/**
 * Generator class for building random trees
 *
 * @author Auke
 *
 */
public class TreeGenerator extends Generator<TreeWrapper> {
	public TreeGenerator() {
		super(TreeWrapper.class);
	}

	@Override
	public TreeWrapper generate(SourceOfRandomness random, GenerationStatus status) {
		Integer[] sizes = { 0, 1, 2, 3, 4, 5, 7, 13, 16 };
		Integer size = random.nextElement(sizes);
		List<Integer> keys = new ArrayList<Integer>();
		for (Integer i = 0; i < size; i++) {
			keys.add(random.nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE));
		}
		Collections.sort(keys);
		return new TreeWrapper(generateNodeWithBounds(random, status, keys));
	}

	public Bst<Integer, String> generateNodeWithBounds(SourceOfRandomness random, GenerationStatus status, List<Integer> keys) {
		if (keys.isEmpty()) {
			return new Empty<Integer, String>();
		} else {
			Integer midpoint = random.nextInt(0, keys.size() - 1);
			String value = Character.toString(random.nextChar('A', 'z'));
			Bst<Integer, String> left = this.generateNodeWithBounds(random, status, keys.subList(0, midpoint));
			Bst<Integer, String> right = this.generateNodeWithBounds(random, status, keys.subList(midpoint + 1, keys.size()));
			return new Fork<Integer, String>(keys.get(midpoint), value, left, right);
		}
	}

	@Override
	public List<TreeWrapper> doShrink(SourceOfRandomness random, TreeWrapper larger) {
		Bst<Integer, String> bst = larger.get();
		Optional<Bst<Integer, String>> left = bst.getLeft(), right = bst.getRight();
		List<TreeWrapper> shrinks = new ArrayList<>();
		if (left.isPresent()) shrinks.add(new TreeWrapper(left.get()));
		if (right.isPresent()) shrinks.add(new TreeWrapper(right.get()));
		return shrinks;
	}

}
