package com.store.service.impl;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.store.dto.SaleItemDTO;
import com.store.dto.SaleRequest;import com.store.model.Medicine;
import com.store.model.Sale;
import com.store.model.SaleItem;
import com.store.repository.MedicineRepository;
import com.store.repository.SaleRepository;
import com.store.service.SaleService;

@Service
public class SaleServiceImpl implements SaleService {

	@Autowired
	private MedicineRepository medicineRepo;

	@Autowired
	private SaleRepository saleRepo;

	private static final Logger log = LoggerFactory.getLogger(SaleServiceImpl.class);

	public String processSale(SaleRequest saleRequest) {
		log.info("Inside processSale() :: SaleServiceImpl");

		List<SaleItem> saleItems = new ArrayList<>();

		for (SaleItemDTO dto : saleRequest.getItems()) {
			Medicine med = medicineRepo.findByMedicineName(dto.getMedicineName())
					.orElseThrow(() -> new RuntimeException("Medicine are not found : " + dto.getMedicineName()));

			if (med.getQuantity() < dto.getQuantity()) {
				throw new RuntimeException("Insufficient stock for : " + dto.getMedicineName());
			}
			med.setQuantity(med.getQuantity() - dto.getQuantity());
			medicineRepo.save(med);

			SaleItem item = new SaleItem();
			item.setMedicineName(dto.getMedicineName());
			item.setQuantity(dto.getQuantity());
			item.setPrice(dto.getPrice());
			item.setTotal(dto.getPrice() * dto.getQuantity());

			saleItems.add(item);

		}

		Sale sale = new Sale();
		sale.setDate(LocalDateTime.now());
		sale.setItems(saleItems);

		for (SaleItem item : saleItems) {
			item.setSale(sale);
		}

		saleRepo.save(sale);
		return "Sale Recorded Successfuly";

	}
	
	@Override
	public byte[] processSaleAndGeneratePDF(SaleRequest saleRequest) {
	    log.info("Inside processSale() :: SaleServiceImpl");

	    List<SaleItem> saleItems = new ArrayList<>();

	    for (SaleItemDTO dto : saleRequest.getItems()) {
	        Medicine med = medicineRepo.findByMedicineName(dto.getMedicineName())
	                .orElseThrow(() -> new RuntimeException("Medicine not found: " + dto.getMedicineName()));

	        if (med.getQuantity() < dto.getQuantity()) {
	            throw new RuntimeException("Insufficient stock for: " + dto.getMedicineName());
	        }

	        med.setQuantity(med.getQuantity() - dto.getQuantity());
	        medicineRepo.save(med);

	        SaleItem item = new SaleItem();
	        item.setMedicineName(dto.getMedicineName());
	        item.setQuantity(dto.getQuantity());
	        item.setPrice(dto.getPrice());
	        item.setTotal(dto.getPrice() * dto.getQuantity());

	        saleItems.add(item);
	    }

	    Sale sale = new Sale();
	    sale.setDate(LocalDateTime.now());
	    sale.setItems(saleItems);

	    for (SaleItem item : saleItems) {
	        item.setSale(sale);
	    }

	    saleRepo.save(sale);

	    return generatePDF(sale); // ⬅️ Return PDF bytes
	}


	private byte[] generatePDF(Sale sale) {
	    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
	        Document document = new Document();
	        PdfWriter.getInstance(document, baos);
	        document.open();
	        
	        
	        // 🔹 Store Details (3 lines above title)
	        Font storeFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, new BaseColor(11, 2, 250));
	        Paragraph storeName = new Paragraph("🩺 Raihan Medical Store", storeFont);
	        storeName.setAlignment(Element.ALIGN_CENTER);
	        document.add(storeName);

	        Paragraph storeAddress = new Paragraph("Action Area - II , Newtown , Kolkata , 700160");
	        storeAddress.setAlignment(Element.ALIGN_CENTER);
	        document.add(storeAddress);

