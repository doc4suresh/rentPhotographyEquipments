package com.kodak.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BranchDto {
	private long id;
	private String name;
	private String address;
	private String phoneNo;
	private String manager;

}
