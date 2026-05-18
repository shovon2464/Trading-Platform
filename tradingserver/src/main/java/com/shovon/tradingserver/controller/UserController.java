package com.shovon.tradingserver.controller;


import com.shovon.tradingserver.dto.request.UserCreateInput;
import com.shovon.tradingserver.dto.request.UserInput;
import com.shovon.tradingserver.dto.request.UserLoginInput;
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


  @PostMapping("/check-email")
  public ResponseEntity<?> checkEmail(@RequestBody String email) {
    try {
      return ResponseEntity.ok(this.userService.checkEmail(email));
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }


  @PostMapping("/create-account")
  public ResponseEntity<?> createAccount(@RequestBody UserCreateInput userCreateInput) {
    try {
      return ResponseEntity.ok(this.userService.create(userCreateInput));
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody UserLoginInput userInput) {
    try {
      return ResponseEntity.ok(this.userService.login(userInput));
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<?> refreshToken(@RequestBody String refreshToken) {
    try {
      return ResponseEntity.ok(this.userService.refreshLogin(refreshToken));
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }
}
