package com.bayani.bayaniserver.security.jwt;

public class SecurityConstants {

  public static final String ACCESS_TOKEN                  = "accessToken";
  public static final int ACCESS_TOKEN_EXPIRATION = 43200; // 30 days
  public static final String APPLICATION_JSON_CONTENT_TYPE = "application/json";
  public static final String HEADER_STRING                 = "Authorization";
  public static final String PUBLIC_URL                    = "/api/public/**";
  public static final String REFRESH_TOKEN                 = "refreshToken";
  public static final int REFRESH_TOKEN_EXPIRATION         = 1440;  // 1 day
  public static final String SECRET                        = "SECRET_KEY";
  public static final String ERROR_URL                     = "/error";
  public static final String SIGN_IN_URL                   = "/api/public/login";
  public static final String SERVICE_URL                   = "/api/**";
  public static final String TOKEN_PREFIX                  = "Bearer ";

  public static final String USER_NOT_FOUND = "Invalid Username - The specified username does not exist in our records.";

  private SecurityConstants() {
    throw new IllegalStateException("Utility class");
  }
}