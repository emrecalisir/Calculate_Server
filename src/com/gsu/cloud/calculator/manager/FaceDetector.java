package com.gsu.cloud.calculator.manager;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.core.MatOfRect;

public class FaceDetector {

	public static void detect(String tmp) {
		try {
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			
			File input = new File(
					"C:\\Users\\emre\\Desktop\\cappadocia_balloon.jpg");
			BufferedImage image = ImageIO.read(input);

			byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer())
					.getData();
			Mat mat = new Mat(image.getHeight(), image.getWidth(),
					CvType.CV_8UC3);
			mat.put(0, 0, data);

			Mat mat1 = new Mat(image.getHeight(), image.getWidth(),
					CvType.CV_8UC1);
			Imgproc.cvtColor(mat, mat1, Imgproc.COLOR_RGB2GRAY);

			byte[] data1 = new byte[mat1.rows() * mat1.cols()
					* (int) (mat1.elemSize())];
			mat1.get(0, 0, data1);
			BufferedImage image1 = new BufferedImage(mat1.cols(), mat1.rows(),
					BufferedImage.TYPE_BYTE_GRAY);
			image1.getRaster().setDataElements(0, 0, mat1.cols(), mat1.rows(),
					data1);

			File ouptut = new File("C:\\Users\\emre\\Desktop\\grayscale.jpg");
			ImageIO.write(image1, "jpg", ouptut);
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}

	}

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.out.println("\nRunning FaceDetector");
		URL url =FaceDetector.class.getResource("/detection_conf.xml");
		CascadeClassifier faceDetector = new CascadeClassifier(url.getPath().substring(1));
		URL urlImage = FaceDetector.class.getResource("/aile_terapisi.jpg");
		Mat image = Highgui.imread(urlImage.getPath().substring(1));

		MatOfRect faceDetections = new MatOfRect();
		faceDetector.detectMultiScale(image, faceDetections);

		System.out.println(String.format("Detected %s faces",
				faceDetections.toArray().length));

		for (Rect rect : faceDetections.toArray()) {
			Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x
					+ rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
		}

		String filename = "ouput1.png";
		System.out.println(String.format("Writing %s", filename));
		Highgui.imwrite(filename, image);
	}

}