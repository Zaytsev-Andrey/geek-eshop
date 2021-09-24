package ru.geekbrains.persist.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "productAllEntityGraph",
                attributeNodes = {
                        @NamedAttributeNode("category"),
                        @NamedAttributeNode("brand"),
                }
        ),
        @NamedEntityGraph(
                name = "productByIdEntityGraph",
                attributeNodes = {
                        @NamedAttributeNode("category"),
                        @NamedAttributeNode("brand"),
                        @NamedAttributeNode("pictures")
                }
        )
})
@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 512)
    private String title;

    @Column(name = "cost", nullable = false, precision = 19, scale = 2)
    private BigDecimal cost;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Picture> pictures = new ArrayList<>();

    public Product(Long id, String title, BigDecimal cost, String description, Category category, Brand brand) {
        this.id = id;
        this.title = title;
        this.cost = cost;
        this.description = description;
        this.category = category;
        this.brand = brand;
    }
}
