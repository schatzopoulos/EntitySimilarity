package athenarc.imsi.sdl.similarityjoin;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import athenarc.imsi.sdl.similarityjoin.algorithms.SimilarityJoinAlgorithm;
import athenarc.imsi.sdl.similarityjoin.algorithms.SimilarityMeasure;
import athenarc.imsi.sdl.similarityjoin.algorithms.lshbased.PSJoin;
import athenarc.imsi.sdl.similarityjoin.algorithms.topk.TopKQueue;

public class Main {
    public static void main(String [] args) {

        // check params
        if (args.length != 4) {
            System.out.println("Usage: java -jar EntitySimilarity.jar -c <json config file> <analysis> <hin_in>");
            System.exit(1);
        }

        // read config json
        JSONObject config = null;
        try {
            JSONParser parser = new JSONParser();
            
             config = (JSONObject) parser.parse(new FileReader(args[1]));

        } catch (Exception e) {
            System.out.println(e);
            System.exit(1);
        }

        String analysis = (String) args[2];
        String inputfile = (String) args[3];

        if (!analysis.equals("Similarity Join") && !analysis.equals("Similarity Search")) {
            System.exit(-1);
        }

        int t = ((Long)config.get("t")).intValue();
        int w = -1;
        int k = -1;
        int minValues = -1;
        
        if (analysis.equals("Similarity Search")) {
            k = ((Long)config.get("searchK")).intValue();
            w = ((Long)config.get("searchW")).intValue();
            minValues = ((Long)config.get("searchMinValues")).intValue();
        } else {
            k = ((Long)config.get("joinK")).intValue();
            w = ((Long)config.get("joinW")).intValue();
            minValues = ((Long)config.get("joinMinValues")).intValue();        
        }

        SimilarityJoinAlgorithm algorithm = new PSJoin(analysis, t, w, minValues);
        algorithm.setK(k);
        algorithm.setSimilarityMeasure(SimilarityMeasure.TYPE.JOIN_SIM);

        algorithm.readRelationMatrix(inputfile);

        // algorithm.printRelationMatrix();
        TopKQueue topK = null;
        String outfile = null;

        if (analysis.equals("Similarity Join")) {
            topK = algorithm.execute();
            outfile = (String) config.get("sim_join_out");
        } else if (analysis.equals("Similarity Search")) {
            int targetId = ((Long)config.get("target_id")).intValue();
            topK = algorithm.executeSearch(targetId);
            outfile = (String) config.get("sim_search_out");
        }

        Utils.writeProgress(analysis, 3, "Writing Results");
        
        try {
            topK.write(outfile);
        } catch (IOException e) {
            System.out.println(e);
        }  
    }
}
