package ru.geekbrains.persist;

import lombok.*;
import ru.geekbrains.persist.AbstractPersistentObject;
import ru.geekbrains.persist.Brand;
import ru.geekbrains.persist.Category;
import ru.geekbrains.persist.Picture;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
        ),
        @NamedEntityGraph(
                name = "productByIdWithPicturesEntityGraph",
                attributeNodes = {
                        @NamedAttributeNode("pictures")
                }
        )
})
@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class Product extends AbstractPersistentObject {

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
    private Set<Picture> pictures = new HashSet<Picture>();

    public Product(UUID id, String title, BigDecimal cost, String description, Category category, Brand brand) {
        super(id);
        this.title = title;
        this.cost = cost;
        this.description = description;
        this.category = category;
        this.brand = brand;
    }

    public void addPicture(Picture picture) {
        if (picture == null) {
            throw new NullPointerException("Can't add null pucture");
        }
        if (picture.getProduct() != null) {
            throw new IllegalStateException("Picture is already assigned to an Product");
        }

        picture.setProduct(this);
        pictures.add(picture);
    }
}
