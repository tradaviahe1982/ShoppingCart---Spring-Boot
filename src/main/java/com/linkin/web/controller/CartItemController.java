package com.linkin.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkin.model.CartItemDTO;
import com.linkin.model.ProductDTO;
import com.linkin.model.ResponseDTO;
import com.linkin.model.SearchCustomerDTO;
import com.linkin.model.SearchShipperDTO;
import com.linkin.service.CustomerService;
import com.linkin.service.ProductService;
import com.linkin.service.ShipperService;

@Controller
@RequestMapping(value = "/staff")
public class CartItemController {
	@Autowired
	ProductService producService;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	ShipperService shipperService;

	@GetMapping(value = "/cart-item/list")
	public String listCartItem(Model model) {
		SearchCustomerDTO searchCustomerDTO = new SearchCustomerDTO();
		searchCustomerDTO.setStart(null);
		model.addAttribute("customers", customerService.find(searchCustomerDTO));
		
		SearchShipperDTO searchShipperDTO = new SearchShipperDTO();
		searchShipperDTO.setStart(null);
		model.addAttribute("shippers", shipperService.find(searchShipperDTO));
		return "admin/cart-item/list-cart-item";
	}

	@GetMapping(value = "/cart-item/delete/{productId}")
	public @ResponseBody String delCartItem(@PathVariable(name = "productId") Long id, HttpSession session) {
		Map<Long, CartItemDTO> map = (Map<Long, CartItemDTO>) session.getAttribute("cart");
		if (map != null) {
			map.remove(id);
			session.setAttribute("cart", map);
		}
		return "ok";
	}

	@GetMapping(value = "/cart-item/delete-multi/{ids}")
	public ResponseEntity<String> delCartItem(@PathVariable(name = "ids") List<Long> ids, HttpSession session) {
		Map<Long, CartItemDTO> map = (Map<Long, CartItemDTO>) session.getAttribute("cart");
		for (Long id : ids) {
			try {
				map.remove(id);
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
		return new ResponseEntity<>("ok", HttpStatus.OK);
	}

	@PutMapping("/cart-item/add-to-order")
	public @ResponseBody ProductDTO addCartItem(HttpSession session, @RequestParam(name = "pId") Long pId,
			@RequestParam(name = "quantity") int quantity) {
		ProductDTO productDTO = producService.getById(pId);
		if (productDTO != null) {
			Object obj = session.getAttribute("cart");

			if (obj == null) { // lan dau add vao gio hang
				CartItemDTO cartItemDTO = new CartItemDTO();
				cartItemDTO.setUnitPrice(productDTO.getUnitPrice());
				cartItemDTO.setProduct(productDTO);
				cartItemDTO.setQuantity(quantity);

				Map<Long, CartItemDTO> map = new HashMap<>();
				map.put(productDTO.getId(), cartItemDTO);
				// luu vao session
				session.setAttribute("cart", map);
			} else {
				Map<Long, CartItemDTO> map = (Map<Long, CartItemDTO>) obj;
				// kiểm tra sản phảm đã có trong giỏ hàng chưa
				CartItemDTO item = map.get(productDTO.getId());

				if (item == null) {
					CartItemDTO cartItemDTO = new CartItemDTO();
					cartItemDTO.setUnitPrice(productDTO.getUnitPrice());
					cartItemDTO.setProduct(productDTO);
					cartItemDTO.setQuantity(quantity);

					map.put(productDTO.getId(), cartItemDTO);
				} else {
					item.setQuantity(item.getQuantity() + quantity);
				}
				session.setAttribute("cart", map);
			}
		}
		return productDTO;
	}

	@PostMapping("/cart-item/get-cart-item-session")
	public @ResponseBody ResponseDTO<CartItemDTO> getCartItemsSession(HttpSession session) {
		ResponseDTO<CartItemDTO> responseDTO = new ResponseDTO<>();

		Map<Long, CartItemDTO> map = (Map<Long, CartItemDTO>) session.getAttribute("cart");
		if (map != null) {
			List<CartItemDTO> list = new ArrayList<>(map.values());
			responseDTO.setData(list);
			responseDTO.setRecordsTotal(list.size());
			responseDTO.setRecordsFiltered(list.size());
		} else {
			responseDTO.setData(new ArrayList<>());
			responseDTO.setRecordsTotal(0);
			responseDTO.setRecordsFiltered(0);
		}

		return responseDTO;
	}

	@PutMapping("/cart-item/update")
	public @ResponseBody String updateCartItem(HttpSession session, @RequestParam(name = "pId") Long pId,
			@RequestParam(name = "quantity") int quantity) {
		Map<Long, CartItemDTO> map = (Map<Long, CartItemDTO>) session.getAttribute("cart");
		if (map != null) {
			CartItemDTO item = map.get(pId);
			if (item != null) {
				item.setQuantity(quantity);
			}
			session.setAttribute("cart", map);
		}
		return "ok";
	}

}
