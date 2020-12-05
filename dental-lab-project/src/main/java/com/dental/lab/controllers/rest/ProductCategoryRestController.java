package com.dental.lab.controllers.rest;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dental.lab.model.entities.ProductCategory;
import com.dental.lab.services.ProductCategoryService;
import com.dental.lab.services.ProductService;

@RestController
@RequestMapping(path = "/api/v1/categories")
public class ProductCategoryRestController {
	
	@Autowired
	private ProductCategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	
	@RequestMapping(path = "/root-category")
	public ResponseEntity<Set<ProductCategory>> getRootCategory() {
		Set<ProductCategory> categories = new HashSet<>();
		categories.add(categoryService.findRootCategory());
		
		return ResponseEntity.ok(categories);
	}
	
	@RequestMapping(path = "/category-children")
	public ResponseEntity<?> getCategoryChildren(
			@RequestParam("parentCategoryId") Long categoryId) {
		
		ProductCategory parentCategory = 
				categoryService.findById(categoryId);

		return ResponseEntity.ok(parentCategory.getChildren());
	}
	
	@RequestMapping(path = "/update-category", method = RequestMethod.POST)
	public ResponseEntity<Set<ProductCategory>> updateProductCategory(
			@RequestParam("parentCategoryId") Long categoryId,
			@RequestParam("productId") Long productId) {
		
		productService.updateProductCategory(productId, categoryId);
		
		return ResponseEntity
				.ok()
				.build();
	
	}

}
