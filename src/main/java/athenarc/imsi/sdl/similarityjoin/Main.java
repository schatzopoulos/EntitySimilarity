package athenarc.imsi.sdl.similarityjoin;

import java.io.IOException;

import athenarc.imsi.sdl.similarityjoin.algorithms.SimilarityJoinAlgorithm;
import athenarc.imsi.sdl.similarityjoin.algorithms.SimilarityMeasure;
import athenarc.imsi.sdl.similarityjoin.algorithms.lshbased.PSJoin;
import athenarc.imsi.sdl.similarityjoin.algorithms.topk.TopKQueue;

public class Main {
    public static void main(String [] args) {
        
        String inputfile = "/opt/workflows/data/out/HIN.csv";
        String outfile = "/opt/workflows/data/out/TMP.csv";
        int t = 1;
        int w = 0;
        int minValues = 5;

        SimilarityJoinAlgorithm algorithm = new PSJoin(t, w, minValues);

        int k = 100;

        algorithm.setK(k);
        algorithm.setSimilarityMeasure(SimilarityMeasure.TYPE.JOIN_SIM);

        algorithm.readRelationMatrix(inputfile);

        // algorithm.printRelationMatrix();

       TopKQueue topK = algorithm.execute();

       Utils.writeProgress(4, "Writing Results");
       try {
            topK.write(outfile);
       } catch (IOException e) {
           System.out.println(e);
       }
    }
}
