/*
 * Author: Emre CALISIR
 * Graduate Thesis: Mobile Cloud Computing - Performance and Efficiency Analysis on Local Cloudlets
 * Galatasaray University Computer Science, 2015
 */

package com.gsu.cloud.calculator.api;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.objdetect.CascadeClassifier;

import com.gsu.cloud.calculator.data.RectangleFace;

@Path("/imageChanger")
public class ImageChanger {

	int numberOfFacesDetected = 0;
	boolean isInitialRequest = true;
	CascadeClassifier faceDetector = null;
	byte[] decodeImage;
	char[] charArray;
	byte[] decodeHex;
	List<RectangleFace> rectangleFaceList = null;
	String response = "";
	JSONObject obj = null;
	JSONArray jsonArray = null;
	Mat receivedMatImage2 = null;
	MatOfRect faceDetections = null;
	RectangleFace rectangleFace = null;
	Long bStartTime, bEndTime = 0L;

	@POST
	@Path("/post")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response ImageChange(String imageContentData) {

		bStartTime = System.currentTimeMillis();
		System.out.println("bStartTime: " + bStartTime);
		imageContentData = imageContentData.replace("imageContentData=", "");
		if (isInitialRequest) {

			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

			faceDetector = new CascadeClassifier(
					"C:/opencv-2.4.9/sources/data/haarcascades/haarcascade_frontalface_alt.xml");
			isInitialRequest = false;
		}

		rectangleFaceList = new ArrayList<RectangleFace>();
		jsonArray = new JSONArray();
		try {
			charArray = imageContentData.toCharArray();
			decodeHex = Hex.decodeHex(charArray);
			decodeImage = Base64.decodeBase64(decodeHex);
			/*
			 * BufferedImage image=ImageIO.read(new
			 * ByteArrayInputStream(decodeImage));
			 * System.out.println("initial size height: " + image.getHeight() +
			 * " width: " + image.getWidth());
			 */
			receivedMatImage2 = decodeToMat(decodeImage);

			faceDetections = new MatOfRect();
			faceDetector.detectMultiScale(receivedMatImage2, faceDetections);
			/*
			 * System.out.println(String.format("Detected %s faces",
			 * faceDetections.toArray().length));
			 */
			numberOfFacesDetected = faceDetections.toArray().length;

			for (Rect rect : faceDetections.toArray()) {

				obj = new JSONObject();
				obj.put("x1", rect.x);
				obj.put("x2", rect.x + rect.width);
				obj.put("y1", rect.y);
				obj.put("y2", rect.y + rect.height);
				jsonArray.put(obj);

				/*
				 * rectangleFace = new RectangleFace(rect.x, rect.x +
				 * rect.width, rect.y, rect.y + rect.height);
				 * rectangleFaceList.add(rectangleFace);
				 * 
				 * obj = new JSONObject(); obj.put("x1", rectangleFace.getX1());
				 * obj.put("x2", rectangleFace.getX2()); obj.put("y1",
				 * rectangleFace.getY1()); obj.put("y2", rectangleFace.getY2());
				 * jsonArray.put(obj);
				 */
			}
			bEndTime = System.currentTimeMillis();
			obj = new JSONObject();
			obj.put("serverTimeLasted", (bEndTime - bStartTime));
			jsonArray.put(obj);

		} catch (DecoderException e) {
			System.out.println(e);

		} catch (Exception e) {
			System.out.println(e);
		}

		System.out.println("Number of faces: " + (bEndTime - bStartTime)
				+ " ms");

		/*
		 * System.out.println("Number of faces: " + numberOfFacesDetected +
		 * ". Time lasted: [(" + bStartTime / 1000 + "," + bEndTime / 1000 +
		 * ")=" + ((bEndTime) - (bStartTime))+" ms ]");
		 */
		System.out.println("bEndTime: " + bEndTime);
		return Response.status(200).entity(jsonArray).build();
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
