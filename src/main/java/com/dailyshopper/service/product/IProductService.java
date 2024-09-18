package com.dailyshopper.service.product;

import java.util.List;

import com.dailyshopper.model.Product;
import com.dailyshopper.requests.AddProductRequest;
import com.fasterxml.jackson.core.io.SegmentedStringWriter;

public interface IProductService {

	Product addProduct(AddProductRequest product);
	Product getProductById(long id);
	void deleteProductById(long id);
	void updateProduct(Product product, long productId);
	List<Product> getAllProducts();
	List<Product> getProdutsByCategory(String category);
	List<Product> getProdutsByBrand(String brand);
	List<Product> getProductsByCategoryAndBrand(String category, String brand);
	List<Product> getProductsByName(String name);
	List<Product> getProductsByBrandAndName(String category, String name);
	long countProductsByBrandAndName(String brand, String name);
}
