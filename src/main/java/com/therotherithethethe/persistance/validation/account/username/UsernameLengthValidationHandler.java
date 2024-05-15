package com.therotherithethethe.persistance.validation.account.username;

import com.therotherithethethe.persistance.validation.TextValidationHandler;

public class UsernameLengthValidationHandler extends TextValidationHandler {

    @Override
    public boolean check(String text) {
        if (!(text.length() >= 4 && text.length() <= 25)) {
            return false;
        }
        return checkNext(text);
    }
}
