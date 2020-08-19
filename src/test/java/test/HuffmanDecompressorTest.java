package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import huffman.compressor.decompressor.HuffmanDecompressor;
import huffman.compressor.decompressor.Node;

class HuffmanDecompressorTest {
	
	private HuffmanDecompressor huffmanDecompressor;

	@BeforeEach
	void setUp() throws Exception {
		huffmanDecompressor = new HuffmanDecompressor();
	}

	@Test
	void testDecode() throws IOException {
		assertEquals("test", huffmanDecompressor.decode("src/test/resources/testoutput1.dat"));
		assertEquals(new String(), huffmanDecompressor.decode("src/test/resources/testemptyoutput.dat"));
	}

	@Test
	void testStringToTree() {
		Node rootNode = new Node("null", 0);
		rootNode.setLeft(new Node("a", 0));
		rootNode.setRight(new Node("b", 0));
		Node root = new Node("null", 0);
		assertEquals(rootNode, huffmanDecompressor.stringToTree(1, root, "010011000011001100010"), "Must be same");
	}

	@Test
	void testDecodeText() {
		HashMap<String, String> swapedNewEncodeForCharacter = new HashMap<String, String>();
		assertEquals("", huffmanDecompressor.decodeText("00111", swapedNewEncodeForCharacter));
		swapedNewEncodeForCharacter.put("0", "a");
		swapedNewEncodeForCharacter.put("1", "b");
		assertEquals("aabbb", huffmanDecompressor.decodeText("00111", swapedNewEncodeForCharacter));
	}

	@Test
	void testNewEncodingFromTree() {
		HashMap<String, String> newEncodeForCharacter = new HashMap<String, String>();
		Node root = new Node("null", 5);
		Node leftNode = new Node("a", 2);
		Node rightNode = new Node("b", 3);
		root.setLeft(leftNode);
		root.setRight(rightNode);
		assertFalse(newEncodeForCharacter.equals(huffmanDecompressor.newEncodingFromTree(root, "", new HashMap<String, String>())));
		
		newEncodeForCharacter.put("a", "0");
		newEncodeForCharacter.put("b", "1");
		assertTrue(newEncodeForCharacter.equals(huffmanDecompressor.newEncodingFromTree(root, "", new HashMap<String, String>())));

	}

}
