package com.geag.opencv;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.objdetect.CascadeClassifier;
import com.geag.util.Util;

public class GeagFaceDetector {

	public MatOfRect detectFaces(String imageContentData) {
		CascadeClassifier faceDetector = null;

		MatOfRect faceDetections = null;
		imageContentData = imageContentData.replace("imageContentData=", "");

		faceDetector = new CascadeClassifier(
				"C:/opencv-2.4.9/sources/data/haarcascades/haarcascade_frontalface_alt.xml");
		Util util = new Util();
		Mat mat = util.convertImageRowDataToMatrix(imageContentData);

		faceDetections = new MatOfRect();
		faceDetector.detectMultiScale(mat, faceDetections);

		return faceDetections;

	}

}
