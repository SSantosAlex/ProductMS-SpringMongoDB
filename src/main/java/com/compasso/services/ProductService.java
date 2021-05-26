package com.compasso.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.compasso.models.ProductDTO;
import com.compasso.repositories.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository prodRepo;
	
	public List<ProductDTO> listAllProducts() {
		return prodRepo.findAll();
	}
	
	public Optional<ProductDTO> getProductbyId(String id) {
		return prodRepo.findById(id);
	}
	
	public void saveProduct(ProductDTO productDTO) {		
		prodRepo.save(productDTO);		
	}
	
	public void deleteProduct(String id) {
		  prodRepo.deleteById(id);
	}

	public HashSet<ProductDTO> searchProduct(String q, Double min_price, Double max_price) {

		HashSet<ProductDTO> productDTOs = new HashSet<ProductDTO>();
			
		if(min_price != null && max_price != null) {
			prodRepo.getByPriceBetween(min_price, max_price).forEach(productDTOs::add);
		}
		
		if(min_price !=null && max_price == null) {
			
			prodRepo.findByPriceGreaterThanEqual(min_price).forEach(productDTOs::add);
			
		} else if(min_price == null && max_price != null) {
			prodRepo.findByPriceLessThanEqual(max_price).forEach(productDTOs::add);
		}
		
		if(q!= null) {
			prodRepo.findByNameLikeIgnoreCaseOrDescriptionLikeIgnoreCase(q,q).forEach(productDTOs::add);
		}
		
		productDTOs.remove(null);
		
		return productDTOs;	
	}
	 
}
