package com.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.store.model.SaleItem;

public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {}

