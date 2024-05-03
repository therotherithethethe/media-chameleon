package com.therotherithethethe.model.entity;

import com.therotherithethethe.model.entity.validation.account.username.UsernameValidationHandler;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
public class Account extends ActiveRecordBase implements Model {
    @Setter
    private UUID id;
    private UUID name;
    private int password;
    private String email;
    private List<String> errors;
    public void setName(UUID name) {
        this.name = name;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
