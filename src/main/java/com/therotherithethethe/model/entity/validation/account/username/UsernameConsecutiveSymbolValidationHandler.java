package com.therotherithethethe.model.entity.validation.account.username;

import com.therotherithethethe.model.entity.validation.TextValidationHandler;

public class UsernameConsecutiveSymbolValidationHandler extends TextValidationHandler {

  @Override
  public boolean check(String text) {
    if (text.contains("..") || text.contains("__")) {
      return false;
    }
    return checkNext(text);
  }
}
