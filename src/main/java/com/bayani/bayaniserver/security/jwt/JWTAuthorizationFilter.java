package com.bayani.bayaniserver.security.jwt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    public JWTAuthorizationFilter(AuthenticationManager authManager) {
    super(authManager);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    String header = request.getHeader(SecurityConstants.HEADER_STRING);
    if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
      chain.doFilter(request, response);
    } else {
      UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

      if (authentication == null) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        return;
      }
      
      SecurityContextHolder.getContext().setAuthentication(authentication);
      chain.doFilter(request, response);
    }
  }

  private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
    String token = "";
    try {
      token = request.getHeader(SecurityConstants.HEADER_STRING);
      
      // parse the token.
      token = token.replace(SecurityConstants.TOKEN_PREFIX, "");

      DecodedJWT decodedJWT = JWTUtil.verifyToken(token);

      //Check if token is expired
      if (decodedJWT.getExpiresAt().before(new Date())) {
        throw new TokenExpiredException("The Token has expired!");
      }

      String user = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes())).build()
        .verify(token).getSubject();
        
      return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
    } catch(Exception exception) {
      log.warn("Warning: {}", exception.getMessage());
      return null;
    }
  }
}
