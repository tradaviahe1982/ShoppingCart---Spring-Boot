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

import com.linkin.model.EmployeeDTO;
import com.linkin.model.ResponseDTO;
import com.linkin.model.SearchEmployeeDTO;
import com.linkin.service.EmployeeService;
import com.linkin.utils.FileStore;

@Controller
@RequestMapping(value = "/admin")
public class EmployeeController {
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	
	@Autowired
	EmployeeService employeeService;

	@GetMapping(value = "/employee/list")
	public String list(Model model) {
		return "admin/employee/list-employee";
	}

	@PostMapping(value = "/employee/list")
	public ResponseEntity<ResponseDTO<EmployeeDTO>> list(@RequestBody SearchEmployeeDTO searchEmployeeDTO) {
		ResponseDTO<EmployeeDTO> responseDTO = new ResponseDTO<>();
		responseDTO.setData(employeeService.find(searchEmployeeDTO));
		responseDTO.setRecordsFiltered(employeeService.count(searchEmployeeDTO));
		responseDTO.setRecordsTotal(employeeService.countTotal(searchEmployeeDTO));

		return new ResponseEntity<ResponseDTO<EmployeeDTO>>(responseDTO, HttpStatus.OK);
	}

	@GetMapping(value = "/employee/delete/{id}")
	public ResponseEntity<String> delete(@PathVariable(name = "id") Long id) {
		employeeService.delete(id);
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}

	@GetMapping(value = "/employee/delete-multi/{ids}")
	public ResponseEntity<String> deleteAll(@PathVariable(name = "ids") List<Long> ids) {
		for (Long id : ids) {
			try {
				employeeService.delete(id);
			} catch (Exception e) {

			}
		}
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}

	@PostMapping("/employee/add")
	public @ResponseBody EmployeeDTO add(@ModelAttribute EmployeeDTO employeeDTO) {
		try {
			// save image
			final String UPLOAD_FOLDER = FileStore.UPLOAD_FOLDER;
			if (employeeDTO.getImageFile() != null && !employeeDTO.getImageFile().isEmpty()) {
				String pathImage = System.currentTimeMillis() + "-employee.jpg";
				Path pathAvatar = Paths.get(UPLOAD_FOLDER + File.separator + pathImage);
				Files.write(pathAvatar, employeeDTO.getImageFile().getBytes());
				employeeDTO.setPathImage(pathImage);
			}
		} catch (IOException e) {
			logger.error("Upload file error");
		}
		employeeService.add(employeeDTO);
		return employeeDTO;
	}

	@PostMapping("/employee/update")
	public @ResponseBody EmployeeDTO update(@ModelAttribute EmployeeDTO employeeDTO) {
		try {
			// save image
			final String UPLOAD_FOLDER = FileStore.UPLOAD_FOLDER;
			if (employeeDTO.getImageFile() != null && !employeeDTO.getImageFile().isEmpty()) {
				String pathImage = System.currentTimeMillis() + "-employee.jpg";
				Path pathAvatar = Paths.get(UPLOAD_FOLDER + File.separator + pathImage);
				Files.write(pathAvatar, employeeDTO.getImageFile().getBytes());
				employeeDTO.setPathImage(pathImage);
			}
		} catch (IOException e) {
			logger.error("Upload file error");
		}
		employeeService.update(employeeDTO);
		return employeeDTO;
	}
}
