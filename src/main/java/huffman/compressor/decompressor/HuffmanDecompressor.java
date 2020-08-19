package huffman.compressor.decompressor;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HuffmanDecompressor {

	public String decode(String inputFileName) throws IOException {
		BufferedInputStream reader = new BufferedInputStream(new FileInputStream(inputFileName));

		// Read first two byte which is going to be size of padded encoded preorder tree
		// in 16 bit binary format
		int size = reader.read();
		if(size == -1) {
			reader.close();
			return new String();
		}
		size = Integer.parseInt(String.format("%8s", Integer.toBinaryString(size)).replaceAll(" ", "0")
				+ String.format("%8s", Integer.toBinaryString(reader.read())).replaceAll(" ", "0"), 2);
		
		// This will gives size of padded bit of encoded preorder tree.
		int padBitSize = reader.read();

		// Read byte by byte file of size tree and convert it to binary format string.
		StringBuffer tree = new StringBuffer();
		for (int i = 0; i < (size / 8 - 1); i++) {
			tree.append(String.format("%8s", Integer.toBinaryString(reader.read())).replaceAll(" ", "0"));
		}

		// Remove added bit from encoded preorder tree string
		String treeInPreorder = tree.substring(0, tree.length() - padBitSize);

		Node root = new Node("null", 0);
		// Create tree from preorder string
		System.out.println(treeInPreorder);
		root = stringToTree(1, root, treeInPreorder);
		// root.printTree(root,"root");

		// Assign new encode to character base on Huffman Tree
		HashMap<String, String> newEncodeForCharacter = newEncodingFromTree(root, "", new HashMap<String, String>());

		// Swap value to key in Map for reverse mapping Ex. {'g':'01'} to {'01':'g'}
		Map<String, String> SwapedNewEncodeForCharacter = newEncodeForCharacter.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
		// This will gives padding size of encoded text.
		int padSize = reader.read();

		// Read byte by byte whole compressed file and convert it to binary format
		// string
		StringBuffer bit_string = new StringBuffer();
		while (reader.available() != 0) {
			bit_string.append(String.format("%8s", Integer.toBinaryString(reader.read())).replaceAll(" ", "0"));
		}

		// Remove padded bit from encoded text
		String EncodedString = bit_string.substring(0, bit_string.length() - padSize);
		
		// Retrieve original text from encoded text
		String originalText = decodeText(EncodedString, SwapedNewEncodeForCharacter);
		reader.close();
		return originalText;
	}
	
	public void fileWriter(String originalText, String outputFileName) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName));
		writer.write(originalText);
		writer.close();
	}

	// Function will convert preorder satring to tree
	public Node stringToTree(int itration, Node root, String tree) {
		int storeRightOrLeftSide = 0;

		while (itration < tree.length()) {

			// If it is '0' means internal node so create empty node and
			// Add to the tree according to the side given by variable side
			if (tree.charAt(itration) == '0') {
				itration++;
				Node temp = new Node("null", 0);

				// If side is 0 than add node to left side and make empty node's parent to Root
				// node
				if (storeRightOrLeftSide == 0) {
					root.setLeft(temp);
					temp.setParent(root);
					root = temp;
				}

				// If side is 1 than add node to right side and make empty node's parent to Root
				// node
				// Make side to 0 because after right side it will goes to left side in preorder
				// traversal
				if (storeRightOrLeftSide == 1) {
					root.setRight(temp);
					temp.setParent(root);
					root = temp;
					storeRightOrLeftSide = 0;
				}
			}

			// If it is '1' means leaf node so create node with letter and
			// it's weight and add to the tree according to the side given by variable side.
			if (tree.charAt(itration) == '1') {
				itration += 10;
				Node temp = new Node(String.valueOf((char) Integer.parseInt(tree.substring(itration - 9, itration), 2)),
						0);

				// If side is 0 than add node to left side and make side to '1' so it will goes
				// in to right subTree
				// Else add node it right side than backtrack to node which right side is empty.
				if (storeRightOrLeftSide == 0) {
					root.setLeft(temp);
					storeRightOrLeftSide = 1;
				} else {
					root.setRight(temp);
					while (root.getParent() != null && root.getRight() != null) {
						root = root.getParent();
					}
				}
			}
		}
		return root;
	}

	// Function will decode binary text to character in given encode text
	public String decodeText(String encodedString, Map<String, String> swapedNewEncodeForCharacter) {
		StringBuffer currentText = new StringBuffer();
		StringBuffer decodedText = new StringBuffer();
		for (char c : encodedString.toCharArray()) {
			currentText.append(c);
			if (swapedNewEncodeForCharacter.containsKey(currentText.toString())) {
				decodedText.append(swapedNewEncodeForCharacter.get(currentText.toString()));
				currentText = new StringBuffer();
			}
		}
		return decodedText.toString();
	} 

	// Function to assign new code to character according to Huffman Tree
	public HashMap<String, String> newEncodingFromTree(Node root, String prefix, HashMap<String, String> code) {
		if (!(root.getLetter() == "null")) {
			code.put(root.getLetter(), prefix);
			return code;
		}
		newEncodingFromTree(root.getLeft(), prefix + "0", code);
		newEncodingFromTree(root.getRight(), prefix + "1", code);
		return code;
	}
}
