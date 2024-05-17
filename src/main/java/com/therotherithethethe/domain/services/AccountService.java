package com.therotherithethethe.domain.services;

import com.therotherithethethe.persistance.entity.Account;
import java.util.Objects;
/**
 * Service class for handling account-related operations.
 */
public class AccountService {
    private static AccountService instance;
    private static final Account DAO = new Account();
    /**
     * Gets the singleton instance of the AccountService.
     *
     * @return the singleton instance of AccountService
     */
    public static synchronized AccountService getInstance() {
        if(Objects.isNull(instance)) {
            instance = new AccountService();
        }
        return instance;
    }
    // Private constructor to prevent instantiation
    private AccountService(){}
    /**
     * Checks if the specified account exists in the database.
     *
     * @param account the account to check
     * @return true if the account exists in the database, false otherwise
     */
    public boolean isExistInDb(Account account) {

        return DAO
            .findByColumn(acc -> acc.equals(account))
            .stream()
            .findFirst()
            .isPresent();
    }
}
