package com.store.service;

import java.io.ByteArrayOutputStream;

import com.store.dto.SaleRequest;
import com.store.dto.SaleRequestDTO;

public interface SaleService {
	
	String processSale(SaleRequest saleRequest);
	
	byte[] processSaleAndGeneratePDF(SaleRequest saleRequest);


}
