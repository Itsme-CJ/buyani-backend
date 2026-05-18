package com.buyani.buyaniserver.security.user;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.buyani.buyaniserver.dto.UserCredentialDTO;

public class UserPrincipal implements UserDetails{
  private static final long serialVersionUID = 1905122041950251207L;

  private UserCredentialDTO user;
  private List<GrantedAuthority> auths;

  public UserPrincipal(UserCredentialDTO user, List<GrantedAuthority> grantedAuths) {
    this.user = user;
    this.auths = grantedAuths;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return auths;
  }

  @Override
  public String getPassword() {
    return this.user.getPassword();
  }

  @Override
  public String getUsername() {
    return this.user.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
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
