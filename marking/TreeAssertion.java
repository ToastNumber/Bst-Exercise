
/**
 * Represents an assertion that is run on a binary search tree.
 * When the <code>run</code> method is executed, it should evaluate the
 * bst and perform an assertion.
 *
 * @author Kelsey McKenna
 *
 */
public interface TreeAssertion {
	public void run(Bst<Integer, String> bst);
}
