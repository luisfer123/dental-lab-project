package com.dental.lab.services.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dental.lab.exceptions.InvalidPageException;
import com.dental.lab.model.entities.Product;
import com.dental.lab.model.entities.ProductCategory;
import com.dental.lab.model.entities.ProductPricing;
import com.dental.lab.model.payloads.ProductPayload;
import com.dental.lab.repositories.ProductPricingRepository;
import com.dental.lab.repositories.ProductRepository;
import com.dental.lab.services.ProductCategoryService;
import com.dental.lab.services.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private ProductCategoryService categoryService;
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	@Autowired
	private ProductPricingRepository productPricingRepo;
	
	@Override
	@Transactional(readOnly = true)
	public Page<Product> findAllPaginated(int pageNumber, int pageSize, String sortBy)
			throws InvalidPageException {
		
		Pageable requestedPage = 
				PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
		Page<Product> productsPage = productRepo.findAll(requestedPage);
		
		if(!productsPage.hasContent())
			throw new InvalidPageException();
		
		return productsPage;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public Product findById(Long id) {
		Product product = productRepo.findById(id).orElseThrow(
				() -> new EntityNotFoundException("Entity Product with id: " + id + " does not exists"));
		
		if(product.getProductImage() == null || product.getProductImage().length == 0) {
			try {
				Resource imageResource = resourceLoader.getResource("classpath:static/images/No-photo-product.jpg");
				byte[] image = Files.readAllBytes(Paths.get(imageResource.getURI()));
				product.setProductImage(image);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return product;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public Set<Product> findByCategoryId(Long categoryId) {
		return productRepo.findByCategoryId(categoryId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = true)
	public ProductPricing findCurrentPriceByProductId(Long productId) {
		return productPricingRepo.findCurrentPriceByProductId(productId)
				.orElseThrow(() -> new EntityNotFoundException("ProductPricing entity with id: " + productId + " was not found!"));
	}
	
	/**
	 * {@inheritDoc}
	 */
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
					ProductPricing currentPrice = findCurrentPriceByProductId(product.getId());
					ProductPayload productPayload = new ProductPayload(product, currentPrice.getPrice());
					return productPayload;
				})
				.collect(Collectors.toSet());
		
		return productsPayload;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ProductCategory> buildProductCategoryPath(Product product) {
		List<ProductCategory> productCategories = new ArrayList<>(
				categoryService.findByProduct(product));
		productCategories.sort(Comparator.comparingInt(ProductCategory::getDepth));
		return productCategories;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	@PreAuthorize(value = "hasRole('ADMIN')")
	public Product updateProductThumbnailImage(Long productId, byte[] imageToUpdate) 
			throws EntityNotFoundException {
		
		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new EntityNotFoundException("Product with id: " + productId + " was not found!"));
		
		product.setProductImage(imageToUpdate);
		return productRepo.save(product);
				
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	@PreAuthorize(value = "hasRole('ADMIN')")
	public Product updateProduct(
			Long productId, Product productUpdated, BigDecimal newPrice) throws EntityNotFoundException {
		
		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new EntityNotFoundException("Product with id: " + productId + " was not found!"));
		
		product.setName(productUpdated.getName());
		product.setDescription(productUpdated.getDescription());
		
		/*
		 * Since each time product price is updated, a new record is inserted
		 * into product_pricing table. We should update Product price only if
		 * the new price is actually different to the current price stored.
		 */
		if(!findCurrentPriceByProductId(productId).getPrice().equals(newPrice))
			updateProductPrice(product, newPrice);
		
		return productRepo.save(product);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	@PreAuthorize(value = "hasRole('ADMIN')")
	public ProductPricing updateProductPrice(Product product, BigDecimal newPrice) {
		/*
		 * Since the ProductPricing with the latest startingDate until now will 
		 * not longer be the current price of the Product with id productId, 
		 * the endingDate field in ProductPricing with the latest startingDate 
		 * must be updated to the current date. This way the new ProductPricing 
		 * with latest startingDate will be the one we are about to add.
		 */
		ProductPricing currentPrice =
				productPricingRepo.findCurrentPriceByProductId(product.getId())
				.orElseThrow(() -> new EntityNotFoundException("ProductPricing for Product with id: " + product.getId() + " was not found!"));
		
		// TODO: get right date.
		currentPrice.setEndingDate(new Timestamp((new Date()).getTime()));
		productPricingRepo.save(currentPrice);
		
		/* Product price is not actually updated but a new instance of ProductPricing
		 * is created with startingDate equal to the current date and time. Current 
		 * product price is always taken as the ProductPricing with the latest startingDate.
		 */
		ProductPricing newProductPricing = new ProductPricing(newPrice);
		newProductPricing.setProduct(product);
		product.getProductPricings().add(newProductPricing);
		
		return productPricingRepo.save(newProductPricing);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	@PreAuthorize(value = "hasRole('ADMIN')")
	public Product updateProductCategory(Long productId, Long newCategoryId) {
		
		Product product = findById(productId);
		ProductCategory newCategory = categoryService.findById(newCategoryId);
		Set<ProductCategory> newCategories = new HashSet<>();
		
		while(newCategory != null) {
			newCategory.getProducts().add(product);
			newCategories.add(newCategory);
			newCategory = newCategory.getParent();
		}
		
		product.setCategories(newCategories);
		Product savedProduct = productRepo.save(product);
		
		return savedProduct;
		
	}


}
