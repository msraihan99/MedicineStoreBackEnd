package com.store.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "sale_items")
public class SaleItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String medicineName;
    private int quantity;
    private double price;
    private double total;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    private Sale sale;
    
    
}
