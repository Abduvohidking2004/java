package uz.backecommers.identety.conf;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.backecommers.identety.service.JwtService;
import uz.backecommers.identety.service.impl.UserUserDetailsServiceImpl;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserUserDetailsServiceImpl userDetailsService;

    private final List<String> permittedUrls = List.of(
            "/api/v1/auth/check",
            "/api/v1/auth/registration",
            "/api/v1/auth/registration/confirm",
            "/api/v1/auth/login",
            "/api/v1/auth/refresh_token",
            "/api/v1/auth/forgot_password",
            "/api/v1/auth/forgot_password/confirm",
            "/api/v1/auth/users",
            "/ecom/swagger-ui.html",
            "/ecom/v3/api-docs/**",
            "/ecom/swagger-ui/**",
            "/ecom"
    );

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = request.getRequestURI();

        // Skip filter for permitted URLs
        if (permittedUrls.stream().anyMatch(url -> pathMatcher.match(url, requestURI))) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                username = jwtService.extractUsername(token);
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return;
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header missing or invalid");
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                request.setAttribute("currentUser", userDetails);
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token validation failed");
                return;
            }
        } else if (username == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Username extraction failed");
            return;
        }

        filterChain.doFilter(request, response);
    }


}
