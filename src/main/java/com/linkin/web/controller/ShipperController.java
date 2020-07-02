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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkin.model.ShipperDTO;
import com.linkin.model.ResponseDTO;
import com.linkin.model.SearchShipperDTO;
import com.linkin.model.SearchShipperDTO;
import com.linkin.model.ShipperDTO;
import com.linkin.service.ShipperService;
import com.linkin.utils.FileStore;

@Controller
@RequestMapping(value = "/staff")
public class ShipperController {
	private static final Logger logger = LoggerFactory.getLogger(ShipperController.class);
	@Autowired
	ShipperService shipperService;

	@GetMapping(value = "/shipper/list")
	public String list(Model model) {
		return "admin/shipper/list-shipper";
	}

	@PostMapping(value = "/shipper/list")
	public ResponseEntity<ResponseDTO<ShipperDTO>> list(@RequestBody SearchShipperDTO searchShipperDTO) {
		ResponseDTO<ShipperDTO> responseDTO = new ResponseDTO<>();
		responseDTO.setData(shipperService.find(searchShipperDTO));
		responseDTO.setRecordsFiltered(shipperService.count(searchShipperDTO));
		responseDTO.setRecordsTotal(shipperService.countTotal(searchShipperDTO));

		return new ResponseEntity<ResponseDTO<ShipperDTO>>(responseDTO, HttpStatus.OK);
	}

	@GetMapping(value = "/shipper/delete/{id}")
	public ResponseEntity<String> delete(@PathVariable(name = "id") Long id) {
		shipperService.delete(id);
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}

	@GetMapping(value = "/shipper/delete-multi/{ids}")
	public ResponseEntity<String> deleteAll(@PathVariable(name = "ids") List<Long> ids) {
		for (Long id : ids) {
			try {
				shipperService.delete(id);
			} catch (Exception e) {

			}
		}
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}

	@PostMapping("/shipper/add")
	public @ResponseBody ShipperDTO add(@ModelAttribute ShipperDTO shipperDTO) {
		try {
			// save image
			final String UPLOAD_FOLDER = FileStore.UPLOAD_FOLDER;
			if (shipperDTO.getImageFile() != null && !shipperDTO.getImageFile().isEmpty()) {
				String pathImage = System.currentTimeMillis() + "-shipper.jpg";
				Path pathAvatar = Paths.get(UPLOAD_FOLDER + File.separator + pathImage);
				Files.write(pathAvatar, shipperDTO.getImageFile().getBytes());
				shipperDTO.setPathImage(pathImage);
			}
		} catch (IOException e) {
			logger.error("Upload file error");
		}
		shipperService.add(shipperDTO);
		return shipperDTO;
	}

	@PostMapping("/shipper/update")
	public @ResponseBody ShipperDTO update(@ModelAttribute ShipperDTO shipperDTO) {
		try {
			// save image
			final String UPLOAD_FOLDER = FileStore.UPLOAD_FOLDER;
			if (shipperDTO.getImageFile() != null && !shipperDTO.getImageFile().isEmpty()) {
				String pathImage = System.currentTimeMillis() + "-shipper.jpg";
				Path pathAvatar = Paths.get(UPLOAD_FOLDER + File.separator + pathImage);
				Files.write(pathAvatar, shipperDTO.getImageFile().getBytes());
				shipperDTO.setPathImage(pathImage);
			}
		} catch (IOException e) {
			logger.error("Upload file error");
		}
		shipperService.update(shipperDTO);
		return shipperDTO;
	}
}
