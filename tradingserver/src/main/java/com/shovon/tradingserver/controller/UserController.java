package com.shovon.tradingserver.controller;


import com.shovon.tradingserver.dto.request.UserCreateInput;
import com.shovon.tradingserver.dto.request.UserInput;
import com.shovon.tradingserver.dto.request.UserLoginInput;
import com.shovon.tradingserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

  @Autowired
  private UserService userService;


  @PostMapping("/create-account")
  public ResponseEntity<?> createAccount(@RequestBody UserCreateInput userCreateInput) {
    try {
      return new ResponseEntity<>(this.userService.create(userCreateInput), HttpStatus.CREATED);
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }
  


}
