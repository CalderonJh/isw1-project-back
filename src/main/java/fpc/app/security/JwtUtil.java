package fpc.app.security;

import static fpc.app.constant.Constant.TOKEN_EXPIRATION_TIME;

import fpc.app.model.auth.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  @Value("${jwt.secret}")
  private String secret;

  private SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(secret.getBytes());
  }

  public String generateToken(User user) {
    return Jwts.builder()
        .subject(user.getUsername())
        .claim("roles", user.getRoles())
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
        .signWith(getSigningKey())
        .compact();
  }

  // extracts the email from the token
  public String extractEmail(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
    final Claims claims = extractAllClaims(token);
    return claimResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
  }

  // checks if the token is valid
  public boolean isTokenValid(String token, String userEmail) {
    final String extractedEmail = extractEmail(token);
    return extractedEmail.equals(userEmail) && !isTokenExpired(token);
  }

  public boolean isTokenExpired(String token) {
    return extractClaim(token, Claims::getExpiration).before(new Date());
  }

  public List<String> extractRoles(String token) {
    Claims claims = extractAllClaims(token);
    List<?> rawRoles = (List<?>) claims.get("roles");
    return rawRoles.stream().map(role -> ((Map<?, ?>) role).get("name").toString()).toList();
  }
}
