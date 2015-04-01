/*
 * Author: Emre CALISIR
 * Graduate Thesis: Mobile Cloud Computing - Performance and Efficiency Analysis on Local Cloudlets
 * Galatasaray University Computer Science, 2015
 */

package com.geag.rest;

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
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.objdetect.CascadeClassifier;

import com.geag.opencv.GeagFaceDetector;
import com.gsu.cloud.calculator.model.RectangleFace;

@Path("/imageChanger")
public class ImageChanger {

	@POST
	@Path("/post")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response ImageChange(String imageContentData) {
		JSONObject obj = null;
		JSONArray jsonArray = new JSONArray();
		Long bStartTime, bEndTime = 0L;
		int numberOfFacesDetected = 0;

		bStartTime = System.currentTimeMillis();
		System.out.println("bStartTime: " + bStartTime);

		GeagFaceDetector geagFaceDetector = new GeagFaceDetector();
		
		try {
			MatOfRect faceDetections = geagFaceDetector.detectFaces(imageContentData);

			numberOfFacesDetected = faceDetections.toArray().length;

			for (Rect rect : faceDetections.toArray()) {
				obj = new JSONObject();
				obj.put("x1", rect.x);
				obj.put("x2", rect.x + rect.width);
				obj.put("y1", rect.y);
				obj.put("y2", rect.y + rect.height);
				jsonArray.put(obj);
			}

			obj = new JSONObject();
			bEndTime = System.currentTimeMillis();
			obj.put("serverTimeLasted", (bEndTime - bStartTime));
			jsonArray.put(obj);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		System.out.println("Number of faces: " + numberOfFacesDetected + " in "
				+ (bEndTime - bStartTime) + " ms");

		return Response.status(200).entity(jsonArray).build();
	}

	


}