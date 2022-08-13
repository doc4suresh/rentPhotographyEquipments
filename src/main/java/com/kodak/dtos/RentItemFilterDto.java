package com.kodak.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RentItemFilterDto {
	private boolean itemPage;
	private String category;
	private String brand;
	private String stock;
}
