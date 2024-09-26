package com.dailyshopper.service.product;

import java.util.List;
import java.util.Optional;

import com.dailyshopper.exceptions.ResourceNotFoundException;
import com.dailyshopper.repository.CategoryRepository;
import com.dailyshopper.requests.AddProductRequest;
import com.dailyshopper.requests.UpdateProductRequest;
import org.springframework.stereotype.Service;
import com.dailyshopper.model.Category;
import com.dailyshopper.model.Product;
import com.dailyshopper.repository.ProductRepository;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
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

	public Product updateProduct(UpdateProductRequest request, Long productId) {

		return productRepository.findById(productId)
				.map(existingProduct-> updateExistingProduct(request, existingProduct))
				.map(productRepository::save)
				.orElseThrow(()->new ResourceNotFoundException("Product not found!"));

	}

	private Product updateExistingProduct(UpdateProductRequest request, Product existingProduct) {

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
	public Product getProductById(Long id) {
		// TODO Auto-generated method stub
		return productRepository.findById(id)
				.orElseThrow(()-> new ResourceNotFoundException("Product Not Found!"));
	}


	@Override
	public void deleteProductById(Long id) {
		// TODO Auto-generated method stub
		productRepository.findById(id).ifPresentOrElse(productRepository :: delete
				, ()-> {throw new ResourceNotFoundException("Product Not Found!");});
		
	}


	@Override
	public List<Product> getAllProducts() {
		// TODO Auto-generated method stub
		return productRepository.findAll();
	}

	@Override
	public List<Product> getProductsByCategory(String category) {
		// TODO Auto-generated method stub
		return productRepository.findByCategoryName(category);	}

	@Override
	public List<Product> getProductsByBrand(String brand) {
		// TODO Auto-generated method stub
		return productRepository.findByBrand(brand);
	}

	@Override
	public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
		// TODO Auto-generated method stub
		return productRepository.findByCategoryAndBrand(category, brand);
	}

	@Override
	public List<Product> getProductsByName(String name) {
		// TODO Auto-generated method stub
		return productRepository.findByName(name);
	}

	@Override
	public List<Product> getProductsByBrandAndName(String category, String name) {
		// TODO Auto-generated method stub
		return productRepository.findByBrandAndName(category, name);
	}

	@Override
	public Long countProductsByBrandAndName(String brand, String name) {
		// TODO Auto-generated method stub
		return productRepository.countByBrandAndName(brand, name);
	}

}
