package com.therotherithethethe.model.entity.validation.account.username;

import com.therotherithethethe.model.entity.validation.TextValidationHandler;

public class UsernameCharacterValidationHandler extends TextValidationHandler {
  @Override
  public boolean check(String text) {
    if (!text.matches("[a-zA-Z0-9._]+")) {
      return false;
    }
    return checkNext(text);
  }
}
