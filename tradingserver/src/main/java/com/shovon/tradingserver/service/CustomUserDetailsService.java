package com.shovon.tradingserver.service;


import com.shovon.tradingserver.model.User;
import com.shovon.tradingserver.repository.UserRepository;
import com.shovon.tradingserver.security.CustomUserDetails;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Optional<User> optUser = this.userRepository.findByEmail(email);

    if (optUser.isEmpty()) {
      throw new UsernameNotFoundException("User not found with email: " + email);
    }

    return new CustomUserDetails(optUser.get());
  }

}
