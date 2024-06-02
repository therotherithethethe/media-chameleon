package com.therotherithethethe.persistance.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class File extends ActiveRecordBase<File> implements Model {
    @Id
    @GeneratedValue
    @Getter
    @Setter
    private UUID id;
    public String path;
    @ManyToMany(mappedBy = "files")

    public List<Session> sessions;
    public File(UUID id, String path) {
        this.id = id;
        this.path = path;
    }
}
