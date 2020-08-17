package com.dental.lab.services.impl;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dental.lab.exceptions.ProductCategoryNotFoundException;
import com.dental.lab.model.entities.ProductCategory;
import com.dental.lab.repositories.ProductCategoryRepository;
import com.dental.lab.services.ProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
	
	@Autowired
	private ProductCategoryRepository categoryRepo;
	
	@Transactional(readOnly = true)
	public ProductCategory findById(Long categoryId) 
			throws ProductCategoryNotFoundException {
		
		return categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ProductCategoryNotFoundException("ProductCategory entity with id: " + categoryId + " was not found!"));
	}
	
	@Transactional(readOnly = true)
	public ProductCategory findRootCategory() {
		return categoryRepo.findRootCategory()
				.orElseThrow(() -> new EntityNotFoundException("Root CategoryProduct was not found"));
	}

}
