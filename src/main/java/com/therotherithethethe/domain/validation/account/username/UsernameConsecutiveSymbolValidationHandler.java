package com.therotherithethethe.domain.validation.account.username;

import com.therotherithethethe.domain.validation.TextValidationHandler;
/**
 * Handler for username consecutive symbol validation in the chain of responsibility pattern.
 */
public class UsernameConsecutiveSymbolValidationHandler extends TextValidationHandler {
  /**
   * Checks if the username does not contain consecutive dots or underscores.
   *
   * @param text the username text to check
   * @return true if the username does not contain consecutive dots or underscores, false otherwise
   */
  @Override
  public boolean check(String text) {
    if (text.contains("..") || text.contains("__")) {
      return false;
    }
    return checkNext(text);
  }
}
