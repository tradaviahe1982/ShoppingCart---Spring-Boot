package com.linkin.service.impl;

import com.linkin.dao.CategoryDao;
import com.linkin.entity.Category;
import com.linkin.model.CategoryDTO;
import com.linkin.model.SearchCategoryDTO;
import com.linkin.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	CategoryDao categoryDao;

	@Override
	public void add(CategoryDTO categoryDTO) {
		Category category = new Category();
		category.setName(categoryDTO.getName());
		category.setDescreption(categoryDTO.getDescreption());
		categoryDao.add(category);
	}

	@Override
	public void update(CategoryDTO categoryDTO) {
		Category category = categoryDao.getById(categoryDTO.getId());
		if (category != null) {
			category.setName(categoryDTO.getName());
			category.setDescreption(categoryDTO.getDescreption());
			categoryDao.update(category);
		}
	}

	@Override
	public void delete(Long id) {
		Category category = categoryDao.getById(id);
		if (category != null) {
			categoryDao.delete(id);
		}
	}

	public CategoryDTO convertDTO(Category category) {
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setId(category.getId());
		categoryDTO.setName(category.getName());
		categoryDTO.setDescreption(category.getDescreption());
		return categoryDTO;
	}

	@Override
	public CategoryDTO getById(Long id) {
		Category category = categoryDao.getById(id);
		if (category != null) {
			CategoryDTO categoryDTO = new CategoryDTO();
			categoryDTO.setId(category.getId());
			categoryDTO.setName(category.getName());
			categoryDTO.setDescreption(category.getDescreption());
			return categoryDTO;
		}
		return null;
	}

	@Override
	public List<CategoryDTO> find(SearchCategoryDTO searchCategoryDTO) {
		List<Category> categories = categoryDao.find(searchCategoryDTO);
		List<CategoryDTO> categoryDTOs = new ArrayList<CategoryDTO>();
		for (Category category : categories) {
			CategoryDTO categoryDTO = new CategoryDTO();
			categoryDTO.setId(category.getId());
			categoryDTO.setName(category.getName());
			categoryDTO.setDescreption(category.getDescreption());
			categoryDTOs.add(categoryDTO);
		}
		return categoryDTOs;
	}

	@Override
	public Long count(SearchCategoryDTO searchCategoryDTO) {
		return categoryDao.count(searchCategoryDTO);
	}

	@Override
	public Long countTotal(SearchCategoryDTO searchCategoryDTO) {
		return categoryDao.countTotal(searchCategoryDTO);
	}

}
