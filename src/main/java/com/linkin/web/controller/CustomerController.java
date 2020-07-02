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

import com.linkin.model.CustomerDTO;
import com.linkin.model.ResponseDTO;
import com.linkin.model.SearchCustomerDTO;
import com.linkin.service.CustomerService;
import com.linkin.utils.FileStore;

@Controller
@RequestMapping(value = "/staff")
public class CustomerController {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
	
	@Autowired
	CustomerService customerService;

	@GetMapping(value = "/customer/list")
	public String list(Model model) {
		return "admin/customer/list-customer";
	}

	@PostMapping(value = "/customer/list")
	public ResponseEntity<ResponseDTO<CustomerDTO>> list(@RequestBody SearchCustomerDTO searchCustomerDTO) {
		ResponseDTO<CustomerDTO> responseDTO = new ResponseDTO<>();
		responseDTO.setData(customerService.find(searchCustomerDTO));
		responseDTO.setRecordsFiltered(customerService.count(searchCustomerDTO));
		responseDTO.setRecordsTotal(customerService.countTotal(searchCustomerDTO));

		return new ResponseEntity<ResponseDTO<CustomerDTO>>(responseDTO, HttpStatus.OK);
	}

	@GetMapping(value = "/customer/delete/{id}")
	public ResponseEntity<String> delete(@PathVariable(name = "id") Long id) {
		customerService.delete(id);
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}

	@GetMapping(value = "/customer/delete-multi/{ids}")
	public ResponseEntity<String> deleteAll(@PathVariable(name = "ids") List<Long> ids) {
		for (Long id : ids) {
			try {
				customerService.delete(id);
			} catch (Exception e) {

			}
		}
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}

	@PostMapping("/customer/add")
	public @ResponseBody CustomerDTO add(@ModelAttribute CustomerDTO customerDTO) {
		try {
			// save image
			final String UPLOAD_FOLDER = FileStore.UPLOAD_FOLDER;
			if (customerDTO.getImageFile() != null && !customerDTO.getImageFile().isEmpty()) {
				String pathImage = System.currentTimeMillis() + "-customer.jpg";
				Path pathAvatar = Paths.get(UPLOAD_FOLDER + File.separator + pathImage);
				Files.write(pathAvatar, customerDTO.getImageFile().getBytes());
				customerDTO.setPathImage(pathImage);
			}
		} catch (IOException e) {
			logger.error("Upload file error");
		}
		customerService.add(customerDTO);
		return customerDTO;
	}

	@PostMapping("/customer/update")
	public @ResponseBody CustomerDTO update(@ModelAttribute CustomerDTO customerDTO) {
		try {
			// save image
			final String UPLOAD_FOLDER = FileStore.UPLOAD_FOLDER;
			if (customerDTO.getImageFile() != null && !customerDTO.getImageFile().isEmpty()) {
				String pathImage = System.currentTimeMillis() + "-customer.jpg";
				Path pathAvatar = Paths.get(UPLOAD_FOLDER + File.separator + pathImage);
				Files.write(pathAvatar, customerDTO.getImageFile().getBytes());
				customerDTO.setPathImage(pathImage);
			}
		} catch (IOException e) {
			logger.error("Upload file error");
		}
		customerService.update(customerDTO);
		return customerDTO;
	}
}
