package com.therotherithethethe.domain.validation.account.username;

import com.therotherithethethe.domain.validation.TextValidationHandler;
/**
 * Handler for username length validation in the chain of responsibility pattern.
 */
public class UsernameLengthValidationHandler extends TextValidationHandler {
    /**
     * Checks if the username length is between 4 and 25 characters.
     *
     * @param text the username text to check
     * @return true if the username length is valid, false otherwise
     */
    @Override
    public boolean check(String text) {
        if (!(text.length() >= 4 && text.length() <= 25)) {
            return false;
        }
        return checkNext(text);
    }
}
