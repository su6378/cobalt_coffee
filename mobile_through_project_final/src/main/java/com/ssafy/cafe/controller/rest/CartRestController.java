package com.ssafy.cafe.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.cafe.model.dto.Cart;
import com.ssafy.cafe.model.dto.CartDetail;
import com.ssafy.cafe.model.service.CartService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/rest/cart")
@CrossOrigin("*")
public class CartRestController {
    @Autowired
    CartService cService;
    
    @GetMapping("/getList")
    @ApiOperation(value="장바구니에 있는 상품을 조회한다. {List<CartDetail>}을 반환한다", response = List.class)
    public ResponseEntity<List<CartDetail>> getList(String userId){
        return new ResponseEntity<List<CartDetail>>(cService.getList(userId), HttpStatus.OK);
    }
    
    @GetMapping("/check")
    @ApiOperation(value="장바구니에 있는 상품을 조회한다. true 혹은 false를 반환한다", response = Boolean.class)
    public Boolean check(String userId, Integer productId){
        return cService.check(userId, productId);
    }
    
    @PostMapping("/add")
    @Transactional
    @ApiOperation(value="장바구니에 상품을 담는다. 성공하면 true를 리턴한다. ", response = Boolean.class)
    public Boolean addCart(@RequestBody Cart cart) {
        cService.addCart(cart);
        return true;
    }
    
    @PutMapping("/update")
    @Transactional
    @ApiOperation(value="상품을 담은 개수를 변경한다. 성공하면 true를 리턴한다. ", response = Boolean.class)
    public Boolean updateCart(@RequestBody Cart cart) {
        cService.updateCart(cart);
        return true;
    }
    
    @DeleteMapping("/delete/{cartId}")
    @Transactional
    @ApiOperation(value="장바구니에 담긴 상품 하나를 지운다. 성공하면 true를 리턴한다. ", response = Boolean.class)
    public Boolean deleteCart(@PathVariable Integer cartId) {
        cService.deleteCart(cartId);
        return true;
    }
    
    @DeleteMapping("/deleteAll/{userId}")
    @Transactional
    @ApiOperation(value="사용자에 해당하는 장바구니에 담긴 상품 전체를 지운다. 성공하면 true를 리턴한다. ", response = Boolean.class)
    public Boolean deleteCart(@PathVariable String userId) {
        cService.deleteAllCart(userId);
        return true;
    }
}
