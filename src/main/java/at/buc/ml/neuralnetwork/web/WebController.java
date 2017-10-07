package at.buc.ml.neuralnetwork.web;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import at.buc.ml.neuralnetwork.NeuralNetwork;
import at.buc.ml.neuralnetwork.backend.ImageProcessor;

@Controller
public class WebController {
	private static final double[] zeros = new double[10];
	

	@RequestMapping("/digits")
	public String digits(Model model) {
		model.addAttribute("img", null);
		model.addAttribute("prediction", new double[] {0.6,0,76});
		return "digits";
	}

	

}
