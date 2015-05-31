package com.geag.ocr;

import java.awt.image.BufferedImage;
import java.io.File;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class Tess {

	public String performOcrOperationFromBufferedImage(BufferedImage img) {

		String result = "";
		try {
			ITesseract instance = new Tesseract(); // JNA Interface Mapping

			result = instance.doOCR(img);
			
		} catch (TesseractException e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return result;
	}
	
	public String performOcrOperationFromUrl(String imageUrl) {

		File imageFile = null;
		String result = "";
		try {

			//URL url = new URL(imageUrl);
			//BufferedImage img = ImageIO.read(url);
			//imageFile = new File("downloaded.png");
			//ImageIO.write(img, "png", imageFile);
			imageFile = new File(imageUrl);
			ITesseract instance = new Tesseract(); // JNA Interface Mapping
			// ITesseract instance = new Tesseract1(); // JNA Direct Mapping

			long a1 = System.currentTimeMillis();

			result = instance.doOCR(imageFile);
			
			long a2 = System.currentTimeMillis();

			long diff = a2 - a1;
			System.out.println(result);
		} catch (TesseractException e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return result;
	}

	public static void main(String[] args) {
		Tess tess = new Tess();
		// String result =
		// tess.performOcrOperation("https://owl.english.purdue.edu/media/jpeg/20071219011328_705.jpg");
		// String result = tess.performOcrOperation("C:/eurotext.png");
		String result = tess
				.performOcrOperationFromUrl("C:/eurotext.png");

		System.out.println(result);
	}

}
