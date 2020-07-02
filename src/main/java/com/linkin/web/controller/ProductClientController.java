package com.linkin.web.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkin.model.ProductDTO;
import com.linkin.model.ResponseDTO;
import com.linkin.model.SearchCategoryDTO;
import com.linkin.model.SearchProductDTO;
import com.linkin.model.SearchSupplierDTO;
import com.linkin.service.CategoryService;
import com.linkin.service.ProductService;
import com.linkin.service.SupplierService;
import com.linkin.utils.FileStore;

@Controller
@RequestMapping(value = "/staff")
public class ProductClientController {
	private static final Logger logger = LoggerFactory.getLogger(ProductClientController.class);
	@Autowired
	ProductService productService;
	@Autowired 
	CategoryService categoryService;
	@Autowired 
	SupplierService supplierService;

	@GetMapping(value = "/product/list")
	public String list(Model model) {
		SearchCategoryDTO searchCategoryDTO = new SearchCategoryDTO();
		searchCategoryDTO.setStart(null);
		model.addAttribute("categories", categoryService.find(searchCategoryDTO));
		SearchSupplierDTO searchSupplierDTO = new SearchSupplierDTO();
		searchCategoryDTO.setStart(null);
		model.addAttribute("suppliers", supplierService.find(searchSupplierDTO));
		return "client/product/list-product";
	}
	
	@PostMapping(value = "/product/list")
	public ResponseEntity<ResponseDTO<ProductDTO>> listDictionary(@RequestBody SearchProductDTO searchProductDTO) {
		ResponseDTO<ProductDTO> responseDTO = new ResponseDTO<>();
		responseDTO.setData(productService.find(searchProductDTO));
		responseDTO.setRecordsFiltered(productService.count(searchProductDTO));
		responseDTO.setRecordsTotal(productService.countTotal(searchProductDTO));
		return new ResponseEntity<ResponseDTO<ProductDTO>>(responseDTO, HttpStatus.OK);
	}

}
