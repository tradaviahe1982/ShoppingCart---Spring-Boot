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
import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.linkin.dao.SupplierDao;
import com.linkin.entity.Supplier;
import com.linkin.model.SearchSupplierDTO;

@Transactional
@Repository
public class SupplierDaoImpl implements SupplierDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void add(Supplier supplier) {
		entityManager.persist(supplier);
	}

	@Override
	public void update(Supplier supplier) {
		entityManager.merge(supplier);
	}

	@Override
	public void delete(Long id) {
		entityManager.remove(getById(id));
	}

	@Override
	public Supplier getById(Long id) {
		return entityManager.find(Supplier.class, id);
	}

	@Override
	public List<Supplier> find(SearchSupplierDTO searchSupplierDTO) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Supplier> criteriaQuery = criteriaBuilder.createQuery(Supplier.class);
		Root<Supplier> root = criteriaQuery.from(Supplier.class);

		List<Predicate> predicates = new ArrayList<>();
		if (StringUtils.isNotBlank(searchSupplierDTO.getKeyword())) {
			Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
					"%" + searchSupplierDTO.getKeyword().toLowerCase() + "%");
			predicates.add(criteriaBuilder.or(predicate));
		}
		if (StringUtils.isNotBlank(searchSupplierDTO.getAddress())) {
			predicates.add(criteriaBuilder.like(root.get("name"), searchSupplierDTO.getAddress()));
		}

		criteriaQuery.where(predicates.toArray(new Predicate[] {}));

		if (StringUtils.equals(searchSupplierDTO.getSortBy().getData(), "id")) {
			if (searchSupplierDTO.getSortBy().isAsc()) {
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("id")));
			} else {
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));
			}
		} else if (StringUtils.equals(searchSupplierDTO.getSortBy().getData(), "name")) {
			if (searchSupplierDTO.getSortBy().isAsc()) {
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("name")));
			} else {
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("name")));
			}
		} else if (StringUtils.equals(searchSupplierDTO.getSortBy().getData(), "address")) {
			if (searchSupplierDTO.getSortBy().isAsc()) {
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("address")));
			} else {
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("address")));
			}
		} else if (StringUtils.equals(searchSupplierDTO.getSortBy().getData(), "city")) {
			if (searchSupplierDTO.getSortBy().isAsc()) {
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("city")));
			} else {
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("city")));
			}
		}

		TypedQuery<Supplier> typedQuery = entityManager.createQuery(criteriaQuery.select(root));
		if (searchSupplierDTO.getStart() != null) {
			typedQuery.setFirstResult(searchSupplierDTO.getStart());
			typedQuery.setMaxResults(searchSupplierDTO.getLength());
		}
		return typedQuery.getResultList();
	}

	@Override
	public Long count(SearchSupplierDTO searchSupplierDTO) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<Supplier> root = criteriaQuery.from(Supplier.class);

		List<Predicate> predicates = new ArrayList<>();
		if (StringUtils.isNotBlank(searchSupplierDTO.getKeyword())) {
			Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
					"%" + searchSupplierDTO.getKeyword().toLowerCase() + "%");
			predicates.add(criteriaBuilder.or(predicate));
		}
		if (StringUtils.isNotBlank(searchSupplierDTO.getAddress())) {
			predicates.add(criteriaBuilder.like(root.get("name"), searchSupplierDTO.getAddress()));
		}

		criteriaQuery.where(predicates.toArray(new Predicate[] {}));
		TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery.select(criteriaBuilder.count(root)));
		return typedQuery.getSingleResult();
	}

	@Override
	public Long countTotal(SearchSupplierDTO searchSupplierDTO) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<Supplier> root = criteriaQuery.from(Supplier.class);
		TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery.select(criteriaBuilder.count(root)));
		return typedQuery.getSingleResult();
	}

}
