package com.therotherithethethe.persistance.validation.account.email;

import com.therotherithethethe.persistance.validation.TextValidationHandler;

public class EmailValidationHandler extends TextValidationHandler {

    @Override
    public boolean check(String text) {
        if (!text.matches(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            return false;
        }
        return checkNext(text);
    }
}
