package com.storefront.product.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.storefront.product.config.ProductConstants;
import com.storefront.product.dto.ProductDto;
import com.storefront.product.dto.ResponseDto;
import com.storefront.product.service.ProductService;

import jakarta.validation.Valid;

@RestController
@Validated
@RequestMapping("/api/product")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@PostMapping
	public ResponseEntity<ResponseDto> create(@Valid @RequestBody ProductDto productDto) {
		productService.create(productDto);
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(new ResponseDto(ProductConstants.STATUS_201, ProductConstants.MESSAGE_201_PRODUCT));
	}

	@GetMapping
	public ResponseEntity<List<ProductDto>> getProducts() {
		return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.getProducts());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ProductDto> getProduct(@PathVariable String id) {
		return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.getProductById(id));
	}
	
	@GetMapping("/getProductByNameOrDescription/{text}")
	public ResponseEntity<List<ProductDto>> getProductByNameOrDescription(@PathVariable String text) {
		return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.getProductByNameOrDescription(text));
	}
	
	@GetMapping("/findByCategory")
	public ResponseEntity<List<ProductDto>> findByCategory(@PathVariable Long id) {
		return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.findByCategory(id));
	}
	
	@PutMapping
	public ResponseEntity<ResponseDto> update(@Valid @RequestBody ProductDto productDto) {
		boolean isUpdated = productService.update(productDto);
        if(isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(ProductConstants.STATUS_200, ProductConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(ProductConstants.STATUS_417, ProductConstants.MESSAGE_417_UPDATE));
        }
	}
	
	@PostMapping("/{id}/deactivate")
	public ResponseEntity<ResponseDto> deactivate(@PathVariable("id") String id) {
		boolean isdeactivated = productService.deactivate(id);
		 if(isdeactivated) {
	            return ResponseEntity
	                    .status(HttpStatus.OK)
	                    .body(new ResponseDto(ProductConstants.STATUS_200, ProductConstants.MESSAGE_200));
	        } else {
	            return ResponseEntity
	                    .status(HttpStatus.EXPECTATION_FAILED)
	                    .body(new ResponseDto(ProductConstants.STATUS_417, ProductConstants.MESSAGE_417_DELETE));
	        }
	}
	
	@PostMapping("/{id}/activate")
	public ResponseEntity<ResponseDto> activate(@PathVariable("id") String id) {
		boolean isActivated = productService.activate(id);
		 if(isActivated) {
	            return ResponseEntity
	                    .status(HttpStatus.OK)
	                    .body(new ResponseDto(ProductConstants.STATUS_200, ProductConstants.MESSAGE_200));
	        } else {
	            return ResponseEntity
	                    .status(HttpStatus.EXPECTATION_FAILED)
	                    .body(new ResponseDto(ProductConstants.STATUS_417, ProductConstants.MESSAGE_417_UPDATE));
	        }
	}

}