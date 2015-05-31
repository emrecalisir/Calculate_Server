package com.geag.opencv;

import org.opencv.core.Core;

public class GeagOpenCvInitializer {
	boolean isInitialRequest = true;

	public void initOpenCv() {
		try {

			if (isInitialRequest) {
				System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
				isInitialRequest = false;
			}

		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
