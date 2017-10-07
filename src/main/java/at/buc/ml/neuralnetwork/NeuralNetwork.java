package at.buc.ml.neuralnetwork;


import java.io.IOException;

import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NeuralNetwork {

    private static Logger log = LoggerFactory.getLogger(NeuralNetwork.class);
	
    private  MultiLayerNetwork model=null;
    
    int batchSize = 128; // batch size for each epoch
    int rngSeed = 123; // random number seed for reproducibility
    int numEpochs = 1; // number of epochs to perform
    final int numRows = 28;
    final int numColumns = 28;
    int outputNum = 10; // number of output classes
    
    
	public NeuralNetwork() {
        
        log.info("Build model....");
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(rngSeed) //include a random seed for reproducibility
                // use stochastic gradient descent as an optimization algorithm
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .iterations(1)
                .learningRate(0.006) //specify the learning rate
                .updater(Updater.NESTEROVS)
                .regularization(true).l2(1e-4)
                .list()
                .layer(0, new DenseLayer.Builder() //create the first, input layer with xavier initialization
                        .nIn(numRows * numColumns)
                        .nOut(100)
                        .activation(Activation.RELU)
                        .weightInit(WeightInit.XAVIER)
                        .build())
                .layer(1, new DenseLayer.Builder()
                		.nIn(100)
                		.nOut(100)
                		.activation(Activation.RELU)
                		.weightInit(WeightInit.XAVIER)
                		.build())
                .layer(2, new DenseLayer.Builder()
                		.nIn(100)
                		.nOut(100)
                		.activation(Activation.RELU)
                		.weightInit(WeightInit.XAVIER)
                		.build())
                .layer(3, new OutputLayer.Builder(LossFunction.NEGATIVELOGLIKELIHOOD) //create hidden layer
                        .nIn(100)
                        .nOut(outputNum)
                        .activation(Activation.SOFTMAX)
                        .weightInit(WeightInit.XAVIER)
                        .build())
                .pretrain(false).backprop(true) //use backpropagation to adjust weights
                .build();

        model = new MultiLayerNetwork(conf);
        model.init();
        //print the score with every 10 iteration
        model.setListeners(new ScoreIterationListener(10));

     
	
	}
	
	public void trainWithMnist(int epochs) {
		
		 //Get the DataSetIterators:
        DataSetIterator mnistTrain = null , mnistTest = null;
        try {
        	mnistTrain = new MnistDataSetIterator(batchSize, true, rngSeed);
			mnistTest = new MnistDataSetIterator(batchSize, false, rngSeed);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        train(mnistTrain, epochs);
        eval(mnistTest, outputNum);
	}
	
	public void train(DataSetIterator trainingSet, int numEpochs) {
		   log.info("Train model....");
	        for( int i=0; i<numEpochs; i++ ){
	            model.fit(trainingSet);
	        }
	}
	
	public void eval(DataSetIterator testSet, int outputNum) {
		log.info("Evaluate model....");
        Evaluation eval = new Evaluation(outputNum); //create an evaluation object with 10 possible classes
        while(testSet.hasNext()){
            DataSet next = testSet.next();
            INDArray output = model.output(next.getFeatureMatrix()); //get the networks prediction
            eval.eval(next.getLabels(), output); //check the prediction against the true class
        }

        log.info(eval.stats());
        log.info("****************Example finished********************");
	}
	
	public double[] predict_single(INDArray input) {
		INDArray output=model.output(input);
		double[] ret=new double[outputNum];
		for (int i = 0; i < ret.length; i++) {
			ret[i]=output.getDouble(i);
		}
		
		return ret;
	}
	
}
