package com.therotherithethethe.domain.validation.account.username;

import com.therotherithethethe.domain.validation.TextValidationHandler;
/**
 * Handler for username character validation in the chain of responsibility pattern.
 */
public class UsernameCharacterValidationHandler extends TextValidationHandler {
  /**
   * Checks if the username contains only valid characters (letters, digits, dots, and underscores).
   *
   * @param text the username text to check
   * @return true if the username contains only valid characters, false otherwise
   */
  @Override
  public boolean check(String text) {
    if (!text.matches("[a-zA-Z0-9._]+")) {
      return false;
    }
    return checkNext(text);
  }
}
