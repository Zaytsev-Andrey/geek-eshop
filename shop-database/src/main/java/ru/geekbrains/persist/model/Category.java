package ru.geekbrains.persist.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
public class Category extends AbstractPersistentObject {

    @Column(name = "title",
            nullable = false)
    private String title;

    @OneToMany(mappedBy = "category")
    private Set<Product> products;

    public Category(String title) {
		super();
		this.title = title;
	}
    
    public Category(UUID id, String title) {
        super(id);
        this.title = title;
    }


}
