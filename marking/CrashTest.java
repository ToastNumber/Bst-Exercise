import static org.junit.Assert.assertFalse;

import java.lang.reflect.Array;

import org.junit.runner.RunWith;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;

// No marks awarded or deducted. This is just to help the markers.

/**
 * Tests whether the methods in the bst interface can at least be run without crashing
 *
 * @author Kelsey McKenna
 *
 */
@RunWith(JUnitQuickcheck.class)
public class CrashTest {
	public final InputGenerator gen = new InputGenerator();

	@Property
	public void balancedShouldNotCrash(@From(TreeGenerator.class) TreeWrapper c) {
		c.get().balanced();
	}

	@Property
	public void deleteShouldNotCrash(@From(TreeGenerator.class) TreeWrapper c, Integer randKey, Boolean randBool) {
		Bst<Integer, String> t = c.get();

		if (Genius.isEmpty(t)) t.delete(randKey);
		else {
			int k;

			if (randBool) k = gen.randomEntry(t).getKey();
			else k = randKey;

			t.delete(k);
		}
	}

	@Property
	public void deleteSmallestShouldNotCrash(@From(TreeGenerator.class) TreeWrapper c) {
		c.get().deleteSmallest();
	}

	@Property
	public void deleteLargestShouldNotCrash(@From(TreeGenerator.class) TreeWrapper c) {
		c.get().deleteLargest();
	}

	@Property
	public void findShouldNotCrash(@From(TreeGenerator.class) TreeWrapper c, Integer randKey, Boolean randBool) {
		Bst<Integer, String> t = c.get();

		if (Genius.isEmpty(t)) assertFalse(t.find(randKey).isPresent());
		else {
			Integer k;

			if (randBool) k = gen.randomEntry(t).getKey();
			else k = randKey;

			t.find(k);
		}
	}

	@Property
	public void hasShouldNotCrash(@From(TreeGenerator.class) TreeWrapper c, Integer randKey, Boolean randBool) {
		Bst<Integer, String> t = c.get();

		if (Genius.isEmpty(t)) assertFalse(t.has(randKey));
		else {
			Integer k;

			if (randBool) k = gen.randomEntry(t).getKey();
			else k = randKey;

			t.has(k);
		}
	}

	@Property
	public void heightShouldNotCrash(@From(TreeGenerator.class) TreeWrapper c) {
		c.get().height();
	}

	@Property
	public void isEmptyShouldNotCrash(@From(TreeGenerator.class) TreeWrapper c) {
		c.get().isEmpty();
	}

	@Property
	public void putShouldNotCrash(@From(TreeGenerator.class) TreeWrapper c, Integer randKey, String randValue) {
		c.get().put(randKey, randValue);
	}

	@Property
	public void saveInOrderShouldNotCrash(@From(TreeGenerator.class) TreeWrapper c) {
		Bst<Integer, String> t = c.get();
		@SuppressWarnings("unchecked")
		Entry<Integer, String>[] entries = (Entry<Integer, String>[]) Array.newInstance(Entry.class, Genius.size(t));

		t.saveInOrder(entries);
	}

	@Property
	public void sizeShouldNotCrash(@From(TreeGenerator.class) TreeWrapper c) {
		c.get().size();
	}

	@Property
	public void smallerShouldNotCrash(@From(TreeGenerator.class) TreeWrapper c, Integer randKey) {
		c.get().smaller(randKey);
	}

	@Property
	public void biggerShouldNotCrash(@From(TreeGenerator.class) TreeWrapper c, Integer randKey) {
		c.get().bigger(randKey);
	}

	@Property
	public void smallestShouldNotCrash(@From(TreeGenerator.class) TreeWrapper c, Integer randKey) {
		c.get().smallest();
	}

	@Property
	public void largestShouldNotCrash(@From(TreeGenerator.class) TreeWrapper c, Integer randKey) {
		c.get().largest();
	}

}
