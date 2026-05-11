package com.shovon.tradingserver.security;



import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration}")
  private Long expiration;

  @Value("${jwt.refresh-expiration}")
  private Long refreshExpiration;

  @Value("${jwt.server-access-expiration}")
  private Long serverAccessExpiration;

  @Value("${jwt.reset-password-expiration}")
  private Long resetPasswordExpiration;


  public String generateAccessToken(String email) {
    return JWT.create()
        .withSubject(email)
        .withExpiresAt(new Date(System.currentTimeMillis() + this.expiration))
        .sign(Algorithm.HMAC512(this.secret));
  }

  public String generateRefreshToken(String email) {
    return JWT.create()
        .withSubject(email)
        .withExpiresAt(new Date(System.currentTimeMillis() + this.refreshExpiration))
        .sign(Algorithm.HMAC512(this.secret));
  }


  public String generateResetPasswordToken(String email) {
    return JWT.create()
        .withSubject(email)
        .withExpiresAt(new Date(System.currentTimeMillis() + this.refreshExpiration))
        .sign(Algorithm.HMAC512(this.secret));
  }

  public boolean validateToken(String token) {
    try {
      JWTVerifier verifier = JWT.require(Algorithm.HMAC512(this.secret)).build();
      DecodedJWT jwt = verifier.verify(token);
      return true;
    } catch (JWTVerificationException exception) {
      return false;
    }
  }

  public String getEmailFromToken(String token) {
    DecodedJWT jwt = JWT.decode(token);
    return jwt.getSubject();
  }

  public boolean isExpired(String token) {
    try {
      DecodedJWT jwt = JWT.decode(token);
      Date expiration = jwt.getExpiresAt();
      return expiration != null && expiration.before(new Date());
    } catch (Exception e) {
      return true;
    }
  }

}
