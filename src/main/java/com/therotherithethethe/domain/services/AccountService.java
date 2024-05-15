package com.therotherithethethe.domain.services;

import com.therotherithethethe.persistance.entity.Account;
import java.util.Objects;

public class AccountService {
    private static AccountService instance;
    private static final Account DAO = new Account();

    public static synchronized AccountService getInstance() {
        if(Objects.isNull(instance)) {
            instance = new AccountService();
        }
        return instance;
    }

    private AccountService(){}
    public boolean isExistInDb(Account account) {

        return DAO
            .findByColumn(acc -> acc.equals(account))
            .stream()
            .findFirst()
            .isPresent();
    }
}
