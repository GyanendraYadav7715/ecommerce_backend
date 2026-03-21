package com.codewithmosh.store.cart;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCartItemRequest {

    @NotNull(message = "Quantity must be provided")
    private Integer quantity;
}
