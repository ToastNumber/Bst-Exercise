import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;

/**
 * Tests the student's code for the lower second class grade, i.e. the methods
 * isEmpty, smaller, bigger, has, find, put, and delete.
 *
 * @author Kelsey McKenna
 *
 */
public final class GradeLower2nd extends IntegrationTest {
	private List<TreeAssertion> assertions =
		new ArrayList<>(Arrays.asList(isEmptyAssertion, smallerAssertion, biggerAssertion, hasAssertion, findAssertion));

	private List<Function<Bst<Integer, String>, Bst<Integer, String>>> functions =
		new ArrayList<>(Arrays.asList(putAssertion, deleteAssertion));

	/**
	 * @param c
	 *            the tree to operate on
	 * @param numOperations
	 *            the number of operations to perform
	 */
	@Property
	public void emptyThroughDeleteShouldWorkTogether(@From(TreeGenerator.class) TreeWrapper c, @InRange(min = "10", max = "100") Integer numOperations) {
		super.testMethodsWorkTogether(c.get(), numOperations, assertions, functions);
	}

}
