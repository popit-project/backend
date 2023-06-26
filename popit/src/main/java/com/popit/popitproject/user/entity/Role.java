package com.popit.popitproject.user.entity;

public enum Role {
    SELLER("ROLE_SELLER"),
    USER("ROLE_USER");

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }
}