package com.linkin.dao;

import java.util.List;

import com.linkin.entity.Shipper;
import com.linkin.entity.User;
import com.linkin.model.SearchShipperDTO;
import com.linkin.model.ShipperDTO;

public interface ShipperDao {

	void add(Shipper sipper);

	void update(Shipper sipper);

	void delete(Long id);

	Shipper getById(Long id);

	List<Shipper> find(SearchShipperDTO searchShipperDTO);

	long count(SearchShipperDTO searchShipperDTO);

	long countTotal(SearchShipperDTO searchShipperDTO);

	

}
