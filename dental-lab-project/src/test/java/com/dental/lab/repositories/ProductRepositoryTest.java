package com.dental.lab.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.dental.lab.model.entities.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ProductRepositoryTest {
	
	@Autowired
	private ProductRepository productRepo;
	
	@Test
	public void findByCategoryIdTest() {
		Set<Product> products = 
				productRepo.findByCategoryId(2L);
		
		assertThat(products)
			.extracting("name")
			.containsOnly(
					"Incrustacion de resina",
					"Carilla de resina",
					"Corona de resina");
	}

}
