package com.mobdeve.cherie;

import androidx.lifecycle.ViewModel;

public class RegisterInfoViewModel extends ViewModel {
    private UserData userData;

    public UserData getUserData() {
        if (userData == null) {
            userData = new UserData();
        }
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }
}