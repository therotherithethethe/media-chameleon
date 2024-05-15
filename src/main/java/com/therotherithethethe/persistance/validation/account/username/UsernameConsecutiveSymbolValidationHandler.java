package com.therotherithethethe.persistance.validation.account.username;

import com.therotherithethethe.persistance.validation.TextValidationHandler;

public class UsernameConsecutiveSymbolValidationHandler extends TextValidationHandler {

  @Override
  public boolean check(String text) {
    if (text.contains("..") || text.contains("__")) {
      return false;
    }
    return checkNext(text);
  }
}
