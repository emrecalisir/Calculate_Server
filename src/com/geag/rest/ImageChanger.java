/*
 * Author: Emre CALISIR
 * Graduate Thesis: Mobile Cloud Computing - Performance and Efficiency Analysis on Local Cloudlets
 * Galatasaray University Computer Science, 2015
 */

package com.geag.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;

import com.geag.opencv.GeagFaceDetector;

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