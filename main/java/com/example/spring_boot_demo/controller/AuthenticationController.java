package com.example.spring_boot_demo.controller;

import com.example.spring_boot_demo.model.AuthenticationResponse;
import com.example.spring_boot_demo.model.AuthenticatonRequest;
import com.example.spring_boot_demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> createUser(@RequestBody AuthenticatonRequest authenticatonRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticatonRequest.getUsername(),
                            authenticatonRequest.getPassword()
                    )
            );

            final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticatonRequest.getUsername());

//            // Check if the user has the required role (if specified in the request)
//            if (authenticatonRequest.getRole() != null && !authenticatonRequest.getRole().isEmpty()) {
//                boolean hasRequiredRole = userDetails.getAuthorities().stream()
//                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_" + authenticatonRequest.getRole()));
//
//                if (!hasRequiredRole) {
//                    return ResponseEntity.status(403).body(
//                            new AuthenticationResponse(null, "User doesn't have the required role: " + authenticatonRequest.getRole()));
//                }
//            }

            String jwt= jwtUtil.generateToken(userDetails.getUsername());
            return ResponseEntity.ok(new AuthenticationResponse(jwt,null));


        } catch (Exception e) {
            return ResponseEntity.status(401).body(new AuthenticationResponse(null,"Invalid username or password"+e.getMessage()));
        }
    }
}
