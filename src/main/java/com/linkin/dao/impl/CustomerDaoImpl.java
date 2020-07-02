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

import com.linkin.dao.CustomerDao;
import com.linkin.entity.Customer;
import com.linkin.model.SearchCustomerDTO;

@Transactional
@Repository
public class CustomerDaoImpl implements CustomerDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void add(Customer customer) {
		entityManager.persist(customer);
	}

	@Override
	public void update(Customer customer) {
		entityManager.merge(customer);
	}

	@Override
	public void delete(Long id) {
		entityManager.remove(getById(id));
	}

	@Override
	public Customer getById(Long id) {
		return entityManager.find(Customer.class, id);
	}

	@Override
	public List<Customer> find(SearchCustomerDTO searchCustomerDTO) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Customer> criteriaQuery = criteriaBuilder.createQuery(Customer.class);
		Root<Customer> root = criteriaQuery.from(Customer.class);

		List<Predicate> predicates = new ArrayList<>();
		if (StringUtils.isNotBlank(searchCustomerDTO.getKeyword())) {
			Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
					"%" + searchCustomerDTO.getKeyword().toLowerCase() + "%");
			predicates.add(criteriaBuilder.or(predicate));
		}
		if (StringUtils.isNotBlank(searchCustomerDTO.getAddress())) {
			predicates.add(criteriaBuilder.like(root.get("name"), searchCustomerDTO.getAddress()));
		}

		criteriaQuery.where(predicates.toArray(new Predicate[] {}));

		if (StringUtils.equals(searchCustomerDTO.getSortBy().getData(), "id")) {
			if (searchCustomerDTO.getSortBy().isAsc()) {
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("id")));
			} else {
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));
			}
		} else if (StringUtils.equals(searchCustomerDTO.getSortBy().getData(), "name")) {
			if (searchCustomerDTO.getSortBy().isAsc()) {
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("name")));
			} else {
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("name")));
			}
		} else if (StringUtils.equals(searchCustomerDTO.getSortBy().getData(), "address")) {
			if (searchCustomerDTO.getSortBy().isAsc()) {
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("address")));
			} else {
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("address")));
			}
		} else if (StringUtils.equals(searchCustomerDTO.getSortBy().getData(), "city")) {
			if (searchCustomerDTO.getSortBy().isAsc()) {
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("city")));
			} else {
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("city")));
			}
		}

		TypedQuery<Customer> typedQuery = entityManager.createQuery(criteriaQuery.select(root));
		if (searchCustomerDTO.getStart() != null) {
			typedQuery.setFirstResult(searchCustomerDTO.getStart());
			typedQuery.setMaxResults(searchCustomerDTO.getLength());
		}
		return typedQuery.getResultList();
	}

	@Override
	public Long count(SearchCustomerDTO searchCustomerDTO) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<Customer> root = criteriaQuery.from(Customer.class);

		List<Predicate> predicates = new ArrayList<>();
		if (StringUtils.isNotBlank(searchCustomerDTO.getKeyword())) {
			Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
					"%" + searchCustomerDTO.getKeyword().toLowerCase() + "%");
			predicates.add(criteriaBuilder.or(predicate));
		}
		if (StringUtils.isNotBlank(searchCustomerDTO.getAddress())) {
			predicates.add(criteriaBuilder.like(root.get("name"), searchCustomerDTO.getAddress()));
		}

		criteriaQuery.where(predicates.toArray(new Predicate[] {}));
		TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery.select(criteriaBuilder.count(root)));
		return typedQuery.getSingleResult();
	}

	@Override
	public Long countTotal(SearchCustomerDTO searchCustomerDTO) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<Customer> root = criteriaQuery.from(Customer.class);
		TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery.select(criteriaBuilder.count(root)));
		return typedQuery.getSingleResult();
	}

}
