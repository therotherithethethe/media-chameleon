package com.therotherithethethe.persistance.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Account entity class.
 */
@Entity
public class Account extends ActiveRecordBase<Account> implements Model, Comparable<Account> {

    public String name;
    public String email;
    @Id
    @GeneratedValue
    private UUID id;
    @Getter
    private int password;
    @OneToMany(mappedBy = "account",  cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    @Setter
    public List<Session> session;

    /**
     * Constructs an account with the specified ID, name, email, and password.
     *
     * @param id the ID of the account
     * @param name the name of the account
     * @param email the email of the account
     * @param password the password of the account
     */
    public Account(UUID id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        setPassword(String.valueOf(password));
    }
    /**
     * Default constructor.
     */
    public Account() {
    }
    /**
     * Compares this account to another account by name.
     *
     * @param o the account to compare to
     * @return a negative integer, zero, or a positive integer as this account is less than, equal to, or greater than the specified account
     */
    @Override
    public int compareTo(Account o) {
        return name.compareTo(o.name);
    }
    /**
     * Sets the password for the account.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password.hashCode();
    }
    /**
     * Checks if this account is equal to another object.
     *
     * @param o the object to compare to
     * @return true if this account is equal to the specified object, false otherwise
     */
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
    /**
     * Gets the ID of the account.
     *
     * @return the ID of the account
     */
    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
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
