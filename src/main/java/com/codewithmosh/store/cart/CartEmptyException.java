package com.codewithmosh.store.cart;

public class CartEmptyException extends  RuntimeException{
    public CartEmptyException(){
        super("cart is empty");
    }
}
