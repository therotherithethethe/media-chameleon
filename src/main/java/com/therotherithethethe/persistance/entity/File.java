package com.therotherithethethe.persistance.entity;

import jakarta.persistence.Column;
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

/**
 * The {@code File} class represents a file entity.
 */
@Entity
@NoArgsConstructor
public class File extends ActiveRecordBase<File> implements Model {
    @Id
    @GeneratedValue
    @Getter
    @Setter
    private UUID id;
    @Column(unique = true, nullable = false)
    public String path;
    @ManyToMany(mappedBy = "files")

    public List<Session> sessions;
    /**
     * Constructs a {@code File} with the given id and path.
     *
     * @param id the unique identifier of the file
     * @param path the path of the file
     */
    public File(UUID id, String path) {
        this.id = id;
        this.path = path;
    }
}
