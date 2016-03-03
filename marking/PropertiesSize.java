import static org.junit.Assert.assertEquals;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;

// Award 0.3, 0.4, 0.3 respectively (total 1)

public class PropertiesSize extends IsolationTest {

	/**
	 * empty.size() should be 0
	 */
	@Property
	public void sizeOfEmptyShouldBe0() {
		assertEquals(0, new Empty<Integer, String>().size());

		Marks.award(0.3, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * Genius.size(t) should be equal to t.size()
	 *
	 * @param c
	 *            the tree to work on
	 */
	@Property
	public void sizeIsAsGoodAsGenius(@From(TreeGenerator.class) TreeWrapper c) {
		assertEquals(Genius.size(c.get()), c.get().size());

		Marks.award(0.4, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * t.size() should not change t
	 *
	 * @param c
	 *            the tree to work on.
	 */
	@Property
	public void shouldBeImmutable(@From(TreeGenerator.class) TreeWrapper c) {
		testImmutability(c.get(), t -> t.size());

		Marks.award(0.3, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}
}
