package com.dental.lab.services;

import java.util.List;
import java.util.Set;

import com.dental.lab.model.entities.Product;
import com.dental.lab.model.entities.ProductCategory;
import com.dental.lab.model.entities.ProductPricing;
import com.dental.lab.model.payloads.ProductPayload;

public interface ProductService {
	
	Set<Product> findByCategoryId(Long categoryId);
	
	/**
	 * Receives a {@linkplain Set} of {@linkplain Product}s and return a {@linkplain Set}
	 * with the corresponding {@linkplain ProductPayload} objects
	 * 
	 * @param products
	 * @return
	 */
	Set<ProductPayload> createProductPayload(Set<Product> products);
	
	/**
	 * Finds the current price of the product by returning the {@linkplain ProductPricing}
	 * entity with the greater (latest) {@code startingDate} attribute's value. 
	 * 
	 * @param productId Id of the {@linkplain Product} we want to find the current
	 * 			price of
	 * @return {@linkplain ProductPricing} with the latest {@code startingDate} 
	 * attribute's value
	 */
	ProductPricing findCurrentPrice(Long productId);
	
	/**
	 * Builds the path of {@linkplain ProductCategory}s in the Category tree.
	 * Goes from the root {@linkplain ProductCategory} to the {@linkplain ProductCategory} passed as
	 * argument.
	 * 
	 * @param category
	 * @return A {@linkplain List} from root {@linkplain ProductCategory} to the
	 * 			{@linkplain ProductCategory} passed as argument
	 */
	List<ProductCategory> buildCategoryPath(ProductCategory category);

}
