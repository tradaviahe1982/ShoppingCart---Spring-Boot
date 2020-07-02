package com.linkin.web.controller;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkin.model.ResponseDTO;
import com.linkin.model.SearchUserDTO;
import com.linkin.model.UserDTO;
import com.linkin.service.UserService;

@Controller
@RequestMapping(value = "/admin")
public class UserAdminController extends BaseWebController {

	@Autowired
	private UserService userService;

	@GetMapping("/accounts")
	public String listUser(Model model) {
		return "admin/userAccount/listUser";
	}

	@PostMapping(value = "/accounts")
	public ResponseEntity<ResponseDTO<UserDTO>> finds(@RequestBody SearchUserDTO searchUserDTO) {
		ResponseDTO<UserDTO> responseDTO = new ResponseDTO<UserDTO>();
		responseDTO.setData(userService.findUsers(searchUserDTO));
		responseDTO.setRecordsTotal(userService.countTotalUsers(searchUserDTO));
		responseDTO.setRecordsFiltered(userService.countUsers(searchUserDTO));
		return new ResponseEntity<ResponseDTO<UserDTO>>(responseDTO, HttpStatus.OK);
	}

	@PostMapping("/accounts/add")
	public @ResponseBody UserDTO addUser(@RequestBody UserDTO userDTO) {
		userService.addUser(userDTO);
		return userDTO;
	}

	@PutMapping("/accounts/update")
	public @ResponseBody UserDTO updateCTVConfig(@RequestBody UserDTO userDTO) {
		userService.updateUser(userDTO);
		return userDTO;
	}

	@GetMapping("/account/change-lock/{id}")
	public ResponseEntity<String> changeLockedUserStatus(@PathVariable(name = "id") Long id) {
		userService.changeAccountLock(id);
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}

	@GetMapping("/account/delete/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable(name = "id") Long id) {
		userService.deleteUser(id);
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}

	@GetMapping("/account/delete-multi/{ids}")
	public ResponseEntity<String> deleteMultiUser(@PathVariable(name = "ids") List<Long> ids) {
		for (long id : ids) {
			try {
				userService.deleteUser(id);
			} catch (Exception e) {
			}
		}
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}

	@GetMapping("/account/reset-password/{id}")
	public String resetPassword(Model model, @PathVariable(name = "id") Long id) {
		UserDTO userDTO = userService.getUserById(id);
		model.addAttribute("userAccountDTO", userDTO);
		return "admin/userAccount/resetPassword";
	}

	@PostMapping("/account/reset-password")
	public String resetPassword(@ModelAttribute(name = "userAccountDTO") UserDTO userDTO, BindingResult bindingResult) {
		validateUserPassword(userDTO, bindingResult);
		if (bindingResult.hasErrors()) {
			return "admin/userAccount/resetPassword";
		}
		userService.setupUserPassword(userDTO);

		return "redirect:/admin/accounts";
	}

	private void validateUserPassword(Object object, Errors errors) {
		UserDTO accountDTO = (UserDTO) object;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "error.msg.empty.account.password");
		if (accountDTO.getPassword().length() < 6 && StringUtils.isNotBlank(accountDTO.getPassword())) {
			errors.rejectValue("password", "error.msg.empty.account.password");
		}
	}

}
