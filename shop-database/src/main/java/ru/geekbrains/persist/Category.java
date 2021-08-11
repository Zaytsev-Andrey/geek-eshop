package ru.geekbrains.persist;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
