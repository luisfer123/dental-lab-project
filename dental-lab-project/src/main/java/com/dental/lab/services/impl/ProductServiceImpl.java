package com.dental.lab.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dental.lab.model.entities.Product;
import com.dental.lab.model.entities.ProductCategory;
import com.dental.lab.model.entities.ProductPricing;
import com.dental.lab.model.payloads.ProductPayload;
import com.dental.lab.repositories.ProductPricingRepository;
import com.dental.lab.repositories.ProductRepository;
import com.dental.lab.services.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	@Autowired
	private ProductPricingRepository productPricingRepo;
	
	@Override
	@Transactional(readOnly = true)
	public Set<Product> findByCategoryId(Long categoryId) {
		return productRepo.findByCategoryId(categoryId);
	}
	
	@Transactional(readOnly = true)
	public ProductPricing findCurrentPrice(Long productId) {

		ProductPricing pp = productPricingRepo.findCurrentPriceByProductId(productId)
				.orElseThrow(() -> new EntityNotFoundException());
		
		return pp;
	}
	
	@Override
	public Set<ProductPayload> createProductPayload(Set<Product> products) {
		
		/*
		 *  Add the default image to the products that do not have photo.
		 */
		byte[] image = null;
		for(Product product: products) {
			if(product.getProductImage() == null || product.getProductImage().length <= 0) {
				if(image == null) {
					try {
						Resource imageResource = resourceLoader.getResource("classpath:static/images/No-photo-product.jpg");
						image = Files.readAllBytes(Paths.get(imageResource.getURI()));
					} catch(IOException e) {
						e.printStackTrace();
					}
				}
				product.setProductImage(image);
			}
		}
		
		/*
		 * Create PayloadProduct for sending products to the view. PayloadProduct
		 * object contains a string representation of the productPicture field instead
		 * of a byte[] object.
		 * 
		 */
		Set<ProductPayload> productsPayload = products
			.stream()
				.map(product -> {
					ProductPricing currentPrice = findCurrentPrice(product.getId());
					ProductPayload productPayload = new ProductPayload(product, currentPrice.getPrice());
					return productPayload;
				})
				.collect(Collectors.toSet());
		
		return productsPayload;
	}
	
	@Transactional(readOnly = true)
	public List<ProductCategory> buildCategoryPath(ProductCategory category) {
		List<ProductCategory> categoryPath = new ArrayList<ProductCategory>();
		
		ProductCategory tempCategory = category;
		do {
			categoryPath.add(tempCategory);
			tempCategory = tempCategory.getParent();
		} while(tempCategory != null);
		
		Collections.reverse(categoryPath);
		
		return categoryPath;
	}

}
