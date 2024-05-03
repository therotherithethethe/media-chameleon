package com.therotherithethethe.model.entity.validation.account.username;

public class CharacterValidationHandler extends UsernameValidationHandler {
    @Override
    public boolean check(String username) {
        return username.matches("[a-zA-Z0-9._]+");
    }
}
