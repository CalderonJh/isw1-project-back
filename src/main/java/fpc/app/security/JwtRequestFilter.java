package fpc.app.security;

import fpc.app.service.auth.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
  private final JwtUtil jwtUtil;
  private final UserService userService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
      throws ServletException, IOException {

    final String authHeader = request.getHeader("Authorization");
    Long userId = null;
    String jwt = null;

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      jwt = authHeader.substring(7);
      try {
        userId = jwtUtil.getUserId(jwt);
      } catch (Exception e) {
        logger.warn("Invalid JWT token");
      }
    }

    if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = userService.loadUser(userId);
      if (jwtUtil.isTokenValid(jwt, Long.parseLong(userDetails.getUsername()))) {
        List<String> roles = jwtUtil.extractRoles(jwt);
        List<SimpleGrantedAuthority> authorities =
            roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).toList();
        UsernamePasswordAuthenticationToken token =
            new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(token);
      }
    }

    chain.doFilter(request, response);
  }
}
