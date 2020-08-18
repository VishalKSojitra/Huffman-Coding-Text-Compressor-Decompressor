package huffman.compressor.decompressor;

import java.io.IOException;

public class Huffman {

	public static void main(String[] args) throws IOException {
		System.out.println("Compressing Input File to Output.dat File.");
		
		HuffmanCompressor c = new HuffmanCompressor();
		
		byte[] encodedTextInByte = c.encode("src/main/resources/input.txt");
		c.fileWriter(encodedTextInByte, "src/main/resources/output.dat");

		System.out.println("\nDecompressing Output.dat File to Original Output File.\n");
		
		HuffmanDecompressor dc = new HuffmanDecompressor();
		
		String originalText = dc.decode("src/main/resources/output.dat");
		dc.fileWriter(originalText, "src/main/resources/output.txt");
	}
}
