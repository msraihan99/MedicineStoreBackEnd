package com.store.controller;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.store.dto.SaleRequest;
import com.store.dto.SaleRequestDTO;
import com.store.model.Medicine;
import com.store.service.MedicineService;
import com.store.service.SaleService;

@RestController
@RequestMapping("/medicines")
@CrossOrigin(origins = "http://localhost:3000")
public class MedicineController {

	@Autowired
	private MedicineService medicineService;

	@Autowired
	private SaleService saleService;
	
	private static final Logger log = LoggerFactory.getLogger(MedicineController.class);

	@PostMapping("/add-medicine")
	public ResponseEntity<?> addMedicine(@RequestBody Medicine medicine) {
	    log.info("Inside addMedicine() :: MedicineController");
	    try {
	        Medicine saved = medicineService.saveMedicine(medicine);
	        log.info("Exiting from addMedicine() :: MedicineController - medicineId: {}", saved.getId());
	        return ResponseEntity.ok(saved);
	    } catch (Exception e) {
	        log.error("Error occurred while saving medicine: ", e);
	        return ResponseEntity
	                .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("An unexpected error occurred while adding the medicine.");
	    }
	}


	@GetMapping("/get-medicines")
	public List<Medicine> getAllMedicines() {
		return medicineService.getAllMedicines();
	}

	@PostMapping("/sell1")
    public String sellMedicines1(@RequestBody SaleRequest saleRequest) {
        return saleService.processSale(saleRequest);
    }
	
	
//	@PostMapping("/sell")
//    public ResponseEntity<byte[]> sellMedicines(@RequestBody SaleRequestDTO request) {
//        ByteArrayOutputStream pdfStream = saleService.processSaleAndGeneratePDF(request);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_PDF);
//        headers.setContentDispositionFormData("attachment", "sale-receipt.pdf");
//
//        return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);
//    }
    
    @PostMapping(value = "/sell", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> sellMedicines(@RequestBody SaleRequest saleRequest) {
        byte[] pdfBytes = saleService.processSaleAndGeneratePDF(saleRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "invoice.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    
    
	 
}
