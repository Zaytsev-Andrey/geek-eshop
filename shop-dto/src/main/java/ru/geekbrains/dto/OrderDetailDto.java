package ru.geekbrains.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.geekbrains.persist.OrderDetail;


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

    public OrderDetailDto(ProductDto productDto, Integer qty, String cost, Boolean giftWrap) {
        this.productDto = productDto;
        this.qty = qty;
        this.cost = cost;
        this.giftWrap = giftWrap;
    }
    
    public OrderDetail toOrderDetail() {
    	OrderDetail orderDetail = new OrderDetail();
    	if (id != null && !id.isBlank()) {
    		orderDetail.setId(UUID.fromString(id));
    	}
    	orderDetail.setProduct(productDto.toProduct());
    	orderDetail.setQty(qty);
    	orderDetail.setCost(new BigDecimal(cost));
    	orderDetail.setGiftWrap(giftWrap);
    	return orderDetail;
    }
    
    public static OrderDetailDto fromOrderDetail(OrderDetail orderDetail) {
    	OrderDetailDto orderDetailDto = new OrderDetailDto();
    	orderDetailDto.setId(orderDetail.getId().toString());
    	orderDetailDto.setProductDto(ProductDto.fromProduct(orderDetail.getProduct()));
    	orderDetailDto.setQty(orderDetail.getQty());
    	orderDetailDto.setCost(orderDetail.getCost().toString());
    	orderDetailDto.setGiftWrap(orderDetail.getGiftWrap());
    	return orderDetailDto;
    }
}
