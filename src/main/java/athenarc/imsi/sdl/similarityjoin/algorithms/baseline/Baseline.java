package athenarc.imsi.sdl.similarityjoin.algorithms.baseline;

import athenarc.imsi.sdl.similarityjoin.algorithms.SimilarityJoinAlgorithm;
import athenarc.imsi.sdl.similarityjoin.algorithms.relationmatrix.RelationMatrix;
import athenarc.imsi.sdl.similarityjoin.algorithms.SimilarityMeasure;
import athenarc.imsi.sdl.similarityjoin.algorithms.relationmatrix.SparseVector;
import athenarc.imsi.sdl.similarityjoin.algorithms.topk.TopKQueue;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Baseline extends SimilarityJoinAlgorithm {

    private long topKTime = 0;

    @Override
    public TopKQueue execute() {
//        int[][] matrix = super.convertRelationMatrix();

        TopKQueue topK = computeTopK(super.getRelationMatrix());

        return topK;
    }

    @Override
    public TopKQueue executeSearch(int authorId) {
        return null;
    }

    private TopKQueue computeTopK(RelationMatrix relationMatrix) {
        long curTime = System.currentTimeMillis();
        TopKQueue topK = new TopKQueue(super.getK());

        for (int i=0; i<relationMatrix.getRowsLength(); i++) {
            SparseVector row = relationMatrix.getRow(i);
            double rowNorm = row.norm2();
//            int[] row = m[i];
//            double rowNorm = 0.0;
//            for (int l=0; l<row.length; l++) {
//                rowNorm += row[l] * row[l];
//            }
//            rowNorm = Math.sqrt(rowNorm);

            for (int j=i+1; j<relationMatrix.getRowsLength(); j++) {
                SparseVector innerRow = relationMatrix.getRow(j);
//                int[] innerRow = m[j];
//
                double similarity = SimilarityMeasure.calculate(row, innerRow, rowNorm, super.getSimilarityMeasure());
//            System.out.println(similarity);
                if (!Double.isNaN(similarity)) {
                    if (topK.check(similarity)) {
                        topK.add(i, j, similarity);
                    }
                }
            }
//            break;
        }

        topKTime = System.currentTimeMillis() - curTime;

        return topK;
    }

    public long getTopKTime() {
        return topKTime;
    }
}
