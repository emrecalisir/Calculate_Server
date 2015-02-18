package com.gsu.cloud.calculator.api;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;

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

import com.gsu.cloud.calculator.manager.FaceDetector;

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
		//CascadeClassifier cascade1 = null;
		//cascade1.load("C:/opencv2.4.9/sources/data/haarcascades/haarcascade_frontalface_alt.xml");
		
		//URL url =FaceDetector.class.getResource("/detection_conf.xml");
		//CascadeClassifier faceDetector = new CascadeClassifier(url.getPath().substring(1));
		
		CascadeClassifier faceDetector = new CascadeClassifier("C:/opencv-2.4.9/sources/data/haarcascades/haarcascade_frontalface_alt.xml"); // With absolute location.
		
        //CascadeClassifier faceDetector = new CascadeClassifier(ImageChanger.class.getResource("haarcascade_frontalface_alt.xml").getPath());

		System.out.println(imageContentData);
		byte[] bytes = imageContentData.getBytes();
		byte[] decodeImage;
		byte[] decodeImage2;
		String stringImage = null;
		char[] charArray;
		byte[] decodeHex;
		try {
			//decodeImage = Base64.decodeBase64(bytes);
			charArray= imageContentData.toCharArray();
			decodeHex = Hex.decodeHex(charArray);
			decodeImage = Base64.decodeBase64(decodeHex);	
		
		
			//decodeImage = Hex.decodeHex(imageContentData.toCharArray());
			//decodeImage2 = Base64.decodeBase64(decodeImage);
		// converting row data to image that is sent from android to server
		
		
//		Mat receivedMatImage = new Mat(1, 100, CvType.CV_8UC1);	
			
		Mat receivedMatImage2 = decodeToMat(decodeImage);

		
	    MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(receivedMatImage2, faceDetections);
		
        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));

        for (Rect rect : faceDetections.toArray()) {
            Core.rectangle(receivedMatImage2, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 255, 0));
        }

        String filename = "output22.png";
        System.out.println(String.format("Writing %s", filename));
        Highgui.imwrite(filename, receivedMatImage2);
        
        /*
		// image processing operation will be done.
		Mat processedMatImage = detectFaces(receivedMatImage);

		makeImageFile(processedMatImage);
*/
		//stringImage = encodeMatToString(processedMatImage);
		} 
		catch(DecoderException ex){
			ex.printStackTrace();
				
		}
		catch (Exception e) {
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
			mat = new Mat(image.getHeight(), image.getWidth(),
					CvType.CV_8UC3);
			mat.put(0, 0, data);

		} catch (Exception ex) {

		}
		return mat;

	}

	public static String encodeImage(byte[] imageByteArray) {
		return new String(Base64.encodeBase64(imageByteArray));
	}
/*
	public static byte[] decodeImage(String imageDataString) {
		if(imageDataString.isEmpty()) {
			return null;
		}
		System.out.println(imageDataString);
		byte[] decodeBase64 = null ;
		//byte[] imageData = imageDataString.getBytes();
		try {
			//byte[] bytes = imageDataString.getBytes();
			System.out.println("byte array=" + new String(bytes));
			
			//decodeBase64 = Base64.decodeBase64(bytes);
			//System.out.println("byte array=" + new String(decodeBase64));
			//System.out.println("String=" + imageDataString);
			//imageByte = Base64.decodeBase64(imageData); 
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}

	    return decodeBase64;
	}
*/
	// operation of face detection receives as input Mat and output is Mat
	private Mat detectFaces(Mat receivedMatImage) {
		System.out.println("\nRunning FaceDetector");
		URL url = FaceDetector.class.getResource("/detection_conf.xml");
		CascadeClassifier faceDetector = new CascadeClassifier(url.getPath()
				.substring(1));
		MatOfRect faceDetections = new MatOfRect();
		faceDetector.detectMultiScale(receivedMatImage, faceDetections);
		numberOfFacesDetected = faceDetections.toArray().length;
		System.out.println(String.format("Detected %s faces",
				numberOfFacesDetected));

		for (Rect rect : faceDetections.toArray()) {
			Core.rectangle(receivedMatImage, new Point(rect.x, rect.y),
					new Point(rect.x + rect.width, rect.y + rect.height),
					new Scalar(0, 255, 0));
		}
		return receivedMatImage;
	}

	private void makeImageFile(Mat imageToEncode){
		String image_str = null;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {

			byte[] data = new byte[imageToEncode.rows() * imageToEncode.cols()
					* (int) (imageToEncode.elemSize())];
			
			image_str = Base64.encodeBase64String(data);

			BufferedImage outputImage = new
			BufferedImage(imageToEncode.cols(), imageToEncode.rows(),
			BufferedImage.TYPE_INT_RGB);
			outputImage.getRaster().setDataElements(0, 0,
			imageToEncode.cols(), imageToEncode.rows(), data);

			File outputFile = new
			File("C:\\Users\\emre2\\Desktop\\deneme1.jpg");
			ImageIO.write(outputImage, "jpg", outputFile);

			outputStream.flush();
		}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
	public String encodeMatToString(Mat imageToEncode) {

		String image_str = null;
		//ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {

			byte[] data = new byte[imageToEncode.rows() * imageToEncode.cols()
					* (int) (imageToEncode.elemSize())];
			
			image_str = encodeImage(data);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return image_str;
	}

	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
}
