package com.example.familymap.model;


public class AuthToken {
    private String authToken;
    private String userName;
    private String personID;

    public AuthToken(String authToken, String userName, String personID) {
        this.authToken = authToken;
        this.userName = userName;
        this.personID = personID;
    }

    public String getPersonID() {
        return personID;
    }

    public String getUserName() {
        return userName;
    }

    public String getAuthTokenID() {
        return authToken;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        AuthToken a = (AuthToken) o;
        return authToken.equals(a.getAuthTokenID()) &&
                userName.equals(a.getUserName()) &&
                personID.equals(a.getPersonID());
    }
}
