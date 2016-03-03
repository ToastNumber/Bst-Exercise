/**
 * A wrapper class for Bst<Integer, String> objects
 * @author Auke
 *
 */
public class TreeWrapper {
	public final Bst<Integer,String> obj;
	TreeWrapper(final Bst<Integer,String> newObj) {
		this.obj=newObj;
	}
	public Bst<Integer,String> get() {
		return obj;
	}
	@Override
	public String toString() {
		return "TreeWrapper{" + this.obj.fancyToString() + "}";
	}
}
