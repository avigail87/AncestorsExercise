

public class Node {

	private final int data; 
	private final Node left; 
	private final Node right;
	
	public Node(int data, Node left, Node right) {
		super();
		this.data = data;
		this.left = left;
		this.right = right;
	}
	
	public int getData() {
		return data;
	}
	public Node getLeft() {
		return left;
	}
	public Node getRight() {
		return right;
	}

}
