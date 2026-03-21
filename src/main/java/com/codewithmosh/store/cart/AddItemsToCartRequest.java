package com.codewithmosh.store.cart;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddItemsToCartRequest {
    @NotNull(message = "ProductId  is not blank")
    private Long productId;
}
