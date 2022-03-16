package ru.geekbrains.persist.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "brands")
@Getter
@Setter
@NoArgsConstructor
public class Brand extends AbstractPersistentObject {

    @Column(name = "title", nullable = false)
    private String title;

    @OneToMany(mappedBy = "brand")
    private Set<Product> products;

	public Brand(String title) {
		super();
		this.title = title;
	}

	public Brand(UUID id, String title) {
		super(id);
		this.title = title;
	}
	
}
