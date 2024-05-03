package com.therotherithethethe.model.entity.validation.account.username;

public abstract class UsernameValidationHandler {
    private UsernameValidationHandler next;
    public static UsernameValidationHandler link(UsernameValidationHandler first, UsernameValidationHandler... chain) {
        UsernameValidationHandler head = first;
        for (UsernameValidationHandler nextInChain: chain) {
            head.next = nextInChain;
            head = nextInChain;
        }
        return first;
    }

    public abstract boolean check(String username);
    protected boolean checkNext(String username) {
        if (next == null) {
            return true;
        }
        return next.check(username);
    }
}