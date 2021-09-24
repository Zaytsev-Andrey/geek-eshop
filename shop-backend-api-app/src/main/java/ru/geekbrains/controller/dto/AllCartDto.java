package ru.geekbrains.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.geekbrains.service.dto.LineItem;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AllCartDto {

    private List<LineItem> lineItems;

    private BigDecimal subtotal;
}
