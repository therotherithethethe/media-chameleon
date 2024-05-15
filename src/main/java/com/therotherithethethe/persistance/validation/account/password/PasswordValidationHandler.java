package com.therotherithethethe.persistance.validation.account.password;

import com.therotherithethethe.persistance.validation.TextValidationHandler;

public class PasswordValidationHandler extends TextValidationHandler {

    @Override
    public boolean check(String text) {
        if (!(text.length() >= 4 && text.length() <= 20)) {
            return false;
        }
        return checkNext(text);
    }
}
