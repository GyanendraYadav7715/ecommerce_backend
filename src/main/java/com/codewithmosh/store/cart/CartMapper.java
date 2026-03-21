package com.codewithmosh.store.cart;

import com.codewithmosh.store.product.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "items", source = "cartItems")
    @Mapping(target = "totalPrice",expression  = "java(cart.getTotalPrice())")
    CartDto toCartDto(Cart cart);
    @Mapping(target = "totalPrice",expression = "java(cartItem.getTotalPrice())")
    CartItemDto toDto(CartItem cartItem);


    CartProductDto toCartProductDto(Product product);

}
