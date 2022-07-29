package ru.geekbrains.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddLineItemDto {

    private String productId;

    private boolean saveGiftWrap;

    private boolean giftWrap;

    private Integer qty;
}
