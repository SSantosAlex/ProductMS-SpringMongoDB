package com.compasso.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.compasso.models.ProductDTO;

@Repository("prodRepo")
public interface ProductRepository extends MongoRepository<ProductDTO, String>{
	
	public List<ProductDTO> findByNameLikeIgnoreCaseOrDescriptionLikeIgnoreCase(String name, String description);
	
	public List<ProductDTO> findByPriceGreaterThanEqual(Double minPrice);
	
	public List<ProductDTO> findByPriceLessThanEqual(Double maxPrice);
	
	@Query(value= "{'price':{ $gte: ?0, $lte: ?1}}")
	public List<ProductDTO> getByPriceBetween(Double minPrice, Double maxPrice);
	
}
