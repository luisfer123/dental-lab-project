package com.dental.lab.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dental.lab.model.entities.ProductCategory;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
	
	Optional<ProductCategory> findById(Long id);
	
	@Query("select c from ProductCategory c where c.parent = null")
	Optional<ProductCategory> findRootCategory();
	
	@Query("select c from ProductCategory c join c.products p where p.id = :productId")
	Set<ProductCategory> findProductCategoriesByProductId(@Param("productId") Long productId);

}
