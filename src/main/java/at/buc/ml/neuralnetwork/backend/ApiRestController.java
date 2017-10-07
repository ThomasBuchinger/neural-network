package at.buc.ml.neuralnetwork.backend;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.collection.CollectionRecordReader;
import org.datavec.api.writable.Writable;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.buc.ml.neuralnetwork.ImageRecord;
import at.buc.ml.neuralnetwork.NeuralNetwork;

@RestController
public class ApiRestController {
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	
	public static final int numEpochs=10;
	
	@Autowired
	NeuralNetwork nn;

	@PostConstruct
	private void setup() {
		nn.trainWithMnist(numEpochs);
		
	}
	
	
	@RequestMapping("/data")
	public Object callback_data(@RequestBody String data) throws IOException {
		BufferedImage img = ImageProcessor.getFromURL(data);
		System.out.println("------------------------------------------------------------------------------------");
		img = ImageProcessor.resize2gray(img, 28, 28);
		int[] values = ImageProcessor.ConvertToIntensitiyScale(img);
		INDArray ds = Nd4j.zeros(1, 28 * 28);
		for (int i = 0; i < values.length; i++) {
			ds.putScalar(new int[] { 0, i }, values[i]);
		}
		double[] prediction = nn.predict_single(ds.divi(255));
		print_prediction(prediction);
		
		
		return new ResponseObject(data, prediction);
	}

	private void print_prediction(double[] output) {
		System.out.print("Result: \t");
		String color;
		for (int i = 0; i < output.length; i++) {
			if (output[i] <= 0.1) {
				color = ANSI_BLACK;
			} else if (output[i] < 0.5) {
				color = ANSI_GREEN;
			} else if (output[i] <= .075) {
				color = ANSI_YELLOW;
			} else {
				color = ANSI_RED;
			}
			System.out.printf("%s%d=%.2f%%%s ", color, i, output[i], ANSI_RESET);
		}
		System.out.println();
	}
	
	class ResponseObject {
		
		double [] prediction;
		String img;
		
		public ResponseObject(String img, double[] prediction) {
			this.img=img;
			this.prediction=prediction;
		}

		public double[] getPrediction() {
			return prediction;
		}

		public void setPrediction(double[] prediction) {
			this.prediction = prediction;
		}

		public String getImg() {
			return img;
		}

		public void setImg(String img) {
			this.img = img;
		}
		
	}
}
