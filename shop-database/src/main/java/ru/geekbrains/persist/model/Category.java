package ru.geekbrains.persist.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title",
            nullable = false)
    private String title;

    @OneToMany(mappedBy = "category")
    private List<Product> products;

    public Category(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
