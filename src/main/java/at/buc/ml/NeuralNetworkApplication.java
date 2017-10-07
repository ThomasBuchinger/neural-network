package at.buc.ml;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import at.buc.ml.neuralnetwork.NeuralNetwork;

@SpringBootApplication
public class NeuralNetworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(NeuralNetworkApplication.class, args);
	}
}
