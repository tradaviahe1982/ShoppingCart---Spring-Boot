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

import com.linkin.dao.CartItemDao;
import com.linkin.entity.CartItem;
import com.linkin.model.SearchCartItemDTO;

@Repository
@Transactional
public class CartItemDaoImpl implements CartItemDao {

	@PersistenceContext
    
	private EntityManager entityManager;

	@Override
	public void add(CartItem cartItem) {
		entityManager.persist(cartItem);
	}

	@Override
	public void edit(CartItem cartItem) {
		entityManager.merge(cartItem);
	}

	@Override
	public void delete(Long id) {
		entityManager.remove(getById(id));
	}

	@Override
	public CartItem getById(Long id) {
		return entityManager.find(CartItem.class, id);
	}

	@Override
	public List<CartItem> find(SearchCartItemDTO searchCartItemDTO) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CartItem> criteriaQuery = criteriaBuilder.createQuery(CartItem.class);
		Root<CartItem> root = criteriaQuery.from(CartItem.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (StringUtils.isNotBlank(searchCartItemDTO.getKeyword())) {
			
		}

		criteriaQuery.where(predicates.toArray(new Predicate[] {}));

		// oder
		if (StringUtils.equals(searchCartItemDTO.getSortBy().getData(), "id")) {
			if (searchCartItemDTO.getSortBy().isAsc()) {
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("id")));
			} else {
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));
			}
		} 

		TypedQuery<CartItem> typedQuery = entityManager.createQuery(criteriaQuery.select(root));

		if (searchCartItemDTO.getStart() != null) {
			typedQuery.setFirstResult((searchCartItemDTO.getStart()));
			typedQuery.setMaxResults(searchCartItemDTO.getLength());
		}
		return typedQuery.getResultList();
	}

	@Override
	public Long count(SearchCartItemDTO searchCartItemDTO) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<CartItem> root = criteriaQuery.from(CartItem.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (StringUtils.isNotBlank(searchCartItemDTO.getKeyword())) {
//			Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
//					"%" + searchCartItemDTO.getKeyword().toLowerCase() + "%");
//			predicates.add(criteriaBuilder.or(predicate));
		}

		criteriaQuery.where(predicates.toArray(new Predicate[] {}));
		TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery.select(criteriaBuilder.count(root)));
		return typedQuery.getSingleResult();
	}

	@Override
	public Long countTotal(SearchCartItemDTO searchCartItemDTO) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<CartItem> root = criteriaQuery.from(CartItem.class);

		List<Predicate> predicates = new ArrayList<Predicate>();
		criteriaQuery.where(predicates.toArray(new Predicate[] {}));

		TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery.select(criteriaBuilder.count(root)));
		return typedQuery.getSingleResult();
	}

}
