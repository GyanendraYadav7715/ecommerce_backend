package com.codewithmosh.store.cart;

public class CartNotFoundException extends  RuntimeException {
     public  CartNotFoundException(){
         super("Cart Not Found");
     }
}
