import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;

// Add points 1,3,1 respectively for each passed property (total 5)

/**
 * Tests the <code>isEmpty</code> method in the Bst interface, but is only allowed to use methods before <code>delete()</code>, i.e.:
 * <code>isEmpty(), smaller(), bigger(), has(), find(), put()</code>.
 *
 * @author Kelsey McKenna
 *
 */
public class PropertiesIsEmpty extends IsolationTest {
	/**
	 * empty.isEmpty() should be true
	 */
	@Property
	public void emptyTreeShouldBeEmpty() {
		assertTrue(new Empty<>().isEmpty());
		Marks.award(1, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * Genius.isEmpty(t) should be equal to t.isEmpty()
	 *
	 * @param c
	 *            the tree to work on
	 */
	@Property
	public void emptyIfAndOnlyIf(@From(TreeGenerator.class) TreeWrapper c) {
		assertEquals(Genius.isEmpty(c.get()), c.get().isEmpty());
		Marks.award(3, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * t.isEmpty() should not change t
	 *
	 * @param c
	 *            the tree to work on
	 */
	@Property
	public void shouldBeImmutable(@From(TreeGenerator.class) TreeWrapper c) {
		testImmutability(c.get(), t -> t.isEmpty());
		Marks.award(1, this.getClass().getName() + ": " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

}
