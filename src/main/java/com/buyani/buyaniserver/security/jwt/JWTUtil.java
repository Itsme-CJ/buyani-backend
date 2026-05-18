package com.buyani.buyaniserver.security.jwt;import java.util.Calendar;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.buyani.buyaniserver.dto.UserCredentialDTO;
import com.buyani.buyaniserver.entity.User;

@Slf4j
public class JWTUtil {
  private JWTUtil() {
  }
  
  public static UserCredentialDTO parseToken(String token) {
    try {
      Claims body = Jwts.parser().setSigningKey(SecurityConstants.SECRET.getBytes()).parseClaimsJws(token).getBody();
      log.info("JWTUtil id : {}", body.get("id"));
      UserCredentialDTO userCredentialDTO = new UserCredentialDTO();
      userCredentialDTO.setEmail(body.getSubject());
      userCredentialDTO.setUserId((String) body.get("id"));

      return userCredentialDTO;

    } catch (JwtException | ClassCastException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static DecodedJWT verifyToken(String token) {
    Algorithm   algorithm   = Algorithm.HMAC512(SecurityConstants.SECRET.getBytes());    
    JWTVerifier jwtVerifier = JWT.require(algorithm).build();

    return jwtVerifier.verify(token);
  }

  public static String createToken(User user, int expiration) {
    Calendar  calendar              = Calendar.getInstance();
    Date      now                   = calendar.getTime();
    Algorithm algorithm             = Algorithm.HMAC512(SecurityConstants.SECRET.getBytes());

    calendar.add(Calendar.MINUTE, expiration);
    Date expirationDate = calendar.getTime();

    return JWT.create().withClaim("id", user.getUserId()).withSubject(user.getEmail())
        .withIssuedAt(now).withNotBefore(now).withExpiresAt(expirationDate)
        .sign(algorithm);
  }
}
