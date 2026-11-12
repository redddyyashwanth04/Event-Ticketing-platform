package com.example.Ticketing_project.security;


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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter { // 1. Runs exactly once per HTTP request

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // 2. Extract JWT token from the HTTP "Authorization" header
            String jwt = parseJwt(request);

            // 3. Validate the token using our JwtUtils
            if (jwt != null && jwtUtils.validateToken(jwt)) {

                // 4. Extract email (subject) stored inside the token
                String email = jwtUtils.getEmailFromToken(jwt);

                // 5. Load user details (authorities/roles) from the database
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                // 6. Create an Authentication object for Spring Security
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null, // Credentials (not needed since JWT is already verified)
                                userDetails.getAuthorities() // Roles (e.g., ROLE_ATTENDEE)
                        );

                // Attach request details to the authentication object
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 7. Store the authentication inside Spring Security's Context.
                // This tells the entire application: "The user is logged in successfully!"
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        // 8. Pass the request forward to the next filter in the chain
        filterChain.doFilter(request, response);
    }

    // Helper method to parse the "Bearer <token>" header format
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        // HTTP Authorization headers look like: "Bearer eyJhbGciOiJIUzI1NiIs..."
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7); // Remove the first 7 characters ("Bearer ") to isolate the token string
        }

        return null;
    }
}