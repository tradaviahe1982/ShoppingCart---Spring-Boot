package com.linkin.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkin.model.CartItemDTO;
import com.linkin.model.OrderDTO;
import com.linkin.model.ResponseDTO;
import com.linkin.model.SearchOrderDTO;
import com.linkin.service.CartItemService;
import com.linkin.service.OrderService;

@Controller
@RequestMapping(value = "/staff")
public class OrderController {

	@Autowired
	OrderService orderService;

	@Autowired
	CartItemService cartIitemService;

	@PostMapping("/order/add")
	public @ResponseBody OrderDTO addOrder(@RequestBody OrderDTO orderDTO, HttpSession session) {
		HashMap<Long, CartItemDTO> map = (HashMap<Long, CartItemDTO>) session.getAttribute("cart");

		if (map != null) {
			orderDTO.setCartItemDTOs(new ArrayList<>(map.values()));
		}
		
		orderDTO.setStatusOrder(1);

		orderService.add(orderDTO);
		if (orderDTO.getDiscount() >=0 && orderDTO.getDiscount() <=99) {
			session.removeAttribute("cart");
		}

		return orderDTO;
	}

	@GetMapping(value = "/order/list")
	public String list(Model model) {
		return "admin/order/list-order";
	}

	@PostMapping(value = "/order/list")
	public ResponseEntity<ResponseDTO<OrderDTO>> list(@RequestBody SearchOrderDTO searchOrderDTO) {
		ResponseDTO<OrderDTO> responseDTO = new ResponseDTO<>();
		responseDTO.setData(orderService.find(searchOrderDTO));
		responseDTO.setRecordsFiltered(orderService.count(searchOrderDTO));
		responseDTO.setRecordsTotal(orderService.countTotal(searchOrderDTO));

		return new ResponseEntity<ResponseDTO<OrderDTO>>(responseDTO, HttpStatus.OK);
	}

	@GetMapping(value = "/order/delete/{id}")
	public ResponseEntity<String> delOrder(@PathVariable(name = "id") Long id) {
		orderService.delete(id);
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}
	
	@GetMapping(value = "/order/delete-multi/{ids}")
	public ResponseEntity<String> delOrder(@PathVariable(name = "ids") List<Long> ids) {
		for (Long id : ids) {
			try {
				orderService.delete(id);
			} catch (Exception e) {

			}
		}
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}

	@PutMapping("/order/update")
	public @ResponseBody OrderDTO updateStatus(@RequestBody OrderDTO newsDTO) {
		orderService.update(newsDTO);
		return newsDTO;
	}
	
	@GetMapping(value = "/order/delete-cartitem/{id}")
	public ResponseEntity<String> delCartItem(@PathVariable(name = "id") Long id) {
		cartIitemService.delete(id);
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}
	
	@PutMapping("/order/update-cartitem")
	public @ResponseBody CartItemDTO updateCartItem(@RequestBody CartItemDTO cartItemDTO) {
		cartIitemService.edit(cartItemDTO);
		return cartItemDTO;
	}

}
