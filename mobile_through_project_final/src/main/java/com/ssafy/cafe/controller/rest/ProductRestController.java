package com.ssafy.cafe.controller.rest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.cafe.model.dto.Product;
import com.ssafy.cafe.model.service.ProductService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/rest/product")
@CrossOrigin("*")
public class ProductRestController {
    @Autowired
    ProductService pService;
    
    @GetMapping("/")
    @ApiOperation(value="전체 상품의 목록을 반환한다.", response = List.class)
    public ResponseEntity<List<Product>> getProductList(){
        return new ResponseEntity<List<Product>>(pService.getProductList(), HttpStatus.OK);
    }
    
    @GetMapping("/new")
    @ApiOperation(value="신규 상품의 목록을 반환한다.", response = List.class)
    public ResponseEntity<List<Product>> getNewProductList(){
        return new ResponseEntity<List<Product>>(pService.getNewProductList(), HttpStatus.OK);
    }
    
    @GetMapping("/best")
    @ApiOperation(value="인기 상품의 목록을 반환한다.", response = List.class)
    public ResponseEntity<List<Product>> getBestProductList(){
        return new ResponseEntity<List<Product>>(pService.getBestProductList(), HttpStatus.OK);
    }
    
    @GetMapping("/coffee")
    @ApiOperation(value="커피 상품의 목록을 반환한다.", response = List.class)
    public ResponseEntity<List<Product>> getCoffeeProductList(){
        return new ResponseEntity<List<Product>>(pService.getCoffeeProductList(), HttpStatus.OK);
    }
    
    @GetMapping("/tea")
    @ApiOperation(value="차 상품의 목록을 반환한다.", response = List.class)
    public ResponseEntity<List<Product>> getTeaProductList(){
        return new ResponseEntity<List<Product>>(pService.getTeaProductList(), HttpStatus.OK);
    }
    
    @GetMapping("/cookie")
    @ApiOperation(value="쿠키 상품의 목록을 반환한다.", response = List.class)
    public ResponseEntity<List<Product>> getCookieProductList(){
        return new ResponseEntity<List<Product>>(pService.getCookieProductList(), HttpStatus.OK);
    }
    
    @GetMapping("/product/{productId}")
    @ApiOperation(value="{productId}에 해당하는 상품의 정보를 반환한다.", response = List.class)
    public ResponseEntity<Product> getProduct(@PathVariable Integer productId){
        return new ResponseEntity<Product>(pService.selectProduct(productId), HttpStatus.OK);
    }
    
    @GetMapping("/comment/{productId}")
    @ApiOperation(value="{productId}에 해당하는 상품의 정보를 comment와 함께 반환한다."
            + "이 기능은 상품의 comment를 조회할 때 사용된다.", response = List.class)
    public List<Map<String, Object>> getProductWithComments(@PathVariable Integer productId){
        return pService.selectWithComment(productId);
    }
}
