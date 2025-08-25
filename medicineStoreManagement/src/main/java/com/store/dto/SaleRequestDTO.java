package com.store.dto;

import java.util.List;

import lombok.Data;

@Data
public class SaleRequestDTO {
	private String customerName;
    private int age;
    private String gender;
    private String mobileNo;
    private List<SaleItemDTO> items;
}
