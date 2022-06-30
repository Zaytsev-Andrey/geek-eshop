package ru.geekbrains.dto;


import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto extends AbstractPersistentDto implements Serializable {

    private String productTitle;

    private Boolean changeGiftWrap;

    private Boolean giftWrap;
    
    private String cost;
    
    private String totalCost;

    private Integer qty;

    public CartItemDto(String id, String productTitle, Boolean changeGiftWrap, Boolean giftWrap, String cost, String totalCost, Integer qty) {
        super(id);
        this.productTitle = productTitle;
        this.changeGiftWrap = changeGiftWrap;
        this.giftWrap = giftWrap;
        this.cost = cost;
        this.totalCost = totalCost;
        this.qty = qty;
    }
}
