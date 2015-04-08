package com.azoomee.transport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Token {
    private String token;

    @SuppressWarnings("UnusedDeclaration")
    private Token() {} //Used by Jackson

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
