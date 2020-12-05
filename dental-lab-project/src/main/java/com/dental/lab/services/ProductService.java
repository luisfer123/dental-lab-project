package com.dental.lab.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;

import com.dental.lab.exceptions.InvalidPageException;
import com.dental.lab.model.entities.Product;
import com.dental.lab.model.entities.ProductCategory;
import com.dental.lab.model.entities.ProductPricing;
import com.dental.lab.model.payloads.ProductPayload;

public interface ProductService {
	
	Page<Product> findAllPaginated(int pageNumber, int pageSize, String sortBy) throws InvalidPageException;
	
	/**
	 * Finds the {@linkplain Product} with id {@code productId}, if the {@linkplain productImage}
	 * property is null, a default images is added.
	 * 
	 * @param productId Id of the {@linkplain Product} we want to find
	 * @return {@linkplain Product} with id {@code productId}
	 */
	Product findById(Long productId);
	
	/**
	 * Finds all the {@linkplain Product}s which are associated with
	 * the {@linkplain ProductCategory} entity with id {@code categoryId}
	 * 
	 * @param categoryId
	 * @return
	 */
	Set<Product> findByCategoryId(Long categoryId);
	
	/**
	 * Receives a {@linkplain Set} of {@linkplain Product}s and return a {@linkplain Set}
	 * with the corresponding {@linkplain ProductPayload} objects.
	 * PayloadProduct
	 * object contains a string representation of the productPicture field instead
	 * of a byte[] object.
	 * 
	 * @param products
	 * @return
	 */
	Set<ProductPayload> createProductPayload(Set<Product> products);
	
	/**
	 * Retrieves {@linkplain ProductPricing} object with the latest (maximum) {@code starting_date}
	 * entry in the database. This tuple corresponds to the current {@linkplain Product}'s price.
	 * Tuples with smaller {@code starting_date} entries correspond with prices that were used
	 * in the past for the corresponding product.
	 * 
	 * @param productId Id of the {@linkplain Product} we want to find the current
	 * 			price of
	 * @return {@linkplain ProductPricing} with the latest {@code startingDate} 
	 * 			attribute's value
	 */
	ProductPricing findCurrentPriceByProductId(Long productId);
	
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
	
	/**
	 * Returns a {@linkplain List} with all the {@linkplain ProductCategory} entities
	 * related with the {@linkplain Product} passed as argument. {@linkplain ProductCategory}
	 * entities are sorted by the order define in the category tree.
	 * 
	 * @param product 
	 * @return {@linkplain List} of sorted {@linkplain ProductCategory} entities
	 */
	List<ProductCategory> buildProductCategoryPath(Product product);
	
	/**
	 * Updates the {@code productImage} field in the {@linkplain Product} 
	 * entity with id {@code productId}.
	 * 
	 * @param productId id of the product which {@code productImage} field is going to be updated.
	 * @param imageToUpdate {@code byte[]} object containing the image to be used to update the
	 * 			{@code productImage} field of the {@linkplain Product} with id {@code productId}
	 * @return {@linkplain Product} object with id {@code productId}, and {@code productImage} updated.
	 * @throws EntityNotFoundException
	 */
	Product updateProductThumbnailImage(Long productId, byte[] imageToUpdate) throws EntityNotFoundException;
	
	/**
	 * <p>Updates {@linkplain Product} information. For updating {@linkplain ProductPricing}
	 * the new price must be different than the current price and 
	 * {@linkplain ProductService#updateProductPrice(Product, BigDecimal)}
	 * is called.</p>
	 * 
	 * @param productId Id of the {@linkplain Product} to be updated
	 * @param productUpdated {@linkplain Product} object with the updated information
	 * @param newPrice New price id we want to update the current price. Otherwise
	 * 			the current price should be passed and no update will be perform
	 * @return {@linkplain Product} with the updated information
	 * @throws EntityNotFoundException If  no {@linkplain Product} with id {@code productId}
	 * 			is found
	 */
	Product updateProduct(Long productId, Product productUpdated, BigDecimal newPrice) throws EntityNotFoundException;
	
	/**
	 * <p>Updates the current price of the given {@linkplain Product} object.</p>
	 * <p>Product price is not actually updated but a new instance of {@linkplain ProductPricing}
	 * is created with startingDate equal to the current date and time. Current 
	 * {@linkplain Product}'s price is always taken as the {@linkplain ProductPricing} 
	 * with the latest startingDate. </p>
	 * Since the {@linkplain ProductPricing} with the latest {@code startingDate} 
	 * until now will not longer be the current price of the {@linkplain Product} 
	 * with id {@code productId}, the {@code endingDate} field in {@linkplain ProductPricing} 
	 * with the latest {@code startingDate} must be updated to the current date. 
	 * This way the new {@linkplain ProductPricing} with latest {@code startingDate} 
	 * will be the one we are adding.
	 *
	 * @param product {@linkplain Product} we want to update {@linkplain ProductPricing}
	 * 			from
	 * @param newPrice
	 * @return {@linkplain ProductPricing} object created, corresponding with the
	 * 			updated {@linkplain Product} price
	 */
	ProductPricing updateProductPrice(Product product, BigDecimal newPrice);
	
	/**
	 * Updates the {@linkplain Product}'s {@code categories} field with a new
	 * {@linkplain Set} of {@linkplain ProductCategory}s.
	 * Only one {@linkplain ProductCategory} can be assign to a {@linkplain Product}
	 * at a time; thought, once one {@linkplain ProductCategory} is assigned, the
	 * parent {@linkplain ProductCategory} and the parent of the parent, and so on...
	 * are associated with the {@linkplain Product} as well. So once one 
	 * {@linkplain ProductCategory} is assigned, so are all its predecessors in the
	 * category tree. All {@linkplain ProductCategory}s associated with one {@linkplain Product}
	 * must belong to a single pat in the {@linkplain ProductCategory} tree.
	 * 
	 * @param productId Id of the {@linkplain Product} we want to update categories of
	 * @param newCategoryId Id of the {@linkplain ProductCategory} we want to associate 
	 * 			with the	{@linkplain Product} (and all its predecessors)
	 * @return {@linkplain Product} with id {@code productId}, already updated
	 */
	Product updateProductCategory(Long productId, Long newCategoryId);

}
