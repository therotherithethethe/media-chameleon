package com.therotherithethethe.domain.services;

import com.therotherithethethe.persistance.entity.Account;
import java.security.SecureRandom;
import lombok.Getter;

/**
 * Service implementation for handling signup operations.
 */
public class SignupService {
    private static SignupService instance;
    public Account curAcc;
    @Getter
    private int validationCode;
    /**
     * Constructs a SignupService with the specified account.
     *
     * @param account the account to associate with this service
     */
    private SignupService(Account account){
        curAcc = account;
    }
    // Private constructor to prevent instantiation
    private SignupService(){
    }
    /**
     * Gets the singleton instance of the SignupService.
     *
     * @return the singleton instance of SignupService
     */
    public static synchronized SignupService getInstance() {
        if(instance == null) {
            instance = new SignupService();
            return instance;
        }
        return instance;
    }
    /**
     * Sends a validation email with a generated validation code.
     */
    public void sendEmail() {
        validationCode = generateCode();
        EmailSender.sendEmail(curAcc.email, "Email verification", String.valueOf(validationCode));
        System.out.println(validationCode);
    }
    /**
     * Generates a random validation code.
     *
     * @return the generated validation code
     */
    private int generateCode() {
        SecureRandom random = new SecureRandom();
        return random.nextInt(999999);
    }

}
