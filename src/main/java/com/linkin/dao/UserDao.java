package com.linkin.dao;

import java.util.List;

import com.linkin.entity.User;
import com.linkin.model.SearchUserDTO;

public interface UserDao {
	void add(User User);

	void update(User User);

	void delete(User User);

	List<User> find(SearchUserDTO searchUserDTO);

	long count(SearchUserDTO searchUserDTO);

	long countTotal(SearchUserDTO searchUserDTO);

	User getById(Long id);

	User getByPhone(String phone);

}
