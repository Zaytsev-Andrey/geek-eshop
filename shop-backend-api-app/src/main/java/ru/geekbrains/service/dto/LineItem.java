package ru.geekbrains.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.geekbrains.controller.dto.ProductDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class LineItem implements Serializable {

    private Long productId;

    private ProductDto productDto;

    private String color;

    private String material;

    private Boolean saveGiftWrap;

    private Boolean giftWrap;

    private Integer qty;

    public LineItem(ProductDto productDto, String color, String material,
                    Boolean setGiftWrap, Boolean giftWrap, Integer qty) {
        this.productId = productDto.getId();
        this.productDto = productDto;
        this.color = color;
        this.material = material;
        this.saveGiftWrap = setGiftWrap;
        this.giftWrap = giftWrap;
        this.qty = qty;
    }

    public BigDecimal getItemTotal() {
        return productDto.getCost().multiply(new BigDecimal(qty));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineItem lineItem = (LineItem) o;
        return Objects.equals(productId, lineItem.productId) &&
                Objects.equals(color, lineItem.color) &&
                Objects.equals(material, lineItem.material) &&
                Objects.equals(saveGiftWrap, lineItem.getSaveGiftWrap());

    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, color, material, saveGiftWrap);
    }
}
