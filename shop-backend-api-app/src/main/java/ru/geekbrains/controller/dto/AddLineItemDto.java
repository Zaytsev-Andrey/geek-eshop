package ru.geekbrains.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddLineItemDto {

    private Long productId;

    private String color;

    private String material;

    private boolean saveGiftWrap;

    private boolean giftWrap;

    private Integer qty;
}
