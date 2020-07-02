package com.linkin.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkin.model.CategoryDTO;
import com.linkin.model.ResponseDTO;
import com.linkin.model.SearchCategoryDTO;
import com.linkin.service.CategoryService;

@Controller
@RequestMapping(value = "/admin")
public class CategoryController {
	@Autowired
	CategoryService categoryService;

	@GetMapping(value = "/category/list")
	public String listCategory() {
		return "admin/category/list-category";
	}

	@PostMapping(value = "/category/list")
	public ResponseEntity<ResponseDTO<CategoryDTO>> listCategory(@RequestBody SearchCategoryDTO searchCategoryDTO) {
		ResponseDTO<CategoryDTO> responseDTO = new ResponseDTO<>();
		responseDTO.setData(categoryService.find(searchCategoryDTO));
		responseDTO.setRecordsFiltered(categoryService.count(searchCategoryDTO));
		responseDTO.setRecordsTotal(categoryService.countTotal(searchCategoryDTO));

		return new ResponseEntity<ResponseDTO<CategoryDTO>>(responseDTO, HttpStatus.OK);
	}

	@GetMapping(value = "/category/delete/{id}")
	public ResponseEntity<String> delCategory(@PathVariable(name = "id") Long id) {
		categoryService.delete(id);
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}

	@GetMapping(value = "/category/delete-multi/{ids}")
	public ResponseEntity<String> delCategory(@PathVariable(name = "ids") List<Long> ids) {
		for (Long id : ids) {
			try {
				categoryService.delete(id);
			} catch (Exception e) {

			}
		}
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}

	@PostMapping("/category/add")
	public @ResponseBody CategoryDTO addCategory(@RequestBody CategoryDTO categoryDTO) {
		categoryService.add(categoryDTO);
		return categoryDTO;
	}

	@PutMapping("/category/update")
	public @ResponseBody CategoryDTO updateCategory(@RequestBody CategoryDTO categoryDTO) {
		categoryService.update(categoryDTO);
		return categoryDTO;
	}
}
