package com.kodak.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfitRentReportDto {
	private String cusName;
	private List<String> cusNameList;
	private String startDate;
	private String endDate;
}
