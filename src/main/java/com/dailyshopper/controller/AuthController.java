package com.dailyshopper.controller;


import com.dailyshopper.request.LoginRequest;
import com.dailyshopper.response.ApiResponse;
import com.dailyshopper.response.JwtResponse;
import com.dailyshopper.security.jwt.JwtUtils;
import com.dailyshopper.security.user.ShopUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/carts")
public class AuthController {

        private final AuthenticationManager authenticationManager;
        private final JwtUtils jwtUtils;


        public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request){
            try {
                Authentication authentication = authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(
                                request.getEmail(), request.getPassword()
                        ));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwt = jwtUtils.generateTokenForUser(authentication);
                ShopUserDetails userDetails = (ShopUserDetails) authentication.getPrincipal();
                JwtResponse jwtResponse = new JwtResponse(userDetails.getId(), jwt);
                return ResponseEntity.ok(new ApiResponse("Login Successful", jwtResponse));
            } catch (AuthenticationException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));

            }
        }

}
