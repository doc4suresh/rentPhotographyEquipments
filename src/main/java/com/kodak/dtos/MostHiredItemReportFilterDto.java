package com.kodak.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MostHiredItemReportFilterDto {
	private int year;
	private String month;
	private List<Integer> yearList;
}
