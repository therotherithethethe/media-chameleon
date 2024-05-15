package com.therotherithethethe.domain.services;

import com.therotherithethethe.persistance.entity.Account;
import java.security.SecureRandom;
import lombok.Getter;

public class SignupServiceImpl {
    private static SignupServiceImpl instance;
    public Account curAcc;
    @Getter
    private int validationCode;
    private SignupServiceImpl(Account account){
        curAcc = account;
    }
    private SignupServiceImpl(){
    }
    public static synchronized SignupServiceImpl getInstance() {
        if(instance == null) {
            instance = new SignupServiceImpl();
            return instance;
        }
        return instance;
    }
    public void sendEmail() {
        validationCode = generateCode();
        //EmailSender.sendEmail(curAcc.email, "Email verification", String.valueOf(validationCode));
        System.out.println(validationCode);
    }
    private int generateCode() {
        SecureRandom random = new SecureRandom();
        return random.nextInt(999999);
    }

}
