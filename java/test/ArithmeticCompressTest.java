/* 
 * Reference arithmetic coding
 * Copyright (c) Project Nayuki
 * 
 * https://www.nayuki.io/page/reference-arithmetic-coding
 * https://github.com/nayuki/Reference-arithmetic-coding
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class ArithmeticCompressTest extends ArithmeticCodingTest {
	
	protected byte[] compress(byte[] b) throws IOException {
		InputStream in = new ByteArrayInputStream(b);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		BitOutputStream bitOut = new BitOutputStream(out);
		
		FrequencyTable freqs = getFrequencies(b);
		ArithmeticCompress.writeFrequencies(bitOut, freqs);
		ArithmeticCompress.compress(freqs, in, bitOut);
		bitOut.close();
		return out.toByteArray();
	}
	
	
	protected byte[] decompress(byte[] b) throws IOException {
		InputStream in = new ByteArrayInputStream(b);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		BitInputStream bitIn = new BitInputStream(in);
		
		FrequencyTable freqs = ArithmeticDecompress.readFrequencies(bitIn);
		ArithmeticDecompress.decompress(freqs, bitIn, out);
		return out.toByteArray();
	}
	
	
	private static FrequencyTable getFrequencies(byte[] b) {
		FrequencyTable freqs = new SimpleFrequencyTable(new int[257]);
		for (byte x : b)
			freqs.increment(x & 0xFF);
		freqs.increment(256);  // EOF symbol gets a frequency of 1
		return freqs;
	}
	
}