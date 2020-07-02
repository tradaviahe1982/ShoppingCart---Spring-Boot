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
@RequestMapping(value = "/admin")
public class ProductController {
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
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
		return "admin/product/list-product";
	}
	
	@PostMapping(value = "/product/list")
	public ResponseEntity<ResponseDTO<ProductDTO>> listDictionary(@RequestBody SearchProductDTO searchProductDTO) {
		ResponseDTO<ProductDTO> responseDTO = new ResponseDTO<>();
		responseDTO.setData(productService.find(searchProductDTO));
		responseDTO.setRecordsFiltered(productService.count(searchProductDTO));
		responseDTO.setRecordsTotal(productService.countTotal(searchProductDTO));

		return new ResponseEntity<ResponseDTO<ProductDTO>>(responseDTO, HttpStatus.OK);
	}

	@GetMapping(value = "/product/delete/{id}")
	public ResponseEntity<String> del(@PathVariable(name = "id") Long id) {
		productService.delete(id);
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}

	@GetMapping(value = "/product/delete-multi/{ids}")
	public ResponseEntity<String> del(@PathVariable(name = "ids") List<Long> ids) {
		for (Long id : ids) {
			try {
				productService.delete(id);
			} catch (Exception e) {

			}
		}
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}

	@PostMapping("/product/add")
	public @ResponseBody ProductDTO addProduct(@ModelAttribute ProductDTO productDTO) {
		try {
			// save image
			final String UPLOAD_FOLDER = FileStore.UPLOAD_FOLDER;
			if (productDTO.getImageFile() != null && !productDTO.getImageFile().isEmpty()) {
				String image = System.currentTimeMillis() + "-product.jpg";
				Path pathAvatar = Paths.get(UPLOAD_FOLDER + File.separator + image);
				Files.write(pathAvatar, productDTO.getImageFile().getBytes());
				productDTO.setPathImage(image);
			}
		} catch (IOException e) {
			logger.error("Upload file error");
		}
		productService.add(productDTO);
		return productDTO;
	}

	@PostMapping("/product/update")
	public @ResponseBody ProductDTO updates(@ModelAttribute ProductDTO productDTO) {
		try {
			// save image
			final String UPLOAD_FOLDER = FileStore.UPLOAD_FOLDER;
			if (productDTO.getImageFile() != null && !productDTO.getImageFile().isEmpty()) {
				String image = System.currentTimeMillis() + "-product.jpg";
				Path pathAvatar = Paths.get(UPLOAD_FOLDER + File.separator + image);
				Files.write(pathAvatar, productDTO.getImageFile().getBytes());
				productDTO.setPathImage(image);
			}
		} catch (IOException e) {
			logger.error("Upload file error");
		}
		productService.update(productDTO);
		return productDTO;
	}
}
