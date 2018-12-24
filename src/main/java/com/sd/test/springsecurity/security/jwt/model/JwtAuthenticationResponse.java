package com.sd.test.springsecurity.security.jwt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class JwtAuthenticationResponse extends ResponseEntity<MessageToken> {

    public JwtAuthenticationResponse(HttpStatus status, String message, String token) {
        super(new MessageToken(message, token), status);
    }
}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class MessageToken{
    private String message;
    private String token;
}
