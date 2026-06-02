package com.shovon.tradingserver.security;

import com.shovon.tradingserver.model.User;
import com.shovon.tradingserver.types.UserRole;
import java.util.Collection;
import java.util.Collections;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

  private final Long id;

  private final String fullName;

  private final String email;

  private final String password;

  private final UserRole userRole;


  public CustomUserDetails(User user) {
    this.id= user.getId();
    this.fullName = user.getFullName();
    this.email = user.getEmail();
    this.password = user.getPassword();
    this.userRole = user.getUserRole();
  }

  public Long getId() {
    return this.id;
  }

  public String getEmail() {
    return this.email;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(new SimpleGrantedAuthority(this.userRole.name()));
  }

  @Override
  public String getUsername() {
    return this.fullName;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }


  @Override
  public @Nullable String getPassword() {
    return this.password;
  }


  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }


}
