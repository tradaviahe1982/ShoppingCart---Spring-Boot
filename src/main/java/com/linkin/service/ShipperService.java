package com.linkin.service;

import java.util.List;

import com.linkin.model.SearchShipperDTO;
import com.linkin.model.ShipperDTO;

public interface ShipperService {

	void add(ShipperDTO shipperDTO);

	void update(ShipperDTO shipperDTO);

	void delete(Long id);

	ShipperDTO getById(Long id);

	List<ShipperDTO> find(SearchShipperDTO searchShipperDTO);

	Long count(SearchShipperDTO searchShipperDTO);

	Long countTotal(SearchShipperDTO searchShipperDTO);
}
