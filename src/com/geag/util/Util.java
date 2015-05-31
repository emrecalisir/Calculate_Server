package com.geag.util;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

public class Util {

    private Random rand = new Random();

	public int randInt(int min, int max) {

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
	
	public Mat convertImageRowDataToMatrix(String imageData) {
		Mat mat = null;
		try {
			char[] charArray = imageData.toCharArray();
			byte[] decodeHex = Hex.decodeHex(charArray);
			byte[] decodeImage = Base64.decodeBase64(decodeHex);
			mat = decodeToMat(decodeImage);

		} catch (Exception e) {
			System.out.println(e);

		}
		return mat;
	}
	
	public BufferedImage convertImageRowDataToBufferedImage(String imageData) {
		BufferedImage img = null;

		try {
			char[] charArray = imageData.toCharArray();
			byte[] decodeHex = Hex.decodeHex(charArray);
			byte[] decodeImage = Base64.decodeBase64(decodeHex);
			InputStream in = new ByteArrayInputStream(decodeImage);
			img = ImageIO.read(in);

		} catch (Exception e) {
			System.out.println(e);

		}
		return img;
	}
	
	public Mat decodeToMat(byte[] imageByte) {
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
