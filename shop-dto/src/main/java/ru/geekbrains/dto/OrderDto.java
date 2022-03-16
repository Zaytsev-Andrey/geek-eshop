package ru.geekbrains.dto;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.geekbrains.persist.model.Order;
import ru.geekbrains.persist.model.OrderStatus;


@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	
    private String id;

    private String creationDate;

    private String price;

    private String status;

    public Order toOrder() {
    	Order order = new Order();
    	if (id != null && !id.isBlank()) {
			order.setId(UUID.fromString(id));
		}
    	order.setPrice(new BigDecimal(price));
    	order.setStatus(OrderStatus.valueOf(status));
    	return order;
    }
    
    public static OrderDto fromOrder(Order order) {
    	OrderDto orderDto = new OrderDto();
    	orderDto.setId(order.getId().toString());
    	orderDto.setCreationDate(dateFormat.format(order.getCreationDate()));
    	orderDto.setPrice(order.getPrice().toString());
    	orderDto.setStatus(order.getStatus().toString());
    	return orderDto;
    }
}
