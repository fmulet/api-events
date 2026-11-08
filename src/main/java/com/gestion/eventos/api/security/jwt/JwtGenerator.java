package com.gestion.eventos.api.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * JwtGenerator
 */
@Component
public class JwtGenerator {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.expiration}")
  private Long jwtExpiration;

  private SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
  }

  public String generateToken(Authentication authentication) {
    String username = authentication.getName();
    Date currentDate = new Date();
    Date expireDate = new Date(currentDate.getTime() + jwtExpiration);
    return Jwts.builder()
      .subject(username)
      .issuedAt(new Date())
      .expiration(expireDate)
      .signWith(getSigningKey(), Jwts.SIG.HS512)
      .compact();
  }

  public String getUsernameFromJwt(String token) {
    return Jwts.parser()
      .verifyWith(getSigningKey())
      .build()
      .parseSignedClaims(token)
      .getPayload()
      .getSubject();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token);
      return true;
    } catch (MalformedJwtException e) {
      System.out.println("Invalid JWT Token: " + e.getMessage());
    } catch (ExpiredJwtException e) {
      System.out.println("JWT Token is expired: " + e.getMessage());
    } catch (UnsupportedJwtException e) {
      System.out.println("JWT Token is unsupported: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      System.out.println("JWT claims string is empty: " + e.getMessage());
    } catch (io.jsonwebtoken.security.SignatureException e) {
      System.out.println("Invalid JWT Signature: " + e.getMessage());
    }
    return false;
  }
}
