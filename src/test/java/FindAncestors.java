import java.util.ArrayList;
import java.util.List;

public class FindAncestors {
	
	static List<Integer> ancestors = new ArrayList<Integer>();
	
	private static boolean displayAncestors(Node root, int num) {
		if (root == null) {
			return false;
		} else if (root.getData() == num) {
			ancestors.add(num);
			return true;
		} else {
			if ((displayAncestors(root.getLeft(), num)) || (displayAncestors(root.getRight(), num))) {
				ancestors.add(root.getData());
				return true;
			}
			return false;
		}
	}
	
	public static List<Integer> getAncestors(Node root, int num) {
		ancestors.clear();
		displayAncestors(root, num);
		return ancestors;
	}
	
	public static void main(String[] args) {
		Node root = new Node(56, new Node(42, new Node(10, null, null), new Node(1, null, null)), new Node(3, null, null));
		System.out.println(getAncestors(root, 10));
	}
}
