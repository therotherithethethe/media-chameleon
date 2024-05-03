package com.therotherithethethe.model.entity.validation.account.username;

public class LengthValidationHandler extends UsernameValidationHandler{

    @Override
    public boolean check(String username) {
        return username.length() >= 4 && username.length() <= 15;
    }
}
