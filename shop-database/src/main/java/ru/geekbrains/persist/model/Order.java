package ru.geekbrains.persist.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NamedEntityGraph(
        name = "orderWithDetailsEntityGraph",
        attributeNodes = {
                @NamedAttributeNode(
                        value = "orderDetails",
                        subgraph = "product"
                )
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "product",
                        attributeNodes = {
                                @NamedAttributeNode("product")
                        }
                )
        }
)
@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creation_date", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @org.hibernate.annotations.CreationTimestamp
    private Date creationDate;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    public Order(BigDecimal price, OrderStatus status, User user) {
        this.price = price;
        this.status = status;
        this.user = user;
    }

    public void addDetail(OrderDetail detail) {
        if (detail == null) {
            throw new NullPointerException("Can't add null detail");
        }
        if (detail.getOrder() != null) {
            throw new IllegalStateException("Detail is already assigned to an Order");
        }

        orderDetails.add(detail);
        detail.setOrder(this);
    }
}