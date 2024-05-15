package com.therotherithethethe.persistance.validation;

public abstract class TextValidationHandler {
  private TextValidationHandler next;

  public static TextValidationHandler link(
      TextValidationHandler first, TextValidationHandler... chain) {
    TextValidationHandler head = first;
    for (TextValidationHandler nextInChain : chain) {
      head.next = nextInChain;
      head = nextInChain;
    }
    return first;
  }

  public abstract boolean check(String text);

  protected boolean checkNext(String text) {
    if (next == null) {
      return true;
    }
    return next.check(text);
  }
}
