package com.shovon.tradingserver.controller;


import com.shovon.tradingserver.dto.response.LoginResponse;
import com.shovon.tradingserver.dto.request.UserInput;
import com.shovon.tradingserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

  @Autowired
  private UserService userService;


  @PostMapping("/create-account")
  public ResponseEntity<?> createAccount(@RequestBody UserInput userInput) {
    try {
      return ResponseEntity.ok(this.userService.create(userInput));
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody UserInput userInput) {
    try {
      return ResponseEntity.ok(this.userService.login(userInput));
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }
}
