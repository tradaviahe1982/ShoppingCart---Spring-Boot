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

import com.linkin.dao.EmployeeDao;
import com.linkin.entity.Employee;
import com.linkin.model.SearchEmployeeDTO;

@Transactional
@Repository
public class EmployeeDaoImpl implements EmployeeDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void add(Employee employee) {
		entityManager.persist(employee);
	}

	@Override
	public void update(Employee employee) {
		entityManager.merge(employee);
	}

	@Override
	public void delete(Long id) {
		entityManager.remove(getById(id));
	}

	@Override
	public Employee getById(Long id) {
		return entityManager.find(Employee.class, id);
	}

	@Override
	public List<Employee> find(SearchEmployeeDTO searchEmployeeDTO) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
		Root<Employee> root = criteriaQuery.from(Employee.class);

		List<Predicate> predicates = new ArrayList<>();
		if (StringUtils.isNotBlank(searchEmployeeDTO.getKeyword())) {
			Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
					"%" + searchEmployeeDTO.getKeyword().toLowerCase() + "%");
			predicates.add(criteriaBuilder.or(predicate));
		}
		if (StringUtils.isNotBlank(searchEmployeeDTO.getAddress())) {
			predicates.add(criteriaBuilder.like(root.get("name"), searchEmployeeDTO.getAddress()));
		}

		criteriaQuery.where(predicates.toArray(new Predicate[] {}));

		if (StringUtils.equals(searchEmployeeDTO.getSortBy().getData(), "id")) {
			if (searchEmployeeDTO.getSortBy().isAsc()) {
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("id")));
			} else {
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));
			}
		} else if (StringUtils.equals(searchEmployeeDTO.getSortBy().getData(), "name")) {
			if (searchEmployeeDTO.getSortBy().isAsc()) {
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("name")));
			} else {
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("name")));
			}
		} else if (StringUtils.equals(searchEmployeeDTO.getSortBy().getData(), "address")) {
			if (searchEmployeeDTO.getSortBy().isAsc()) {
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("address")));
			} else {
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("address")));
			}
		} else if (StringUtils.equals(searchEmployeeDTO.getSortBy().getData(), "city")) {
			if (searchEmployeeDTO.getSortBy().isAsc()) {
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("city")));
			} else {
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("city")));
			}
		}

		TypedQuery<Employee> typedQuery = entityManager.createQuery(criteriaQuery.select(root));
		if (searchEmployeeDTO.getStart() != null) {
			typedQuery.setFirstResult(searchEmployeeDTO.getStart());
			typedQuery.setMaxResults(searchEmployeeDTO.getLength());
		}
		return typedQuery.getResultList();
	}

	@Override
	public Long count(SearchEmployeeDTO searchEmployeeDTO) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<Employee> root = criteriaQuery.from(Employee.class);

		List<Predicate> predicates = new ArrayList<>();
		if (StringUtils.isNotBlank(searchEmployeeDTO.getKeyword())) {
			Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
					"%" + searchEmployeeDTO.getKeyword().toLowerCase() + "%");
			predicates.add(criteriaBuilder.or(predicate));
		}
		if (StringUtils.isNotBlank(searchEmployeeDTO.getAddress())) {
			predicates.add(criteriaBuilder.like(root.get("name"), searchEmployeeDTO.getAddress()));
		}

		criteriaQuery.where(predicates.toArray(new Predicate[] {}));
		TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery.select(criteriaBuilder.count(root)));
		return typedQuery.getSingleResult();
	}

	@Override
	public Long countTotal(SearchEmployeeDTO searchEmployeeDTO) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<Employee> root = criteriaQuery.from(Employee.class);
		TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery.select(criteriaBuilder.count(root)));
		return typedQuery.getSingleResult();
	}

}
