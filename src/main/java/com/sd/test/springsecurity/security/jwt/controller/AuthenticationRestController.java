package com.sd.test.springsecurity.security.jwt.controller;

import com.sd.test.springsecurity.entity.User;
import com.sd.test.springsecurity.security.jwt.model.JwtAuthenticationRequest;
import com.sd.test.springsecurity.security.jwt.model.JwtAuthenticationResponse;
import com.sd.test.springsecurity.security.jwt.exception.AuthenticationException;
import com.sd.test.springsecurity.security.jwt.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
@RequestMapping(value = "${app.api.rest.root}/jwt")
public class AuthenticationRestController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;

    @RequestMapping(value = "/${jwt.route.authentication.path}", method = RequestMethod.POST)
    public JwtAuthenticationResponse createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        // Reload password post-security so we can generate the token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        // Return the token
        return new JwtAuthenticationResponse(HttpStatus.OK, "Created", token);
    }

    @RequestMapping(value = "/${jwt.route.authentication.refresh}", method = RequestMethod.GET)
    public JwtAuthenticationResponse refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String authToken = request.getHeader(tokenHeader);
        final String token = authToken.substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = (User) userDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return new JwtAuthenticationResponse(HttpStatus.OK, "Success", refreshedToken);
        } else {
            return new JwtAuthenticationResponse(HttpStatus.BAD_REQUEST, "Error", null);
        }
    }

    @ExceptionHandler({AuthenticationException.class})
    public JwtAuthenticationResponse handleAuthenticationException(AuthenticationException e) {
        return new JwtAuthenticationResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
    }

    /**
     * Authenticates the user. If something is wrong, an {@link AuthenticationException} will be thrown
     */
    private void authenticate(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new AuthenticationException("User is disabled!", e);
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Bad credentials!", e);
        }
    }
}
