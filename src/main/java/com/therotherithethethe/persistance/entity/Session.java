package com.therotherithethethe.persistance.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class Session extends ActiveRecordBase<Session> implements Model {
    @Id
    @GeneratedValue
    @Getter
    @Setter
    private UUID id;
    public LocalDateTime dateTime;
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "account_id")
    public Account account;
    @ManyToMany
    @JoinTable(
        name = "session_file",
        joinColumns = { @JoinColumn(name = "session_id") },
        inverseJoinColumns = { @JoinColumn(name = "file_id") }
    )
    public List<File> files;
    public Session(UUID id, LocalDateTime dateTime) {
        this.id = id;
        this.dateTime = dateTime;
    }
}
