package com.linkin.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linkin.dao.SupplierDao;
import com.linkin.entity.Supplier;
import com.linkin.model.SearchSupplierDTO;
import com.linkin.model.SupplierDTO;
import com.linkin.service.SupplierService;
import com.linkin.utils.FileStore;

@Transactional
@Service
public class SupplierServiceImpl implements SupplierService {

	@Autowired
	private SupplierDao supplierDao;

	@Override
	public void add(SupplierDTO supplierDTO) {
		Supplier supplier = new Supplier();
		supplier.setName(supplierDTO.getName());
		supplier.setAddress(supplierDTO.getAddress());
		supplier.setCity(supplierDTO.getCity());
		supplier.setPhone(supplierDTO.getPhone());
		supplier.setNote(supplierDTO.getNote());
		supplier.setImage(supplierDTO.getImage());
		// supplier.setStatus(supplierDTO.getStatus());
		supplierDao.add(supplier);
	}

	@Override
	public void update(SupplierDTO supplierDTO) {
		Supplier supplier = supplierDao.getById(supplierDTO.getId());
		supplier.setId(supplierDTO.getId());
		supplier.setName(supplierDTO.getName());
		supplier.setAddress(supplierDTO.getAddress());
		supplier.setCity(supplierDTO.getCity());
		supplier.setPhone(supplierDTO.getPhone());
		supplier.setNote(supplierDTO.getNote());
//		supplier.setStatus(supplierDTO.getStatus());
		if (supplierDTO.getImage() != null) {
			final String UPLOAD_FOLDER = FileStore.UPLOAD_FOLDER;
			File avatarFile = new File(UPLOAD_FOLDER + File.separator + supplier.getImage());

			if (avatarFile.exists()) {
				avatarFile.delete();
			}

			supplier.setImage(supplierDTO.getImage());
		}
		supplierDao.update(supplier);
	}

	@Override
	public void delete(Long id) {
		Supplier Supplier = supplierDao.getById(id);
		if (Supplier != null) {
			supplierDao.delete(id);
		}
	}

	@Override
	public SupplierDTO getById(Long id) {
		Supplier supplier = supplierDao.getById(id);
		if (supplier != null) {
			SupplierDTO supplierDTO = new SupplierDTO();
			supplierDTO.setId(supplier.getId());
			supplierDTO.setName(supplier.getName());
			supplierDTO.setAddress(supplier.getAddress());
			supplierDTO.setCity(supplier.getCity());
			supplierDTO.setNote(supplier.getNote());
			supplierDTO.setPhone(supplier.getPhone());
			supplierDTO.setImage(supplier.getImage());
			// supplierDTO.setStatus(supplier.getStatus());
			return supplierDTO;
		}
		return null;
	}

	@Override
	public List<SupplierDTO> find(SearchSupplierDTO searchSupplierDTO) {
		List<Supplier> customers = supplierDao.find(searchSupplierDTO);
		List<SupplierDTO> customerDTOs = new ArrayList<SupplierDTO>();
		for (Supplier supplier : customers) {
			SupplierDTO supplierDTO = new SupplierDTO();
			supplierDTO.setId(supplier.getId());
			supplierDTO.setName(supplier.getName());
			supplierDTO.setAddress(supplier.getAddress());
			supplierDTO.setCity(supplier.getCity());
			supplierDTO.setNote(supplier.getNote());
			supplierDTO.setPhone(supplier.getPhone());
			supplierDTO.setImage(supplier.getImage());
	//		supplierDTO.setStatus(supplier.getStatus());
			customerDTOs.add(supplierDTO);
		}
		return customerDTOs;
	}

	@Override
	public Long count(SearchSupplierDTO searchSupplierDTO) {
		return supplierDao.count(searchSupplierDTO);
	}

	@Override
	public Long countTotal(SearchSupplierDTO searchSupplierDTO) {
		return supplierDao.countTotal(searchSupplierDTO);
	}

}
