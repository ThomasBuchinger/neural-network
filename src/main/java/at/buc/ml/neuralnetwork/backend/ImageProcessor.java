package at.buc.ml.neuralnetwork.backend;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ImageProcessor {
	public static BufferedImage getFromURL(String dataurl) throws IOException {
		String base64Image = dataurl.split(",")[1];
		byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);

		BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));

		return img;
	}

	public static BufferedImage resize2gray(BufferedImage img, int w, int h) {
		Image tmp = img.getScaledInstance(w, h, BufferedImage.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
		ColorConvertOp op = new ColorConvertOp(cs, null);
		BufferedImage grayimg = op.filter(dimg, null);

		return grayimg;

	}

	public static String encode2base64(BufferedImage img) {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();

		try {
			ImageIO.write(img, "png", os);
			return Base64.getEncoder().encodeToString(os.toByteArray());
		} catch (final IOException ioe) {
			throw new UncheckedIOException(ioe);
		}
	}

	public static int[] ConvertToIntensitiyScale(BufferedImage img) {
		int color, alpha;
		int width=img.getWidth(), height=img.getHeight();
		int [] values = new int[width*height];
		int mask= 0xFF;
		for (int h = 0; h < height; h++) {
			for (int w = 0; w < width; w++) {
				 color = img.getRGB(w, h);
				 alpha = (color >>> 24) & mask;
				values [(h*height)+w]=alpha;
			}
		}
		return values;
	}
}
