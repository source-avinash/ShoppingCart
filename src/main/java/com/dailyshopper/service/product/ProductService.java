package com.dailyshopper.service.product;

import java.util.List;
import java.util.Optional;

import com.dailyshopper.repository.CategoryRepository;
import com.dailyshopper.requests.AddProductRequest;
import com.dailyshopper.requests.UpdateProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dailyshopper.exceptions.ProductNotFoundException;
import com.dailyshopper.model.Category;
import com.dailyshopper.model.Product;
import com.dailyshopper.repository.ProductRepository;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{


	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;

	@Override
	public Product addProduct(AddProductRequest request) {

		//check if the category is found in DB
		//if yes, set it as the new product
		//if no, then save it as new category
		//then set it as new product category

		Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
				.orElseGet(()->{
					Category newCategory = new Category(request.getCategory().getName());
					return categoryRepository.save(newCategory);
				});
			request.setCategory(category);
			return productRepository.save(createProduct(request, category));
	}

	public Product createProduct(AddProductRequest request, Category category) {
		return new Product(
				request.getName(),
				request.getBrand(),
				request.getPrice(),
				request.getInventory(),
				request.getDescription(),
				category
		);
	}

	private Product updateProduct(UpdateProductRequest request, Long pid) {

		return productRepository.findById(pid)
				.map(existingProduct-> updateExistingProduct(existingProduct, request))
				.map(productRepository::save)
				.orElseThrow(()->new ProductNotFoundException("Product not found!"));

	}

	private Product updateExistingProduct(Product existingProduct, UpdateProductRequest request) {

		existingProduct.setName(request.getName());
		existingProduct.setBrand(request.getBrand());
		existingProduct.setPrice(request.getPrice());
		existingProduct.setInventory(request.getInventory());
		existingProduct.setDescription(request.getDescription());


		Category category = categoryRepository.findByName(request.getCategory().getName());
		existingProduct.setCategory(category);
		return existingProduct;
	}

	@Override
	public Product getProductById(long id) {
		// TODO Auto-generated method stub
		return productRepository.findById(id)
				.orElseThrow(()-> new ProductNotFoundException("Product Not Found!"));
	}

	@Override
	public void deleteProductById(long id) {
		// TODO Auto-generated method stub
		productRepository.findById(id).ifPresentOrElse(productRepository :: delete
				, ()-> {throw new ProductNotFoundException("Product Not Found!");});
		
	}

	@Override
	public void updateProduct(Product product, long productId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Product> getAllProducts() {
		// TODO Auto-generated method stub
		return productRepository.findAll();
	}

	@Override
	public List<Product> getProdutsByCategory(String category) {
		// TODO Auto-generated method stub
		return productRepository.findByCategoryName(category);	}

	@Override
	public List<Product> getProdutsByBrand(String brand) {
		// TODO Auto-generated method stub
		return productRepository.findByBrandName(brand);
	}

	@Override
	public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
		// TODO Auto-generated method stub
		return productRepository.findByCategoryAndBrand(category, brand);
	}

	@Override
	public List<Product> getProductsByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> getProductsByBrandAndName(String category, String name) {
		// TODO Auto-generated method stub
		return productRepository.findByBrandAndName(category, name);
	}

	@Override
	public long countProductsByBrandAndName(String brand, String name) {
		// TODO Auto-generated method stub
		return productRepository.countByBrandAndName(brand, name);
	}

}
