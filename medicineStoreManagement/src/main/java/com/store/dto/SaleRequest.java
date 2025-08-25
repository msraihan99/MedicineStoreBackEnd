package com.store.dto;

import java.util.List;

import lombok.Data;

@Data
public class SaleRequest {
	 private List<SaleItemDTO> items;
    
}
