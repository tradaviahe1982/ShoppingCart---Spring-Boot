package com.linkin.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.linkin.dao.ShipperDao;
import com.linkin.entity.Customer;
import com.linkin.entity.Shipper;
import com.linkin.model.SearchShipperDTO;

@Repository
@Transactional

public class ShipperDaoImpl implements ShipperDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void add(Shipper sipper) {
		entityManager.persist(sipper);
	}

	@Override
	public void update(Shipper sipper) {
		entityManager.merge(sipper);
	}

	@Override
	public void delete(Long id) {
		entityManager.remove(getById(id));

	}

	@Override
	public Shipper getById(Long id) {
		return entityManager.find(Shipper.class, id);
	}

	@Override
	public List<Shipper> find(SearchShipperDTO searchShipperDTO) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Shipper> criteriaQuery = criteriaBuilder.createQuery(Shipper.class);
		Root<Shipper> root = criteriaQuery.from(Shipper.class);

		List<Predicate> predicates = new ArrayList<>();
		if (StringUtils.isNotBlank(searchShipperDTO.getKeyword())) {
			Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
					"%" + searchShipperDTO.getKeyword().toLowerCase() + "%");
			predicates.add(criteriaBuilder.or(predicate));
		}

		criteriaQuery.where(predicates.toArray(new Predicate[] {}));

		if (StringUtils.equals(searchShipperDTO.getSortBy().getData(), "id")) {
			if (searchShipperDTO.getSortBy().isAsc()) {
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("id")));
			} else {
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));
			}
		} else if (StringUtils.equals(searchShipperDTO.getSortBy().getData(), "name")) {
			if (searchShipperDTO.getSortBy().isAsc()) {
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("name")));
			} else {
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("name")));
			}
		}

		TypedQuery<Shipper> typedQuery = entityManager.createQuery(criteriaQuery.select(root));
		if (searchShipperDTO.getStart() != null) {
			typedQuery.setFirstResult(searchShipperDTO.getStart());
			typedQuery.setMaxResults(searchShipperDTO.getLength());
		}
		return typedQuery.getResultList();
	}

	@Override
	public long count(SearchShipperDTO searchShipperDTO) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<Shipper> root = criteriaQuery.from(Shipper.class);

		List<Predicate> predicates = new ArrayList<>();
		if (StringUtils.isNotBlank(searchShipperDTO.getKeyword())) {
			Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
					"%" + searchShipperDTO.getKeyword().toLowerCase() + "%");
			predicates.add(criteriaBuilder.or(predicate));
		}

		criteriaQuery.where(predicates.toArray(new Predicate[] {}));
		TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery.select(criteriaBuilder.count(root)));
		return typedQuery.getSingleResult();
	}

	@Override
	public long countTotal(SearchShipperDTO searchShipperDTO) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<Shipper> root = criteriaQuery.from(Shipper.class);
		TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery.select(criteriaBuilder.count(root)));
		return typedQuery.getSingleResult();
	}
}
