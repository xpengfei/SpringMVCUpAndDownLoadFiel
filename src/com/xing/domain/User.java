package com.xing.domain;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

/**
 *@author xpengfei
 *@creat  9:32:35 PM   Sep 17, 2017
 */
public class User implements Serializable{

	private String username;
	private MultipartFile image;

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}
}
