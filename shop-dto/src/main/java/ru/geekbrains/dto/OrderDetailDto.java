package ru.geekbrains.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDto {

    private String id;

    private ProductDto productDto;

    private Integer qty;

    private String cost;

    private Boolean giftWrap;

}
