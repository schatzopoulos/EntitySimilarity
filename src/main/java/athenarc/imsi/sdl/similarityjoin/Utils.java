package athenarc.imsi.sdl.similarityjoin;


import com.googlecode.javaewah.datastructure.BitSet;

public class Utils {

    // calculates hamming distance by counting the non-zero bits of the xor
    public static int hammingDistance(BitSet x, BitSet y) {
        return x.xorcardinality(y);
    }

    public static void writeProgress(double progress, String stage) {
        System.out.println("Similarity Join\t" + stage + "\t" + progress);
    }
}
