package com.therotherithethethe.model.entity.validation.account.username;

import com.therotherithethethe.model.entity.validation.TextValidationHandler;

public class UsernameLengthValidationHandler extends TextValidationHandler {

  @Override
  public boolean check(String text) {
    if (!(text.length() >= 4 && text.length() <= 15)) {
      return false;
    }
    return checkNext(text);
  }
}
