package com.therotherithethethe.persistance.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Entity
public class Account extends ActiveRecordBase<Account> implements Model, Comparable<Account> {

    public String name;
    public String email;
    @Setter
    @Getter
    @Id
    @GeneratedValue
    private UUID id;
    @Getter
    private int password;

    public Account(UUID id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        setPassword(String.valueOf(password));
    }

    public Account() {
    }

    @Override
    public int compareTo(Account o) {
        return name.compareTo(o.name);
    }

    public void setPassword(String password) {
        this.password = password.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Account account = (Account) o;

        if (name.equals(account.name)) {
            return true;
        }
        return email.equals(account.email);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + password;
        result = 31 * result + email.hashCode();
        return result;
    }
}
