package com.github.ferrantemattarutigliano.software.server.constant;

public enum Role {
    ROLE_INDIVIDUAL("INDIVIDUAL"),
    ROLE_THIRD_PARTY("THIRD_PARTY");

    private String role;

    Role(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return role;
    }
}
