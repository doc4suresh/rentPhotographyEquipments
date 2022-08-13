package com.kodak.utility;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FilePath {

	@Value("${image-file-path}")
	private String propImgFilePath;

	private static String imgFilePath;

	/**
	 * application.properties not read when JVM load classes. Because of that we
	 * need to add @PostConstruct and assign the path to static variable for latter
	 * use.
	 */
	@PostConstruct
	private void setPath() {
		this.imgFilePath = propImgFilePath;
	}

	public static String getNicFilePath() {
		return imgFilePath + "/nic/";
	}
	
	public static String getItemImgPath() {
		return imgFilePath + "/items/";
	}
}
