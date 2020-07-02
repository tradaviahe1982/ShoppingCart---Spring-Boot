package com.linkin.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linkin.dao.ProductDao;
import com.linkin.entity.Category;
import com.linkin.entity.Product;
import com.linkin.entity.Supplier;
import com.linkin.model.ProductDTO;
import com.linkin.model.SearchProductDTO;
import com.linkin.service.ProductService;
import com.linkin.utils.FileStore;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
	@Autowired
	ProductDao productDao;

	@Override
	public void add(ProductDTO productDTO) {
		Product product = new Product();
		product.setName(productDTO.getName());
		product.setUnitPrice(productDTO.getUnitPrice());
		product.setDiscontinued(productDTO.getDiscontinued());
		product.setPathImage(productDTO.getPathImage());
		product.setNote(productDTO.getNote());
		if (productDTO.getCategoryId() != null) {
			product.setCategory(new Category(productDTO.getCategoryId()));
		}
		if (productDTO.getSupplierId() != null) {
			product.setSupplier(new Supplier(productDTO.getSupplierId()));
		}
		productDao.add(product);

	}

	@Override
	public void update(ProductDTO productDTO) {
		Product product = productDao.getById(productDTO.getId());
		if (product != null) {

			if (productDTO.getPathImage() != null) {
				final String UPLOAD_FOLDER = FileStore.UPLOAD_FOLDER;
				File avatarFile = new File(UPLOAD_FOLDER + File.separator + product.getPathImage());

				if (avatarFile.exists()) {
					avatarFile.delete();
				}

				product.setPathImage(productDTO.getPathImage());
			}
			product.setName(productDTO.getName());
			product.setUnitPrice(productDTO.getUnitPrice());
			product.setDiscontinued(productDTO.getDiscontinued());
			product.setNote(productDTO.getNote());
			if (productDTO.getCategoryId() != null) {
				product.setCategory(new Category(productDTO.getCategoryId()));
			}
			if (productDTO.getSupplierId() != null) {
				product.setSupplier(new Supplier(productDTO.getSupplierId()));
			}

			productDao.update(product);
		}

	}

	@Override
	public void delete(Long id) {
		Product product = productDao.getById(id);
		if (product != null) {
			productDao.delete(id);
		}
	}

	@Override
	public ProductDTO getById(Long id) {
		Product product = productDao.getById(id);
		if (product != null) {
			ProductDTO productDTO = new ProductDTO();
			productDTO.setId(product.getId());
			productDTO.setName(product.getName());
			productDTO.setUnitPrice(product.getUnitPrice());
			productDTO.setDiscontinued(product.getDiscontinued());
			productDTO.setPathImage(product.getPathImage());
			productDTO.setNote(product.getNote());
			if (product.getCategory() != null) {
				productDTO.setCategoryId(product.getCategory().getId());
				productDTO.setCategoryName(product.getCategory().getName());
			}
			if (product.getSupplier() != null) {
				productDTO.setSupplierId(product.getSupplier().getId());
				productDTO.setSupplierName(product.getSupplier().getName());
			}
			return productDTO;
		}
		return null;
	}

	@Override
	public List<ProductDTO> find(SearchProductDTO searchProducDTO) {
		List<Product> products = productDao.find(searchProducDTO);
		List<ProductDTO> productDTOs = new ArrayList<ProductDTO>();
		for (Product product : products) {
			ProductDTO productDTO = new ProductDTO();
			productDTO.setId(product.getId());
			productDTO.setName(product.getName());
			productDTO.setUnitPrice(product.getUnitPrice());
			productDTO.setDiscontinued(product.getDiscontinued());
			productDTO.setPathImage(product.getPathImage());
			productDTO.setNote(product.getNote());
			if (product.getCategory() != null) {
				productDTO.setCategoryId(product.getCategory().getId());
				productDTO.setCategoryName(product.getCategory().getName());
			}
			if (product.getSupplier() != null) {
				productDTO.setSupplierId(product.getSupplier().getId());
				productDTO.setSupplierName(product.getSupplier().getName());
			}

			productDTOs.add(productDTO);
		}
		return productDTOs;
	}

	@Override
	public Long count(SearchProductDTO searchProducDTO) {

		return productDao.count(searchProducDTO);
	}

	@Override
	public Long countTotal(SearchProductDTO searchProducDTO) {

		return productDao.countTotal(searchProducDTO);
	}

}
