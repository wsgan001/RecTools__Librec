package net.librec.recommender;

import net.librec.common.LibrecException;
import net.librec.eval.Measure;
import net.librec.eval.RecommenderEvaluator;
import net.librec.math.structure.DenseMatrix;
import net.librec.math.structure.MatrixEntry;
import net.librec.math.structure.SparseMatrix;
import net.librec.recommender.item.RecommendedItemList;
import net.librec.recommender.item.RecommendedList;
import net.librec.util.DriverClassUtil;
import net.librec.util.ReflectionUtil;
import org.apache.commons.lang.ArrayUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.DoubleToLongFunction;

/**
 * Matrix Factorization Recommender
 * Methods with user factors and item factors: such as SVD(Singular Value Decomposition)
 * <p>
 * Created by Keqiang Wang
 */
public abstract class MatrixFactorizationRecommender extends AbstractRecommender {
    /**
     * learn rate, maximum learning rate
     */
    protected float learnRate, maxLearnRate;

    /**
     * user latent factors
     */
    protected DenseMatrix userFactors;

    /**
     * item latent factors
     */
    protected DenseMatrix itemFactors;

    /**
     * the number of latent factors;
     */
    protected int numFactors;

    /**
     * the number of iterations
     */
    protected int numIterations;

    /**
     * init mean
     */
    protected float initMean;

    /**
     * init standard deviation
     */
    protected float initStd;

    /**
     * user regularization
     */
    protected float regUser;

    /**
     * item regularization
     */
    protected float regItem;


//    protected RecommendedList recommendedList_iter_train;
//    protected RecommendedList recommendedList_iter_validate;
//    protected RecommendedList recommendedList_iter_test;

    /**
     * setup
     * init member method
     *
     * @throws LibrecException if error occurs during setting up
     */
    protected void setup() throws LibrecException {
        super.setup();
        numIterations = conf.getInt("rec.iterator.maximum",100);
        learnRate = conf.getFloat("rec.iterator.learnrate", 0.01f);
        maxLearnRate = conf.getFloat("rec.iterator.learnrate.maximum", 1000.0f);

        regUser = conf.getFloat("rec.user.regularization", 0.01f);
        regItem = conf.getFloat("rec.item.regularization", 0.01f);

        numFactors = conf.getInt("rec.factor.number", 10);
        isBoldDriver = conf.getBoolean("rec.learnrate.bolddriver", false);
        decay = conf.getFloat("rec.learnrate.decay", 1.0f);

        userFactors = new DenseMatrix(numUsers, numFactors);
        itemFactors = new DenseMatrix(numItems, numFactors);

        initMean = 0.0f;
        initStd = 0.1f;

        // initialize factors
        userFactors.init(initMean, initStd);
        itemFactors.init(initMean, initStd);
    }

    /**
     * predict a specific rating for user userIdx on item itemIdx.
     *
     * @param userIdx user index
     * @param itemIdx item index
     * @return predictive rating for user userIdx on item itemIdx with bound
     * @throws LibrecException if error occurs during predicting
     */
    protected double predict(int userIdx, int itemIdx) throws LibrecException {
        return DenseMatrix.rowMult(userFactors, userIdx, itemFactors, itemIdx);
    }


    /**
     * Update current learning rate after each epoch <br>
     * <ol>
     * <li>bold driver: Gemulla et al., Large-scale matrix factorization with distributed stochastic gradient descent,
     * KDD 2011.</li>
     * <li>constant decay: Niu et al, Hogwild!: A lock-free approach to parallelizing stochastic gradient descent, NIPS
     * 2011.</li>
     * <li>Leon Bottou, Stochastic Gradient Descent Tricks</li>
     * <li>more ways to adapt learning rate can refer to: http://www.willamette.edu/~gorr/classes/cs449/momrate.html</li>
     * </ol>
     * @param iter the current iteration
     */
    protected void updateLRate(int iter) {
        if (learnRate < 0.0) {
            return;
        }

        if (isBoldDriver && iter > 1) {
            learnRate = Math.abs(lastLoss) > Math.abs(loss) ? learnRate * 1.05f : learnRate * 0.95f;
        } else if (decay > 0 && decay < 1) {
            learnRate *= decay;
        }

        // limit to max-learn-rate after update
        if (maxLearnRate > 0 && learnRate > maxLearnRate) {
            learnRate = maxLearnRate;
        }
        lastLoss = loss;

    }

