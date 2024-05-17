package com.therotherithethethe.domain.validation.account.email;

import com.therotherithethethe.domain.validation.TextValidationHandler;
/**
 * Handler for email validation in the chain of responsibility pattern.
 */
public class EmailValidationHandler extends TextValidationHandler {
    /**
     * Checks if the email is valid.
     *
     * @param text the email text to check
     * @return true if the email is valid, false otherwise
     */
    @Override
    public boolean check(String text) {
        if (!text.matches(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            return false;
        }
        return checkNext(text);
    }
}
