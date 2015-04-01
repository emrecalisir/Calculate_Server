package com.geag.opencv;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.objdetect.CascadeClassifier;

public class GeagFaceDetector {
	public MatOfRect detectFaces(String imageContentData) {
		boolean isInitialRequest = true;
		CascadeClassifier faceDetector = null;
		byte[] decodeImage;
		char[] charArray;
		byte[] decodeHex;
		Mat receivedMatImage2 = null;
		MatOfRect faceDetections = null;
		imageContentData = imageContentData.replace("imageContentData=", "");
		try {

			if (isInitialRequest) {

				System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

				faceDetector = new CascadeClassifier(
						"C:/opencv-2.4.9/sources/data/haarcascades/haarcascade_frontalface_alt.xml");
				isInitialRequest = false;
			}

			charArray = imageContentData.toCharArray();
			decodeHex = Hex.decodeHex(charArray);
			decodeImage = Base64.decodeBase64(decodeHex);
			//decodeImage = Base64.decodeBase64(imageContentData.getBytes());
			
			receivedMatImage2 = decodeToMat(decodeImage);
			faceDetections = new MatOfRect();
			faceDetector.detectMultiScale(receivedMatImage2, faceDetections);

		} 
		/*catch (DecoderException e) {
			System.out.println(e);
		}
		*/ catch (Exception e) {
			System.out.println(e);
		}
		return faceDetections;

	}
	public static Mat decodeToMat(byte[] imageByte) {
		BufferedImage image = null;
		Mat mat = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
			image = ImageIO.read(bis);
			byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer())
					.getData();
			mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
			mat.put(0, 0, data);

		} catch (Exception ex) {

		}
		return mat;

	}
}
