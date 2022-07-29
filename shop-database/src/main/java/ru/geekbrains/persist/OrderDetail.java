package ru.geekbrains.persist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

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
public class OrderDetail extends AbstractPersistentEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "count", nullable = false)
    private Integer qty;

    @Column(name = "cost", nullable = false)
    private BigDecimal cost;

    @Column(name = "gift_wrap", nullable = false)
    private Boolean giftWrap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    public OrderDetail(UUID id, Product product, Integer qty, BigDecimal cost, Boolean giftWrap, Order order) {
        super(id);
        this.product = product;
        this.qty = qty;
        this.cost = cost;
        this.giftWrap = giftWrap;
        this.order = order;
    }

    public OrderDetail(Product product, Integer count, BigDecimal cost, Boolean giftWrap) {
        this.product = product;
        this.qty = count;
        this.cost = cost;
        this.giftWrap = giftWrap;
    }
}
