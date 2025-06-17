package com.by.dallinday.member;

import lombok.Getter;

public enum Role {

    USER("USER"),

    ADMIN("ADMIN");

    @Getter
    private String role;

    Role(String role) {
        this.role = role;
    }
}
