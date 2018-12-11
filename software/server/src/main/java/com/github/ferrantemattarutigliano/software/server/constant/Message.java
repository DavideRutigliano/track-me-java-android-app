package com.github.ferrantemattarutigliano.software.server.constant;

public enum Message {

    REGISTRATION_SUCCESS("Registration has succeed."),
    BAD_PARAMETERS("Submitted parameters are not valid."),
    BAD_SSN_UPDATE("You cannot change your ssn: "),
    BAD_VAT_UPDATE("You cannot change your vat: "),
    BAD_LOGIN("You are not logged in."),
    BAD_REQUEST("You are not allowed to visit this page."),
    INVALID_EMAIL("Submitted email is not valid."),
    INDIVIDUAL_ALREADY_EXISTS("Individual already exists."),
    THIRD_PARTY_ALREADY_EXISTS("Third Party already exists."),
    USERNAME_ALREADY_EXISTS("Username already exists: "),
    USERNAME_IS_EMPTY("To login, you should provide an username."),
    USERNAME_DO_NOT_EXISTS("Username do not exists."),
    CHANGE_USERNAME_SUCCESS("Username changed, new username: "),
    CHANGE_PASSWORD_SUCCESS("Password changed."),
    CHANGE_PROFILE_SUCCESS("Profile updated."),

    REQUEST_SUCCESS("Request has succeed."),
    REQUEST_INVALID_SSN("Request has an invalid SSN."),
    REQUEST_NOT_ANONYMOUS("Request has too strict criteria: cannot guarantee anonymity."),
    REQUEST_ACCEPTED("Request accepted."),
    REQUEST_REJECTED("Request rejected.");

    private String msg;

    Message(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return msg;
    }
}
