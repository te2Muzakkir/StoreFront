package com.storefront.product.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.storefront.product.config.ProductConstants;
import com.storefront.product.dto.CategoryDto;
import com.storefront.product.dto.ResponseDto;
import com.storefront.product.entity.Category;
import com.storefront.product.service.CategoryService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/category")
@Validated
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	
	@PostMapping("/create")
	public ResponseEntity<ResponseDto> create(@Valid @RequestBody CategoryDto categoryDto) {
		categoryService.create(categoryDto);
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(new ResponseDto(ProductConstants.MESSAGE_201_CATEGORY, ProductConstants.MESSAGE_201_CATEGORY));
	}

	@GetMapping
	public ResponseEntity<List<Category>> getCategories() {
		return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.getCategories());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Category> getCategory(@PathVariable String id) {
		return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.getCategory(id));
	}
	
	@PutMapping("/update")
	public ResponseEntity<ResponseDto> update(@Valid @RequestBody CategoryDto categoryDto) {
		boolean isUpdated = categoryService.update(categoryDto);
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
	
	@DeleteMapping("/deactivate")
	public ResponseEntity<ResponseDto> deactivate(@RequestParam("id") String id) {
		boolean isdeleted = categoryService.delete(id);
		 if(isdeleted) {
	            return ResponseEntity
	                    .status(HttpStatus.OK)
	                    .body(new ResponseDto(ProductConstants.STATUS_200, ProductConstants.MESSAGE_200));
	        } else {
	            return ResponseEntity
	                    .status(HttpStatus.EXPECTATION_FAILED)
	                    .body(new ResponseDto(ProductConstants.STATUS_417, ProductConstants.MESSAGE_417_DELETE));
	        }
	}
	
}