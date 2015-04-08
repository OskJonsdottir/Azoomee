package com.azoomee.transport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Sibling {
    private String name;

    @SuppressWarnings("UnusedDeclaration")
    private Sibling() {}    //Used by Jackson

    public Sibling(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
