package com.gsu.cloud.calculator.api;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;

import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.objdetect.CascadeClassifier;

@Path("/imageChanger")
public class ImageChanger {

	int numberOfFacesDetected = 0;

	@POST
	@Path("/post")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String ImageChange(String imageContentData) {

		imageContentData = imageContentData.replace("imageContentData=", "");
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		CascadeClassifier faceDetector = new CascadeClassifier(
				"C:/opencv-2.4.9/sources/data/haarcascades/haarcascade_frontalface_alt.xml");

		System.out.println(imageContentData);
		byte[] decodeImage;
		String stringImage = null;
		char[] charArray;
		byte[] decodeHex;
		try {
			charArray = imageContentData.toCharArray();
			decodeHex = Hex.decodeHex(charArray);
			decodeImage = Base64.decodeBase64(decodeHex);

			Mat receivedMatImage2 = decodeToMat(decodeImage);

			MatOfRect faceDetections = new MatOfRect();
			faceDetector.detectMultiScale(receivedMatImage2, faceDetections);

			System.out.println(String.format("Detected %s faces",
					faceDetections.toArray().length));

			for (Rect rect : faceDetections.toArray()) {
				Core.rectangle(receivedMatImage2, new Point(rect.x, rect.y),
						new Point(rect.x + rect.width, rect.y + rect.height),
						new Scalar(0, 255, 0));
			}

			String filename = "output22.png";
			System.out.println(String.format("Writing %s", filename));
			Highgui.imwrite(filename, receivedMatImage2);

		} catch (DecoderException ex) {
			ex.printStackTrace();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stringImage;
	}

	public static Mat decodeToMat(byte[] imageByte) {
		BufferedImage image = null;
		Mat mat = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
			image = ImageIO.read(bis);
			ImageIO.write(image, "jpg", new File("c:/new-darksouls.jpg"));
			byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer())
					.getData();
			mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
			mat.put(0, 0, data);

		} catch (Exception ex) {

		}
		return mat;

	}

	public static String encodeImage(byte[] imageByteArray) {
		return new String(Base64.encodeBase64(imageByteArray));
	}

	public String encodeMatToString(Mat imageToEncode) {

		String image_str = null;
		// ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {

			byte[] data = new byte[imageToEncode.rows() * imageToEncode.cols()
					* (int) (imageToEncode.elemSize())];

			image_str = encodeImage(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return image_str;
	}

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
					.digit(s.charAt(i + 1), 16));
		}
		return data;
	}
}
