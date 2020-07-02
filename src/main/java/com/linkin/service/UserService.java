package com.linkin.service;

import java.util.List;

import com.linkin.model.SearchUserDTO;
import com.linkin.model.UserDTO;

public interface UserService {
	void addUser(UserDTO userDTO);

	void updateUser(UserDTO userDTO);

	void updateProfile(UserDTO userDTO);

	void deleteUser(Long id);

	List<UserDTO> findUsers(SearchUserDTO searchUserDTO);

	UserDTO getUserById(Long id);

	void changeAccountLock(long id);

	long countUsers(SearchUserDTO searchUserDTO);

	long countTotalUsers(SearchUserDTO searchUserDTO);

	void changePassword(UserDTO accountDTO);

	void resetPassword(UserDTO accountDTO);

	void setupUserPassword(UserDTO accountDTO);
	
}
