package huffman.compressor.decompressor;


public class Node {
	private String letter;
	private int weight;
	private Node left;
	private Node right;
	private Node parent;

	public Node(String letter, int weight) {
		this.setLetter(letter);
		this.setWeight(weight);
		this.setLeft(null);
		this.setRight(null);
		this.setParent(null);
	}

	@Override
	public String toString() {
		return (this.getLetter() + "\t" + this.getWeight() + "\t");

	}
	
	@Override
	public boolean equals(Object node) {
		if (this == node) {
			return true;
		}
		if (node == null || !(node instanceof Node)) {
			return false;
		}
		Node newNode = (Node) node;
		return this.getLetter().equals(newNode.getLetter()) && this.getWeight() == newNode.getWeight() &&
				equals(this.left, newNode.getLeft()) && equals(this.right, newNode.getRight());
	}
	
	private boolean equals(Node x, Node y) {
        if (x == null) {
        	return y == null;
        } else {
        	return x.equals(y);
        }
    }
	
	
	public void printTree(Node root, String r) {
		if (root == null) {
			return;
		} else {
			System.out.println(root + " " + r);
		}
		printTree(root.getLeft(), "Left");
		printTree(root.getRight(), "Right");
	}

	public Node getLeft() {
		return left;
	}

	public void setLeft(Node left) {
		this.left = left;
	}

	public Node getRight() {
		return right;
	}

	public void setRight(Node right) {
		this.right = right;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public String getLetter() {
		return letter;
	}

	public void setLetter(String letter) {
		this.letter = letter;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

}
