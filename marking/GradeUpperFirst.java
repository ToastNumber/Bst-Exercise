import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;

// 1 mark. This should be mostly for the markers to look at if it fails!

/**
 * Tests the student's code for the upper first class grade, i.e. the methods
 * isEmpty, smaller, bigger, has, find, put, delete, smallest, deleteSmallest,
 * largest, deleteLargest, size, height, saveInOrder, and balanced.
 *
 * @author Kelsey McKenna
 *
 */
public final class GradeUpperFirst extends IntegrationTest {
	private List<TreeAssertion> assertions =
		new ArrayList<>(Arrays.asList(isEmptyAssertion, smallerAssertion, biggerAssertion, hasAssertion, findAssertion, smallestAssertion,
			deleteSmallestAssertion, largestAssertion, deleteLargestAssertion, sizeAssertion, heightAssertion, saveInOrderAssertion));

	private List<Function<Bst<Integer, String>, Bst<Integer, String>>> functions =
		new ArrayList<>(Arrays.asList(putAssertion, deleteAssertion, balancedAssertion));

	/**
	 * @param c
	 *            the tree to operate on
	 * @param numOperations
	 *            the number of operations to perform
	 */
	@Property
	public void allMethodsShouldWorkTogether(@From(TreeGenerator.class) TreeWrapper c, @InRange(min = "10", max = "100") Integer numOperations) {
		testMethodsWorkTogether(c.get(), numOperations, assertions, functions);
	}

}
