package com.dental.lab.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dental.lab.model.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
	@Query("select p from Product p join p.categories c where c.id = :categoryId")
	Set<Product> findByCategoryId(@Param("categoryId") Long categoryId);

}
