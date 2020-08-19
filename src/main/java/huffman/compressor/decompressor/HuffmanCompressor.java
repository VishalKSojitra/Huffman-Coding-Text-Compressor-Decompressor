package huffman.compressor.decompressor;

import java.io.*;
import java.util.*;

public class HuffmanCompressor {
	
	public byte[] encode(String inputFileName) throws IOException {

		HashMap<Character, Integer> letterFrequency = new HashMap<>();
		BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
		// Read the file and count the frequency of charcter in text file
		int line;
		while ((line = reader.read()) != -1) {
			char c = (char) line;
			letterFrequency.put(c, letterFrequency.getOrDefault(c, 0) + 1);
		}
		reader.close();
		
		if(letterFrequency.isEmpty()) {
			return new byte[0];
		}
		 System.out.println("Letter Frequency : ");
		 System.out.println("\t\t" + letterFrequency.keySet());
		 System.out.println("\t\t" + letterFrequency.values());

		// Generate Huffman Tree using character frequency
		Node root = createHuffmanTree(letterFrequency);
		System.out.println("Huffman Tree : ");
		root.printTree(root,"Root");

		// Assign new encode to character base on Huffman Tree
		HashMap<String, String> newEncodeForCharacter = newEncodingFromTree(root, "", new HashMap<String, String>());

		// Encode text using new encoding of character
		StringBuffer encodedText = generateEncodedtext(newEncodeForCharacter, inputFileName);

		// Add padding to make size of encoded text multiple of 8
		StringBuffer paddedEncodedText = generatePaddedEncodedtext(encodedText);

		// Create preorder string of Huffman Tree
		StringBuffer preorderStingTree = createPreorderStringFromTree(root, new StringBuffer());

		// Add padding to make size of tree multiple of 8
		StringBuffer paddedPreorderStingTree = generatePaddedEncodedtext(preorderStingTree);

		// Get preorder tree size in binary of size 16 bit with leading '0', Ex. 96 will
		// became '0000000001100000'
		// Will store size of tree in compressed file so it will be used during
		// decompression
		// Than concatenate padded preorder string to size of tree in binary
		paddedPreorderStingTree.insert(0,
				String.format("%16s", Integer.toBinaryString(paddedPreorderStingTree.length())).replaceAll(" ", "0"));

		// Concatenate padded preorder tree to padded encoded text
		paddedEncodedText.insert(0, paddedPreorderStingTree);

		// Convert binary encoded text to byte array so it could be written in binary
		// file.
		return createByteArray(paddedEncodedText.toString());
	}
	
	public void fileWriter(byte[] encodedTextInByte, String outputFileName) throws IOException {
		BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(outputFileName));
		writer.write(encodedTextInByte);
		writer.close();
	}

	// Function to create Huffman Tree using Character's Frequency
	public Node createHuffmanTree(HashMap<Character, Integer> letterFrequency) {
		PriorityQueue<Node> pQueue = new PriorityQueue<Node>((x, y) -> x.getWeight() - y.getWeight());

		for (char key : letterFrequency.keySet()) {
			Node treeNode = new Node(Character.toString(key), letterFrequency.get(key));
			pQueue.add(treeNode);
		}

		while (pQueue.size() > 1) {
			Node left = pQueue.poll();
			Node right = pQueue.poll();
			Node newNode = new Node("null", left.getWeight() + right.getWeight());
			newNode.setLeft(left);
			newNode.setRight(right);
			pQueue.add(newNode);
		}
		return pQueue.poll();
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

	// Function to encode text according to new encoding
	public StringBuffer generateEncodedtext(HashMap<String, String> newEncodeForCharacter, String inputFileName)
			throws IOException {
		StringBuffer encodedText = new StringBuffer();
		BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
		int line;
		while ((line = reader.read()) != -1) {
			char c = (char) line;
			encodedText.append(newEncodeForCharacter.get(Character.toString(c)));
		}
		reader.close();
		return encodedText;
	}

	// Concat '0' at ending of string to make it's size multiple to 8.
	public StringBuffer generatePaddedEncodedtext(StringBuffer encodedText) {
		int padSize = 8 - encodedText.length() % 8;
		for (int i = 0; i < padSize; i++) {
			encodedText.append("0");
		}
		encodedText.insert(0, String.format("%8s", Integer.toBinaryString(padSize)).replaceAll(" ", "0"));
		return encodedText;
	}

	// Create preorder string of tree
	public StringBuffer createPreorderStringFromTree(Node root, StringBuffer preorderStringFromTree) {
		if (root != null) {
			if (root.getLetter() != "null") {
				int c = root.getLetter().charAt(0);
				preorderStringFromTree
						.append("1" + String.format("%9s", Integer.toBinaryString(c)).replaceAll(" ", "0"));
			} else {
				preorderStringFromTree.append("0");
			}
			if (root != null) {
				createPreorderStringFromTree(root.getLeft(), preorderStringFromTree);
			}
			if (root != null) {
				createPreorderStringFromTree(root.getRight(), preorderStringFromTree);
			}
		}
		return preorderStringFromTree;
	}

	// This will read 8 bit and convert it to byte and create array of it
	public byte[] createByteArray(String paddedEncodedText) {
		if (paddedEncodedText.length() % 8 != 0) {
			System.out.println("Error !! Something went wrong.");
			System.exit(0);
		}

		byte[] byteArray = new byte[paddedEncodedText.length() / 8];
		for (int i = 0, j = 0; i < paddedEncodedText.length(); i = i + 8) {
			paddedEncodedText.substring(i, i + 8);
			byteArray[j++] = (byte) Integer.parseInt(paddedEncodedText.substring(i, i + 8), 2);
		}
		return byteArray;
	}
}
