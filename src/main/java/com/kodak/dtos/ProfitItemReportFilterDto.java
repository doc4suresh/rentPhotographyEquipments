package com.kodak.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfitItemReportFilterDto {
	private int year;
	private String itemName;
	private List<Integer> yearList;
	private List<String> itemNameList;
}
