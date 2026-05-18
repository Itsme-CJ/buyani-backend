package com.buyani.buyaniserver.security.user;

import java.util.ArrayList;
import java.util.Optional;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.buyani.buyaniserver.dto.UserCredentialDTO;
import com.buyani.buyaniserver.entity.User;
import com.buyani.buyaniserver.mapper.HibernateFieldMapper;
import com.buyani.buyaniserver.repository.UserRepo;
import com.buyani.buyaniserver.security.jwt.SecurityConstants;

@Service
public class UserDetailService implements UserDetailsService {
  @Autowired
  UserRepo userRepo;

  private DozerBeanMapper dozerBeanMapper;

  @Autowired
  public UserDetailService() {
    this.dozerBeanMapper = new DozerBeanMapper();
    this.dozerBeanMapper.setCustomFieldMapper(new HibernateFieldMapper());
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    try {
      Optional<User> optionalUser = userRepo.findByEmail(username);

      if (optionalUser.isEmpty()) {
        throw new UsernameNotFoundException(SecurityConstants.USER_NOT_FOUND);
      }
  
      User user = optionalUser.get();
      UserCredentialDTO cred = new UserCredentialDTO();
      cred.setEmail(user.getEmail());
      cred.setPassword(user.getPassword());
  
      return new UserPrincipal(dozerBeanMapper.map(cred, UserCredentialDTO.class), new ArrayList<>());
    } catch (Exception e){
      throw new UsernameNotFoundException(e.getMessage());
    }
  }
}
