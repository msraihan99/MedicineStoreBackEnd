package com.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.store.model.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {

}
