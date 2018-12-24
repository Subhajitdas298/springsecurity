package com.sd.test.springsecurity.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class FormattedResponse extends ResponseEntity<MessageToken> {

    public FormattedResponse(HttpStatus status, String message, String token) {
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
