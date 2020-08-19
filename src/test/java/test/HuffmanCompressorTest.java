package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import huffman.compressor.decompressor.HuffmanCompressor;
import huffman.compressor.decompressor.Node;

class HuffmanCompressorTest {

	private HuffmanCompressor huffmanCompressor;

	@BeforeEach
	void setUp() throws Exception {
		huffmanCompressor = new HuffmanCompressor();
	}

	@Test
	void testEncode() throws IOException {
		
		byte[] encodedTextInByte = {0, 48, 8, 78, -119, -50, 101, 0, 2, 112};
		assertArrayEquals(encodedTextInByte, huffmanCompressor.encode("src/test/resources/testinput1.txt"));
		assertArrayEquals(new byte[0], huffmanCompressor.encode("src/test/resources/testemptyinput.txt"));
	}

	@Test
	void testCreateHuffmanTree() {
		HashMap<Character, Integer> letterFrequency = new HashMap<Character, Integer>();
		
		Node root = new Node("null", 5);
		Node leftNode = new Node("a", 2);
		Node rightNode = new Node("b", 3);
		root.setLeft(leftNode);
		root.setRight(rightNode);
		assertNotEquals(root, huffmanCompressor.createHuffmanTree(letterFrequency), "createHuffmanTree must return null");
		
		letterFrequency.put('a', 2);
		letterFrequency.put('b',3);
		assertEquals(root, huffmanCompressor.createHuffmanTree(letterFrequency), "Must be same");
		assertEquals(root, root, "Must be same");
		
		root.setRight(leftNode);
		assertNotEquals(root, huffmanCompressor.createHuffmanTree(letterFrequency), "Must not be same");
	}

	@Test
	void testNewEncodingFromTree() {
		HashMap<String, String> newEncodeForCharacter = new HashMap<String, String>();
		Node root = new Node("null", 5);
		Node leftNode = new Node("a", 2);
		Node rightNode = new Node("b", 3);
		root.setLeft(leftNode);
		root.setRight(rightNode);
		assertFalse(newEncodeForCharacter.equals(huffmanCompressor.newEncodingFromTree(root, "", new HashMap<String, String>())));
		
		newEncodeForCharacter.put("a", "0");
		newEncodeForCharacter.put("b", "1");
		assertTrue(newEncodeForCharacter.equals(huffmanCompressor.newEncodingFromTree(root, "", new HashMap<String, String>())));
	}

	@Test
	void testGenerateEncodedtext() throws IOException {
		HashMap<String, String> newEncodeForCharacter = new HashMap<String, String>();	
		newEncodeForCharacter.put("a", "0");
		newEncodeForCharacter.put("b", "1");
		String expectedEncodedtext = new String("00111");
		String actualEncodedtext = huffmanCompressor.generateEncodedtext(newEncodeForCharacter, "src/test/resources/testinput2.txt").toString();
		assertTrue(expectedEncodedtext.equals(actualEncodedtext), "Must be 00111");
		assertFalse("00".equals(actualEncodedtext), "Must not be 00111");
	}

	@Test
	void testGeneratePaddedEncodedtext() {
		StringBuffer encodedText = new StringBuffer("");
		assertEquals("0000100000000000", huffmanCompressor.generatePaddedEncodedtext(encodedText).toString());
		
		encodedText = new StringBuffer("1");
		assertEquals("0000011110000000", huffmanCompressor.generatePaddedEncodedtext(encodedText).toString());
		
		encodedText = new StringBuffer("11");
		assertEquals("0000011011000000", huffmanCompressor.generatePaddedEncodedtext(encodedText).toString());
		
		encodedText = new StringBuffer("11110000");
		assertEquals("000010001111000000000000", huffmanCompressor.generatePaddedEncodedtext(encodedText).toString());
	}

	@Test
	void testCreatePreorderStringFromTree() {
		Node root = new Node("null", 5);
		assertEquals("0", huffmanCompressor.createPreorderStringFromTree(root, new StringBuffer()).toString());
		
		Node leftNode = new Node("a", 2);
		Node rightNode = new Node("b", 3);
		root.setLeft(leftNode);
		root.setRight(rightNode);
		assertEquals("010011000011001100010", huffmanCompressor.createPreorderStringFromTree(root, new StringBuffer()).toString());
	}

	@Test
	void testCreateByteArray() {
		//huffmanCompressor = new HuffmanCompressor();
		assertEquals(-1, huffmanCompressor.createByteArray("11111111")[0]);
	}

}
