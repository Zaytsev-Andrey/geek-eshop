package ru.geekbrains.controller.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.geekbrains.persist.model.Product;

import java.math.BigDecimal;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDto {

    private Long id;

    private ProductDto productDto;

    private Integer qty;

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")
    @JsonSubTypes({@JsonSubTypes.Type(name = "BIG_DECIMAL", value = BigDecimal.class)})
    private BigDecimal cost;

    private Boolean giftWrap;

    public OrderDetailDto(ProductDto productDto, Integer qty, BigDecimal cost, Boolean giftWrap) {
        this.productDto = productDto;
        this.qty = qty;
        this.cost = cost;
        this.giftWrap = giftWrap;
    }
}
