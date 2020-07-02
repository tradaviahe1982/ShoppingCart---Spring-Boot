package com.linkin.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.linkin.dao.EmployeeDao;
import com.linkin.dao.UserDao;
import com.linkin.entity.Employee;
import com.linkin.entity.Role;
import com.linkin.entity.User;
import com.linkin.model.SearchUserDTO;
import com.linkin.model.UserDTO;
import com.linkin.model.UserPrincipal;
import com.linkin.service.UserService;
import com.linkin.utils.PasswordGenerator;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private EmployeeDao employeeDao;

	@Autowired
	private SessionRegistry sessionRegistry;

	@Override
	public void addUser(UserDTO userDTO) {
		// add to account
		User user = new User();
		user.setName(userDTO.getName());
		user.setPhone(userDTO.getPhone());
		user.setPassword(PasswordGenerator.getHashString(userDTO.getPassword()));
		user.setEnabled(true);
		user.setRole(new Role(userDTO.getRoleId()));

		userDao.add(user);

		userDTO.setId(user.getId());
	}

	@Override
	public void updateUser(UserDTO userDTO) {
		User user = userDao.getById(userDTO.getId());
		if (user != null) {
			user.setName(userDTO.getName());
			user.setPhone(userDTO.getPhone());
			user.setRole(new Role(userDTO.getRoleId()));
			
			Employee emp = user.getEmployee();
			if (emp != null) {
				emp.setName(user.getName());
			}
			userDao.update(user);
		}
	}

	@Override
	public void updateProfile(UserDTO userDTO) {
		User user = userDao.getById(userDTO.getId());
		if (user != null) {
			user.setName(userDTO.getName());
			Employee emp = user.getEmployee();
			if (emp != null) {
				emp.setName(user.getName());
			}
			userDao.update(user);
		}
	}

	@Override
	public void deleteUser(Long id) {
		User user = userDao.getById(id);
		if (user != null) {
			userDao.delete(user);
			Employee emp = user.getEmployee();
			if (emp != null) {
				employeeDao.delete(emp.getId());
			}
			expireUserSessions(user.getPhone());
		}
	}

	@Override
	public List<UserDTO> findUsers(SearchUserDTO searchUserDTO) {
		List<User> users = userDao.find(searchUserDTO);
		List<UserDTO> userDTOs = new ArrayList<UserDTO>();

		users.forEach(user -> {
			UserDTO userDTO = new UserDTO();
			userDTO.setId(user.getId());
			userDTO.setName(user.getName());
			userDTO.setPhone(user.getPhone());
			userDTO.setRoleId(user.getRole().getId());
			userDTO.setEnabled(user.getEnabled());
			userDTOs.add(userDTO);
		});

		return userDTOs;
	}

	@Override
	public long countUsers(SearchUserDTO searchUserDTO) {
		return userDao.count(searchUserDTO);
	}

	@Override
	public long countTotalUsers(SearchUserDTO searchUserDTO) {
		return userDao.countTotal(searchUserDTO);
	}

	@Override
	public UserDTO getUserById(Long id) {
		User user = userDao.getById(id);
		if (user != null) {
			UserDTO userDTO = new UserDTO();
			userDTO.setId(user.getId());
			userDTO.setName(user.getName());
			userDTO.setPhone(user.getPhone());
			userDTO.setRoleId(user.getRole().getId());
			userDTO.setEnabled(user.getEnabled());
			return userDTO;
		}
		return null;
	}

	@Override
	public void changeAccountLock(long id) {
		User userAccount = userDao.getById(id);
		if (userAccount != null) {
			userAccount.setEnabled(!userAccount.getEnabled());
			userDao.update(userAccount);

			expireUserSessions(userAccount.getPhone());
		}
	}

	private void expireUserSessions(String username) {
		for (Object principal : sessionRegistry.getAllPrincipals()) {
			if (principal instanceof UserPrincipal) {
				UserPrincipal userDetails = (UserPrincipal) principal;
				if (userDetails.getUsername().equals(username)) {
					for (SessionInformation information : sessionRegistry.getAllSessions(userDetails, true)) {
						information.expireNow();
					}
				}
			}
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.getByPhone(username);

		if (user == null) {
			throw new UsernameNotFoundException("not found");
		}

		List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));

		UserPrincipal accountDTO = new UserPrincipal(user.getPhone(), user.getPassword(), user.getEnabled(), true, true,
				true, authorities);
		accountDTO.setId(user.getId());
		accountDTO.setName(user.getName());
		accountDTO.setRoleId(user.getRole().getId());
		if (user.getEmployee() != null) {
			accountDTO.setPathImage("/image/" + user.getEmployee().getPathImage());
		} else {
			accountDTO.setPathImage("/user/images/icon/logofb.png");
		}
		return accountDTO;
	}

	@Override
	public void changePassword(UserDTO userDTO) {
		User user = userDao.getById(userDTO.getId());
		if (user != null && PasswordGenerator.checkHashStrings(userDTO.getPassword(), user.getPassword())) {
			user.setPassword(PasswordGenerator.getHashString(userDTO.getRepassword()));
			userDao.update(user);

			expireUserSessions(user.getPhone());
		} else {
			throw new DataIntegrityViolationException("wrong password");
		}
	}

	@Override
	public void resetPassword(UserDTO accountDTO) {
		User user = userDao.getByPhone(accountDTO.getPhone());
		if (user != null) {
			String password = PasswordGenerator.generateRandomPassword();
			user.setPassword(PasswordGenerator.getHashString(password));
			userDao.update(user);

			expireUserSessions(user.getPhone());
		}
	}

	@Override
	public void setupUserPassword(UserDTO accountDTO) {
		User user = userDao.getById(accountDTO.getId());
		if (user != null) {
			user.setPassword(PasswordGenerator.getHashString(accountDTO.getPassword()));
			userDao.update(user);

			expireUserSessions(user.getPhone());
		}
	}

}