	        Paragraph storeContact = new Paragraph("Contact: +91-8697404140 | Owner: Md Raihan Sk");
	        storeContact.setAlignment(Element.ALIGN_CENTER);
	        document.add(storeContact);

	        document.add(Chunk.NEWLINE); // Add space after store details

	       
	        // 🔹 Title
	        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13);
	        Paragraph title = new Paragraph("💊 Medicine Invoice", titleFont);
	        title.setAlignment(Element.ALIGN_CENTER);
	        document.add(title);
	        document.add(Chunk.NEWLINE);
	        
	     
	        // 🔹 Table
	        PdfPTable table = new PdfPTable(4);
	        table.setWidthPercentage(100);
	        table.setSpacingBefore(10f);
	        table.setSpacingAfter(10f);

	        Stream.of("Medicine", "Quantity", "Price", "Total").forEach(header -> {
	            PdfPCell cell = new PdfPCell(new Phrase(header));
	            cell.setBackgroundColor(new BaseColor(173, 216, 230));
	            cell.setPadding(5f); 
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            table.addCell(cell);
	        });

	        double grandTotal = 0;
	        for (SaleItem item : sale.getItems()) {
	        	// Medicine Name
	            PdfPCell medicineCell = new PdfPCell(new Phrase(item.getMedicineName()));
	            medicineCell.setPadding(5f);
	            medicineCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            medicineCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            medicineCell.setBackgroundColor(new BaseColor(247, 242, 205));
	            table.addCell(medicineCell);

	            // Quantity
	            PdfPCell qtyCell = new PdfPCell(new Phrase(String.valueOf(item.getQuantity())));
	            qtyCell.setPadding(5f);
	            qtyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            qtyCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            qtyCell.setBackgroundColor(new BaseColor(247, 242, 205));
	            table.addCell(qtyCell);

	            // Price
	            PdfPCell priceCell = new PdfPCell(new Phrase(String.valueOf(item.getPrice())));
	            priceCell.setPadding(5f);
	            priceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            priceCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            priceCell.setBackgroundColor(new BaseColor(247, 242, 205));
	            table.addCell(priceCell);

	            // Total
	            double total = item.getPrice() * item.getQuantity();
	            PdfPCell totalCell = new PdfPCell(new Phrase(String.format("%.2f", total)));
	            totalCell.setPadding(5f);
	            totalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            totalCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            totalCell.setBackgroundColor(new BaseColor(247, 242, 205));
	            table.addCell(totalCell);
	            
	            grandTotal += item.getTotal();
	        }

	        document.add(table);

	     // Create a 2-column table for Grand Total and Date
	        PdfPTable totalDateTable = new PdfPTable(2);
	        totalDateTable.setWidthPercentage(100);
	        totalDateTable.setSpacingBefore(20f);

	        // Grand Total cell (right aligned)
	        PdfPCell totalCell = new PdfPCell(new Phrase("\n\n\n\n\n\nGrand Total: ₹" + String.format("%.2f", grandTotal),
	                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, new BaseColor(255, 94, 0))));
	        totalCell.setBorder(PdfPCell.NO_BORDER);
	        totalCell.setHorizontalAlignment(Element.ALIGN_LEFT);
	        totalCell.setPadding(5f);

	        // Date cell (left aligned)
	        PdfPCell dateCell = new PdfPCell(new Phrase("\n\n\n\n\n\n\nDate: " + sale.getDate().toString()));
	        dateCell.setBorder(PdfPCell.NO_BORDER);
	        dateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        dateCell.setPadding(5f);

	        // Add cells to table
	        totalDateTable.addCell(totalCell);
	        totalDateTable.addCell(dateCell);

	        // Add table to document
	        document.add(totalDateTable);

	        
	        document.close();
	        return baos.toByteArray();

	    } catch (Exception e) {
	        throw new RuntimeException("Error generating PDF: " + e.getMessage(), e);
	    }
	}


	
}
