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
        if (args.length != 2) {
            System.out.println("Usage: java -jar EntitySimilarity.jar -c <json config file>");
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

        String inputfile = (String) config.get("hin_out");
        String outfile = (String) config.get("analysis_out");
        String operation = (String) config.get("operation");
        int k = ((Long)config.get("k")).intValue();
        int t = ((Long)config.get("t")).intValue();
        int w = ((Long)config.get("w")).intValue();
        int minValues = ((Long)config.get("min_values")).intValue();

        SimilarityJoinAlgorithm algorithm = new PSJoin(t, w, minValues);

        algorithm.setK(k);
        algorithm.setSimilarityMeasure(SimilarityMeasure.TYPE.JOIN_SIM);

        algorithm.readRelationMatrix(inputfile);

        // algorithm.printRelationMatrix();
        TopKQueue topK = null;
        if (operation.equals("join")) {
            topK = algorithm.execute();
        } else if (operation.equals("search")) {
            int targetId = ((Long)config.get("target_id")).intValue();
            topK = algorithm.executeSearch(targetId);
        } else {
            System.out.println("No supported operation");
            System.exit(2);
        }

        Utils.writeProgress(4, "Writing Results");

        try {
            topK.write(outfile);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
