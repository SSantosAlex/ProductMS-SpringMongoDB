package com.compasso.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.compasso.models.ErrorMsgs;
import com.compasso.models.ProductDTO;
import com.compasso.services.ProductService;
import com.compasso.services.SequenceGeneratorService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value="API Catalogo de Produtos")
@CrossOrigin(origins="*")
public class ProductController {
	
	@Autowired
	private ProductService prodService;
	
	@Autowired
    private SequenceGeneratorService sequenceGeneratorService;
	
	@GetMapping("/products")
	@ApiOperation(value="Lista de produtos")
	public List<ProductDTO> list(){
		return prodService.listAllProducts();
	}
	
	@GetMapping("/products/search")
	@ApiOperation(value="Lista de produtos filtrados")
	@ResponseBody
	public List<?> searchProduct(@RequestParam(required = false) String q,
			@RequestParam(required = false, name= "min_price") Double min_price,
			@RequestParam(required = false, name= "max_price") Double max_price)
	{
			
		List<ProductDTO> productDTOs = new ArrayList<ProductDTO>();
		
		productDTOs.addAll(prodService.searchProduct(q, min_price, max_price));
		
		return productDTOs;
	}

	
	@GetMapping("/products/{id}")
	@ApiOperation(value="Busca de um produto por ID")
	public ResponseEntity<ProductDTO> getProductById(@PathVariable("id") String id) {
		
			Optional<ProductDTO> productData = prodService.getProductbyId(id);
			
			if(productData.isPresent()) {
				return new ResponseEntity<>(productData.get(), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
	}
	
	@PostMapping("/products")
	@ApiOperation(value="Criação de um produto")
	public ResponseEntity<?> addProduct(@Valid @RequestBody ProductDTO productDTO, Errors errors) {
		
		if(errors.hasErrors()) {
			ErrorMsgs error = new ErrorMsgs(400, "Verifique os campos e valores digitados.");	
			return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		} else {
			Long sequence = sequenceGeneratorService.generateSequence(ProductDTO.SEQUENCE_NAME);
			
			productDTO.setId(sequence.toString());
			prodService.saveProduct(productDTO);				
			
			return new ResponseEntity<ProductDTO>(productDTO, HttpStatus.CREATED);
		}
	}
	
	@PutMapping("/products/{id}")
	@ApiOperation(value="Atualização de um produto")
	public ResponseEntity<?> updateProduct(@Valid @RequestBody ProductDTO productDTO,Errors errors, @PathVariable String id) {
		
			Optional<ProductDTO> checkProduct = prodService.getProductbyId(id);
			
			if(errors.hasErrors() || checkProduct.isEmpty()) {
				ErrorMsgs error = new ErrorMsgs(400, "Campos inválidos, vazios ou id não encontrado, favor verique e tente novamente");			
				return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
			} else {
				productDTO.setId(id);
				prodService.saveProduct(productDTO);
				return new ResponseEntity<ProductDTO>(productDTO, HttpStatus.OK);	
		}
	}
	
	
	@DeleteMapping("products/{id}")	  
	@ApiOperation(value="Deleção de um produto") public ResponseEntity<?>
	deleteProduct(@PathVariable String id) {
		  try { 
			  prodService.deleteProduct(id);
			  return new ResponseEntity<>(HttpStatus.OK); 
		  }catch(Exception e) {	  
			  return new ResponseEntity<>(HttpStatus.NOT_FOUND); }
		  }
	 
}