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

import com.linkin.model.ResponseDTO;
import com.linkin.model.SearchSupplierDTO;
import com.linkin.model.SupplierDTO;
import com.linkin.service.SupplierService;
import com.linkin.utils.FileStore;

@Controller
@RequestMapping(value = "/admin")
public class SupplierController {
	private static final Logger logger = LoggerFactory.getLogger(SupplierController.class);
	@Autowired
	SupplierService supplierService;

	@GetMapping(value = "/supplier/list")
	public String list(Model model) {
		SearchSupplierDTO searchSupplierDTO = new SearchSupplierDTO();
		searchSupplierDTO.setStart(null);
		model.addAttribute("supplierService", supplierService.find(searchSupplierDTO));
		return "admin/supplier/list-supplier";
	}

	@PostMapping(value = "/supplier/list")
	public ResponseEntity<ResponseDTO<SupplierDTO>> listCategory(@RequestBody SearchSupplierDTO searchSupplierDTO) {
		ResponseDTO<SupplierDTO> responseDTO = new ResponseDTO<>();
		responseDTO.setData(supplierService.find(searchSupplierDTO));
		responseDTO.setRecordsFiltered(supplierService.count(searchSupplierDTO));
		responseDTO.setRecordsTotal(supplierService.countTotal(searchSupplierDTO));

		return new ResponseEntity<ResponseDTO<SupplierDTO>>(responseDTO, HttpStatus.OK);
	}

	@GetMapping(value = "/supplier/delete/{id}")
	public ResponseEntity<String> delete(@PathVariable(name = "id") Long id) {
		supplierService.delete(id);
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}

	@GetMapping(value = "/supplier/delete-multi/{ids}")
	public ResponseEntity<String> deleteAll(@PathVariable(name = "ids") List<Long> ids) {
		for (Long id : ids) {
			try {
				supplierService.delete(id);
			} catch (Exception e) {

			}
		}
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}

	@PostMapping("/supplier/add")
	public @ResponseBody SupplierDTO add(@ModelAttribute SupplierDTO supplierDTO) {
		try {
			// save image
			final String UPLOAD_FOLDER = FileStore.UPLOAD_FOLDER;
			if (supplierDTO.getImageFile() != null && !supplierDTO.getImageFile().isEmpty()) {
				String image = System.currentTimeMillis() + "-supplier.jpg";
				Path pathAvatar = Paths.get(UPLOAD_FOLDER + File.separator + image);
				Files.write(pathAvatar, supplierDTO.getImageFile().getBytes());
				supplierDTO.setImage(image);
			}
		} catch (IOException e) {
			logger.error("Upload file error");
		}
		supplierService.add(supplierDTO);
		return supplierDTO;
	}

	@PostMapping("/supplier/update")
	public @ResponseBody SupplierDTO update(@ModelAttribute SupplierDTO supplierDTO) {
		try {
			final String UPLOAD_FOLDER = FileStore.UPLOAD_FOLDER;
			if (supplierDTO.getImageFile() != null && !supplierDTO.getImageFile().isEmpty()) {
				String image = System.currentTimeMillis() + "-supplier.jpg";
				Path pathAvatar = Paths.get(UPLOAD_FOLDER + File.separator + image);
				Files.write(pathAvatar, supplierDTO.getImageFile().getBytes());
				supplierDTO.setImage(image);
			}
		} catch (IOException e) {
			logger.error("Upload file error");
		}
		supplierService.update(supplierDTO);
		return supplierDTO;
	}
}
