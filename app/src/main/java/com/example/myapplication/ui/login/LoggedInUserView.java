package com.example.myapplication.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String displayName;
    //... other data fields that may be accessible to the UI
    private String cookie;

    LoggedInUserView(String id, String displayName) {
        this.cookie = id;
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }
    String getCookie(){return cookie;}
}
