package ru.geekbrains.persist.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@NamedEntityGraph(
        name = "orderDetailWithOrderEntityGraph",
        attributeNodes = {
                @NamedAttributeNode("order")
        }
)
@Entity
@Table(name = "order_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "count", nullable = false)
    private Integer count;

    @Column(name = "cost", nullable = false)
    private BigDecimal cost;

    @Column(name = "gift_wrap", nullable = false)
    private Boolean giftWrap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    public OrderDetail(Product product, Integer count, BigDecimal cost, Boolean giftWrap) {
        this.product = product;
        this.count = count;
        this.cost = cost;
        this.giftWrap = giftWrap;
    }
}
