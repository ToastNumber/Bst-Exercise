import static org.junit.Assert.assertEquals;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;

// Award 0.6, 0.8, 0.6 respectively (total 2)

public class PropertiesHeight extends IsolationTest {
	/**
	 * The height of the empty tree should be -1
	 */
	@Property
	public void heightOfEmptyShouldBeMinus1() {
		assertEquals(-1, new Empty<Integer, String>().height());
		Marks.award(0.6, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * Genius.height(t) should equal t.height()
	 *
	 * @param c
	 *            the tree to work on
	 */
	@Property
	public void heightIsAsGoodAsGenius(@From(TreeGenerator.class) TreeWrapper c) {
		assertEquals(Genius.height(c.get()), c.get().height());
		Marks.award(0.8, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * t.height() should not change t
	 *
	 * @param c
	 *            the tree to work on
	 */
	@Property
	public void isImmutable(@From(TreeGenerator.class) TreeWrapper c) {
		testImmutability(c.get(), t -> t.height());
		Marks.award(0.6, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}
}
