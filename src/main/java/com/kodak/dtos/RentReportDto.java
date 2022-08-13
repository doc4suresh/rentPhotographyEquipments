package com.kodak.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RentReportDto {
	private String username;
	private List<String> usernameList;
	private String cusName;
	private List<String> cusNameList;
	private String startDate;
	private String endDate;
	private String status;
	private List<String> statusList;
}
