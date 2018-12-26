package com.sd.test.springsecurity.security.jwt.exception;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;

public class ExpiredJwtException extends io.jsonwebtoken.ExpiredJwtException {
    public ExpiredJwtException(Header header, Claims claims, String message) {
        super(header, claims, message);
    }

    public ExpiredJwtException(Header header, Claims claims, String message, Throwable cause) {
        super(header, claims, message, cause);
    }
}
