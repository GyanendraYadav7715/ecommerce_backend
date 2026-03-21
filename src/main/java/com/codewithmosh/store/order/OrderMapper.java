package com.codewithmosh.store.order;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    // Maps the main Order
    OrderDto toDto(Order order);

    // ADD THIS: Maps individual items inside the list
    OrderItemDto toOrderItemDto(OrderItem orderItem);
}