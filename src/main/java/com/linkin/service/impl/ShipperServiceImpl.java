package com.linkin.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkin.dao.ShipperDao;
import com.linkin.entity.Shipper;
import com.linkin.model.SearchShipperDTO;
import com.linkin.model.ShipperDTO;
import com.linkin.service.ShipperService;
import com.linkin.utils.DateTimeUtils;
import com.linkin.utils.FileStore;

@Service
@Transactional
public class ShipperServiceImpl implements ShipperService {

	@Autowired
	private ShipperDao shipperDao;

	@Override
	public void add(ShipperDTO shipperDTO) {
		Shipper shipper = new Shipper();
		shipper.setName(shipperDTO.getName());
		shipper.setAddress(shipperDTO.getAddress());
		shipper.setPhone(shipperDTO.getPhone());
		shipper.setNote(shipperDTO.getNote());
		if (StringUtils.isNotBlank(shipperDTO.getBirthDay())) {
			shipper.setBirthDay(DateTimeUtils.parseDate(shipperDTO.getBirthDay(), DateTimeUtils.DD_MM_YYYY));
		}
		
		shipper.setPathImage(shipperDTO.getPathImage());
		shipper.setStatus(shipperDTO.getStatus());
		shipperDao.add(shipper);
	}

	@Override
	public void update(ShipperDTO shipperDTO) {
		Shipper shipper = shipperDao.getById(shipperDTO.getId());
		shipper.setName(shipperDTO.getName());
		shipper.setAddress(shipperDTO.getAddress());
		shipper.setPhone(shipperDTO.getPhone());
		shipper.setNote(shipperDTO.getNote());
		if (StringUtils.isNotBlank(shipperDTO.getBirthDay())) {
			shipper.setBirthDay(DateTimeUtils.parseDate(shipperDTO.getBirthDay(), DateTimeUtils.DD_MM_YYYY));
		}
		if (shipperDTO.getPathImage() != null) {
			final String UPLOAD_FOLDER = FileStore.UPLOAD_FOLDER;
			File avatarFile = new File(UPLOAD_FOLDER + File.separator + shipper.getPathImage());

			if (avatarFile.exists()) {
				avatarFile.delete();
			}

			shipper.setPathImage(shipperDTO.getPathImage());
		}
		shipper.setStatus(shipperDTO.getStatus());
		shipperDao.update(shipper);

	}

	@Override
	public void delete(Long id) {
		Shipper Shipper = shipperDao.getById(id);
		if (Shipper != null) {
			shipperDao.delete(id);
		}
	}

	@Override
	public ShipperDTO getById(Long id) {
		Shipper shipper = shipperDao.getById(id);
		if (shipper != null) {
			ShipperDTO shipperDTO = new ShipperDTO();
			shipperDTO.setId(shipper.getId());
			shipperDTO.setName(shipper.getName());
			shipperDTO.setAddress(shipper.getAddress());
			shipperDTO.setNote(shipper.getNote());
			shipperDTO.setPhone(shipper.getPhone());
			if (shipper.getBirthDay() != null) {
				shipperDTO.setBirthDay(DateTimeUtils.formatDate(shipper.getBirthDay(), DateTimeUtils.DD_MM_YYYY));
			}
			shipperDTO.setPathImage(shipper.getPathImage());
			shipperDTO.setStatus(shipper.getStatus());

			return shipperDTO;
		}
		return null;
	}

	@Override
	public List<ShipperDTO> find(SearchShipperDTO searchShipperDTO) {
		List<Shipper> shippers = shipperDao.find(searchShipperDTO);
		List<ShipperDTO> shipperDTOs = new ArrayList<ShipperDTO>();
		for (Shipper shipper : shippers) {
			ShipperDTO shipperDTO = new ShipperDTO();
			shipperDTO.setId(shipper.getId());
			shipperDTO.setName(shipper.getName());
			shipperDTO.setAddress(shipper.getAddress());
			shipperDTO.setNote(shipper.getNote());
			shipperDTO.setPhone(shipper.getPhone());
			if (shipper.getBirthDay() != null) {
				shipperDTO.setBirthDay(DateTimeUtils.formatDate(shipper.getBirthDay(), DateTimeUtils.DD_MM_YYYY));
			}
			shipperDTO.setPathImage(shipper.getPathImage());
			shipperDTO.setStatus(shipper.getStatus());
			shipperDTOs.add(shipperDTO);
		}
		return shipperDTOs;
	}

	@Override
	public Long count(SearchShipperDTO searchShipperDTO) {
		return shipperDao.count(searchShipperDTO);
	}

	@Override
	public Long countTotal(SearchShipperDTO searchShipperDTO) {
		return shipperDao.countTotal(searchShipperDTO);
	}

}
