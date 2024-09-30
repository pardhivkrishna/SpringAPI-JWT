package com.example.springJwt.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("webSecurity")
public class WebSecurity {

    // Method to check if the authenticated user can access the cart
    public boolean checkUserAccess(Authentication authentication, HttpServletRequest request) {
        String username = authentication.getName(); // Get the username of the currently authenticated user
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN")); // Check if the user is an admin
        
        // Check if the user is accessing their own cart based on URL structure
        boolean isAccessingOwnCart = request.getRequestURI().contains("/cart/" + username);

        // Allow access if the user is an admin or accessing their own cart
        return isAdmin || isAccessingOwnCart;
    }
}
