package at.buc.ml.neuralnetwork;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;

public class ImageRecord {
	public static DataSet getImageDataset(int[] values) {
		INDArray input=Nd4j.zeros(1, 28*28);
		for (int i = 0; i < values.length; i++) {
			input.putScalar(new int[] {0, i}, values[i]);
		}
		
		DataSet ds=new DataSet();
		ds.addFeatureVector(input);
		return ds;
	}
	
}
