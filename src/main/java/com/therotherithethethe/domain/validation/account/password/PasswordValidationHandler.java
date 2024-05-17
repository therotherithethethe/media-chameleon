package com.therotherithethethe.domain.validation.account.password;

import com.therotherithethethe.domain.validation.TextValidationHandler;
/**
 * Handler for password validation in the chain of responsibility pattern.
 */
public class PasswordValidationHandler extends TextValidationHandler {
    /**
     * Checks if the password length is between 4 and 20 characters.
     *
     * @param text the password text to check
     * @return true if the password length is valid, false otherwise
     */
    @Override
    public boolean check(String text) {
        if (!(text.length() >= 4 && text.length() <= 20)) {
            return false;
        }
        return checkNext(text);
    }
}
