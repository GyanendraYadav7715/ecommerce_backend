package com.codewithmosh.store.order;

import com.codewithmosh.store.dtos.OrderProductDto;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private OrderProductDto product;
    private int quantity;
    private BigDecimal totalPrice;
}
