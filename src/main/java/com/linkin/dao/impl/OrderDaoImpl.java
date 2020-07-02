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

import com.linkin.dao.OrderDao;
import com.linkin.entity.Order;
import com.linkin.model.SearchOrderDTO;
import com.linkin.utils.DateTimeUtils;

@Repository
@Transactional
public class OrderDaoImpl implements OrderDao {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public void add(Order order) {
		entityManager.persist(order);
	}

	@Override
	public void update(Order order) {
		entityManager.merge(order);
	}

	@Override
	public void delete(Long id) {
		entityManager.remove(getById(id));
	}

	@Override
	public Order getById(Long id) {
		return entityManager.find(Order.class, id);
	}

	@Override
	public List<Order> find(SearchOrderDTO searchOrderDTO) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
		Root<Order> root = criteriaQuery.from(Order.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (StringUtils.isNotBlank(searchOrderDTO.getKeyword())) {
			Predicate predicate1 = criteriaBuilder.like(criteriaBuilder.lower(root.get("note")),
					"%" + searchOrderDTO.getKeyword().toLowerCase() + "%");
			predicates.add(criteriaBuilder.or(predicate1));
		}

		if (searchOrderDTO.getStatusOrder() != null) {
			predicates.add(criteriaBuilder.equal(root.get("statusOrder"), searchOrderDTO.getStatusOrder()));

		}

		if (StringUtils.isNotBlank(searchOrderDTO.getFromDate())) {
			try {
				Predicate predicate = criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"),
						DateTimeUtils.getStartOfDay(DateTimeUtils.parseDate(searchOrderDTO.getFromDate(), DateTimeUtils.DD_MM_YYYY)));
				predicates.add(predicate);
			} catch (RuntimeException exception) {

			}
		}

		if (StringUtils.isNotBlank(searchOrderDTO.getToDate())) {
			try {
				Predicate predicate = criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"),
						DateTimeUtils.getEndOfDay(DateTimeUtils.parseDate(searchOrderDTO.getToDate(), DateTimeUtils.DD_MM_YYYY)));
				predicates.add(predicate);
			} catch (RuntimeException exception) {

			}
		}

		criteriaQuery.where(predicates.toArray(new Predicate[] {}));
		if (StringUtils.equals(searchOrderDTO.getSortBy().getData(), "id")) {
			if (searchOrderDTO.getSortBy().isAsc()) {
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("id")));
			} else {
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));
			}
		} else if (StringUtils.equals(searchOrderDTO.getSortBy().getData(), "note")) {
			if (searchOrderDTO.getSortBy().isAsc()) {
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("note")));
			} else {
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("note")));
			}
		}

		TypedQuery<Order> typedQuery = entityManager.createQuery(criteriaQuery.select(root));

		if (searchOrderDTO.getStart() != null) {
			typedQuery.setFirstResult((searchOrderDTO.getStart()));
			typedQuery.setMaxResults(searchOrderDTO.getLength());
		}

		return typedQuery.getResultList();
	}

	@Override
	public Long count(SearchOrderDTO searchOrderDTO) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<Order> root = criteriaQuery.from(Order.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (StringUtils.isNotBlank(searchOrderDTO.getKeyword())) {
			Predicate predicate1 = criteriaBuilder.like(criteriaBuilder.lower(root.get("note")),
					"%" + searchOrderDTO.getKeyword().toLowerCase() + "%");
			predicates.add(criteriaBuilder.or(predicate1));
		}

		if (searchOrderDTO.getStatusOrder() != null) {
			predicates.add(criteriaBuilder.equal(root.get("statusOrder"), searchOrderDTO.getStatusOrder()));

		}

		if (StringUtils.isNotBlank(searchOrderDTO.getFromDate())) {
			try {
				Predicate predicate = criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"),
						DateTimeUtils.parseDate(searchOrderDTO.getFromDate(), DateTimeUtils.DD_MM_YYYY));
				predicates.add(predicate);
			} catch (RuntimeException exception) {

			}
		}

		if (StringUtils.isNotBlank(searchOrderDTO.getToDate())) {
			try {
				Predicate predicate = criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"),
						DateTimeUtils.parseDate(searchOrderDTO.getToDate(), DateTimeUtils.DD_MM_YYYY));
				predicates.add(predicate);
			} catch (RuntimeException exception) {

			}
		}

		criteriaQuery.where(predicates.toArray(new Predicate[] {}));
		TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery.select(criteriaBuilder.count(root)));
		return typedQuery.getSingleResult();
	}

	@Override
	public Long countTotal(SearchOrderDTO searchOrderDTO) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<Order> root = criteriaQuery.from(Order.class);
		TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery.select(criteriaBuilder.count(root)));
		return typedQuery.getSingleResult();
	}

}
