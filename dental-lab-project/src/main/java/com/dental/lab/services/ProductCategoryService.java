package com.dental.lab.services;

import com.dental.lab.exceptions.ProductCategoryNotFoundException;
import com.dental.lab.model.entities.ProductCategory;

public interface ProductCategoryService {
	
	/**
	 * Finds the {@linkplain ProductCategory} entity with id {@code categoryId}
	 * 
	 * @param categoryId Id of the {@linkplain ProductCategory} entity to be found
	 * @return {@linkplain ProductCategory} entity with id {@code categoryId} if exists
	 * @throws ProductCategoryNotFoundException If no {@linkplain ProductCategory} entity
	 * 			with id {@code categoryId} is found
	 */
	ProductCategory findById(Long categoryId) throws ProductCategoryNotFoundException;
	
	ProductCategory findRootCategory();

}
