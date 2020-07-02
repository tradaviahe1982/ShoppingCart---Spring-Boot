package com.linkin.dao.impl;

import com.linkin.dao.CategoryDao;
import com.linkin.entity.Category;
import com.linkin.model.SearchCategoryDTO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class CategoryDaoImpl implements CategoryDao {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public void add(Category category) {
		entityManager.persist(category);
	}

	@Override
	public void update(Category category) {
		entityManager.merge(category);
	}

	@Override
	public void delete(Long id) {
		entityManager.remove(getById(id));
	}

	@Override
	public Category getById(Long id) {
		return entityManager.find(Category.class, id);
	}

	@Override
	public List<Category> find(SearchCategoryDTO searchCategoryDTO) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Category> criteriaQuery = criteriaBuilder.createQuery(Category.class);
		Root<Category> root = criteriaQuery.from(Category.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (StringUtils.isNotBlank(searchCategoryDTO.getKeyword())) {
			Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
					"%" + searchCategoryDTO.getKeyword().toLowerCase() + "%");
			predicates.add(criteriaBuilder.or(predicate));
		}

		criteriaQuery.where(predicates.toArray(new Predicate[] {}));

		// order
		if (StringUtils.equals(searchCategoryDTO.getSortBy().getData(), "id")) {
			if (searchCategoryDTO.getSortBy().isAsc()) {
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("id")));
			} else {
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));
			}
		} else if (StringUtils.equals(searchCategoryDTO.getSortBy().getData(), "name")) {
			if (searchCategoryDTO.getSortBy().isAsc()) {
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("name")));
			} else {
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("name")));
			}
		} else if (StringUtils.equals(searchCategoryDTO.getSortBy().getData(), "descreption")) {
			if (searchCategoryDTO.getSortBy().isAsc()) {
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("descreption")));
			} else {
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("descreption")));
			}
			
			
		} 
		TypedQuery<Category> typedQuery = entityManager.createQuery(criteriaQuery.select(root));

		if (searchCategoryDTO.getStart() != null) {
			typedQuery.setFirstResult((searchCategoryDTO.getStart()));
			typedQuery.setMaxResults(searchCategoryDTO.getLength());
		}
		return typedQuery.getResultList();
	}

	@Override
	public Long count(SearchCategoryDTO searchCategoryDTO) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<Category> root = criteriaQuery.from(Category.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (StringUtils.isNotBlank(searchCategoryDTO.getKeyword())) {
			Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
					"%" + searchCategoryDTO.getKeyword().toLowerCase() + "%");
			predicates.add(criteriaBuilder.or(predicate));
		}

		criteriaQuery.where(predicates.toArray(new Predicate[] {}));
		TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery.select(criteriaBuilder.count(root)));
		return typedQuery.getSingleResult();
	}

	@Override
	public Long countTotal(SearchCategoryDTO searchCategoryDTO) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<Category> root = criteriaQuery.from(Category.class);
		TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery.select(criteriaBuilder.count(root)));
		return typedQuery.getSingleResult();
	}

}
