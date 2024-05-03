package com.therotherithethethe.model.entity.validation.account.username;

import lombok.NoArgsConstructor;

public class ConsecutiveSymbolValidationHandler extends UsernameValidationHandler{

    @Override
    public boolean check(String username) {
        return !username.contains("..") && !username.contains("__");
    }
}