    /**
     * Post each iteration, we do things:
     * <ol>
     * <li>print debug information</li>
     * <li>check if converged</li>
     * <li>if not, adjust learning rate</li>
     * </ol>
     * @param iter current iteration
     * @return boolean: true if it is converged; false otherwise
     * @throws LibrecException if error occurs
     */
    protected boolean isConverged(int iter) throws LibrecException{
        float delta_loss = (float) (lastLoss - loss);

        // print out debug info
        if (verbose) {
            String recName = getClass().getSimpleName().toString();
            String info = recName + " iter " + iter + ": loss = " + loss + ", delta_loss = " + delta_loss;
            //LOG.info(info);
        }

        if (Double.isNaN(loss) || Double.isInfinite(loss)) {
//            LOG.error("Loss = NaN or Infinity: current settings does not fit the recommender! Change the settings and try again!");
            throw new LibrecException("Loss = NaN or Infinity: current settings does not fit the recommender! Change the settings and try again!");
        }

        // check if converged
        boolean converged = Math.abs(loss) < 1e-5;
        lastLoss = loss;

        Evaluate_iter(iter);

        return converged;
    }

//    protected void Evaluate_iter(int iter) throws LibrecException{
//        Map<Measure.MeasureValue, Double> eval_train = null;
//        Map<Measure.MeasureValue, Double> eval_validate = null;
//        Map<Measure.MeasureValue, Double> eval_test = null;
//
//        recommendedList_iter_train = recommend_iter("train");
//        if (recommendedList_iter_train != null){
//            eval_train = evaluateMap_iter(recommendedList_iter_train, trainMatrix);
//            //print_eval_iter(eval_train, iter, "train");
//        }
//
//        recommendedList_iter_validate = recommend_iter("validate");
//        if (recommendedList_iter_validate != null){
//            eval_validate = evaluateMap_iter(recommendedList_iter_validate, validMatrix);
//            //print_eval_iter(eval_validate, iter, "validate");
//        }
//
//        recommendedList_iter_test = recommend_iter("test");
//        if (recommendedList_iter_test != null){
//            eval_test = evaluateMap_iter(recommendedList_iter_test, testMatrix);
//            //print_eval_iter(eval_test, iter, "test");
//        }
//
//        if (eval_train != null && eval_train.size() > 0) {
//            for (Map.Entry<Measure.MeasureValue, Double> entry : eval_train.entrySet()) {
//                String evalName = null;
//                if (entry != null && entry.getKey() != null) {
//                    Double validate_score = -1.0, test_score = -1.0, train_score = entry.getValue();
//                    if (eval_validate != null)
//                        validate_score = eval_validate.get(entry.getKey());
//                    if (eval_test != null)
//                        test_score = eval_test.get(entry.getKey());
//                    if (entry.getKey().getTopN() != null && entry.getKey().getTopN() > 0) {
//                        LOG.info("iter " + Integer.toString(iter) + " "
//                                + "Evaluator value:" + entry.getKey().getMeasure() + " top " + entry.getKey().getTopN() + " is " + train_score + "\t" + validate_score + "\t" + test_score);
//                    } else {
//                        LOG.info("iter " + Integer.toString(iter) + " "
//                                + "Evaluator value:" + entry.getKey().getMeasure() + " is " + train_score + "\t" + validate_score + "\t" + test_score);
//                    }
//                }
//            }
//        }
//    }
//
//    protected void print_eval_iter(Map<Measure.MeasureValue, Double> evalValueMap, int iter, String type) throws  LibrecException{
//        if (evalValueMap != null && evalValueMap.size() > 0) {
//            for (Map.Entry<Measure.MeasureValue, Double> entry : evalValueMap.entrySet()) {
//                String evalName = null;
//                if (entry != null && entry.getKey() != null) {
//                    if (entry.getKey().getTopN() != null && entry.getKey().getTopN() > 0) {
//                        LOG.info("iter " + Integer.toString(iter) + " " + type + " "
//                                + "Evaluator value:" + entry.getKey().getMeasure() + " top " + entry.getKey().getTopN() + " is " + entry.getValue());
//                    } else {
//                        LOG.info("iter " + Integer.toString(iter) + " " + type + " "
//                                + "Evaluator value:" + entry.getKey().getMeasure() + " is " + entry.getValue());
//                    }
//                }
//            }
//        }
//    }
//
//    protected RecommendedList recommend_iter(String type) throws LibrecException {
//        RecommendedList rl = null;
//        if (isRanking && topN > 0) {
//            rl = recommendRank();
//        } else {
//            rl = recommendRating_iter(type);
//        }
//        return rl;
//    }
//
//
//    protected Map<Measure.MeasureValue, Double> evaluateMap_iter(RecommendedList rl, SparseMatrix eval_matrix) throws LibrecException {
//        Map<Measure.MeasureValue, Double> evaluatedMap = new HashMap<>();
//        String[] evalClassKeys = conf.getStrings("rec.eval.classes");
//        List<Measure.MeasureValue> measureValueList = Measure.getMeasureEnumList(isRanking, topN);
//        if (measureValueList != null) {
//            for (Measure.MeasureValue measureValue : measureValueList) {
//                if (evalClassKeys!= null && evalClassKeys.length > 0)
//                {
//                    if (!ArrayUtils.contains(evalClassKeys, measureValue.getMeasure().name().toLowerCase()))
//                        continue;
//                }
//                RecommenderEvaluator evaluator = ReflectionUtil
//                        .newInstance(measureValue.getMeasure().getEvaluatorClass());
//                if (isRanking && measureValue.getTopN() != null && measureValue.getTopN() > 0) {
//                    evaluator.setTopN(measureValue.getTopN());
//                }
//                double evaluatedValue = evaluator.evaluate(context, rl, eval_matrix);
//                evaluatedMap.put(measureValue, evaluatedValue);
//            }
//        }
//        return evaluatedMap;
//    }
//
//    protected RecommendedList recommendRating_iter(String type) throws LibrecException {
//        RecommendedList rl = new RecommendedItemList(numUsers - 1, numUsers);
//
//        SparseMatrix target = testMatrix;
//        if (type.equals("test")){
//            target = testMatrix;
//        }
//        else if (type.equals("train")){
//            target = trainMatrix;
//        }
//        if (type.equals("validate")){
//            target = validMatrix;
//        }
//        if (target == null)
//            return null;
//
//        for (MatrixEntry matrixEntry : target) {
//            int userIdx = matrixEntry.row();
//            int itemIdx = matrixEntry.column();
//            double predictRating = predict(userIdx, itemIdx, true);
//            if (Double.isNaN(predictRating)) {
//                predictRating = globalMean;
//            }
//            rl.addUserItemIdx(userIdx, itemIdx, predictRating);
//        }
//
//        return rl;
//    }
}
