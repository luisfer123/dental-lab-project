package com.dental.lab.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dental.lab.model.entities.ProductCategory;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
	
	Optional<ProductCategory> findById(Long id);
	
	@Query("select c from ProductCategory c where c.parent = null")
	Optional<ProductCategory> findRootCategory();

}
