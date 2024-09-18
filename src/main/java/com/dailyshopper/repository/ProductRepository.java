package com.dailyshopper.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dailyshopper.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findByCategoryName(String category);
	
	List<Product> findByBrandName(String brand);

	List<Product> findByCategoryAndBrand(String category, String brand);

	List<Product> findByBrandAndName(String category, String name);

	long countByBrandAndName(String brand, String name);

	

}
