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

import com.linkin.dao.ProductDao;
import com.linkin.entity.Product;
import com.linkin.model.SearchProductDTO;

@Repository
@Transactional
public class ProductDaoImpl implements ProductDao {
	@PersistenceContext
	EntityManager entityManager;

	@Override
	public void add(Product product) {
		entityManager.persist(product);

	}

	@Override
	public void update(Product product) {
		entityManager.merge(product);

	}

	@Override
	public void delete(Long id) {
		entityManager.remove(getById(id));
	}

	@Override
	public Product getById(Long id) {
		return entityManager.find(Product.class, id);
	}

	@Override
	public List<Product> find(SearchProductDTO searchProductDTO) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
		Root<Product> root = criteriaQuery.from(Product.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (StringUtils.isNotBlank(searchProductDTO.getKeyword())) {
			Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
					"%" + searchProductDTO.getKeyword().toLowerCase() + "%");
			predicates.add(criteriaBuilder.or(predicate));
		}

		criteriaQuery.where(predicates.toArray(new Predicate[] {}));

		// oder
		if (StringUtils.equals(searchProductDTO.getSortBy().getData(), "id")) {
			if (searchProductDTO.getSortBy().isAsc()) {
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("id")));
			} else {
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));
			}
		} else if (StringUtils.equals(searchProductDTO.getSortBy().getData(), "name")) {
			if (searchProductDTO.getSortBy().isAsc()) {
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("name")));
			} else {
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("name")));
			}
		} else if (StringUtils.equals(searchProductDTO.getSortBy().getData(), "unitPrice")) {
			if (searchProductDTO.getSortBy().isAsc()) {
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("unitPrice")));
			} else {
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("unitPrice")));
			}
		} else if (StringUtils.equals(searchProductDTO.getSortBy().getData(), "discontinued")) {
			if (searchProductDTO.getSortBy().isAsc()) {
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("discontinued")));
			} else {
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("discontinued")));
			}

		}

		TypedQuery<Product> typedQuery = entityManager.createQuery(criteriaQuery.select(root));

		if (searchProductDTO.getStart() != null) {
			typedQuery.setFirstResult((searchProductDTO.getStart()));
			typedQuery.setMaxResults(searchProductDTO.getLength());
		}
		return typedQuery.getResultList();
	}

	@Override
	public Long count(SearchProductDTO searchProductDTO) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<Product> root = criteriaQuery.from(Product.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (StringUtils.isNotBlank(searchProductDTO.getKeyword())) {
			Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
					"%" + searchProductDTO.getKeyword().toLowerCase() + "%");
			predicates.add(criteriaBuilder.or(predicate));
		}

		criteriaQuery.where(predicates.toArray(new Predicate[] {}));
		TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery.select(criteriaBuilder.count(root)));
		return typedQuery.getSingleResult();
	}

	@Override
	public Long countTotal(SearchProductDTO searchProductDTO) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<Product> root = criteriaQuery.from(Product.class);
		TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery.select(criteriaBuilder.count(root)));
		return typedQuery.getSingleResult();
	}

}
