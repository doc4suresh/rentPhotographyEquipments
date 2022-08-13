package com.kodak.dtos;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDto {

	private Long id;
	private String firstName;
	private String lastName;
	private String nic;
	private MultipartFile nicFaceImg;
	private MultipartFile nicBackImg;
	private String mobile;
	private String mobile2;
	private String address;
	private String billingAddress;

}
