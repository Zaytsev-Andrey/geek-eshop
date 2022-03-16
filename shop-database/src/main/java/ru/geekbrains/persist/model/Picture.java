package ru.geekbrains.persist.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "pictures")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Picture extends AbstractPersistentObject {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "storage_uuid", nullable = false)
    private String storageUUID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public Picture(String name, String contentType, String storageUUID) {
        this.name = name;
        this.contentType = contentType;
        this.storageUUID = storageUUID;
    }
}
