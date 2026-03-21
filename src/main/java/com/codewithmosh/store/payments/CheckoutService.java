package com.codewithmosh.store.payments;

import com.codewithmosh.store.order.Order;
import com.codewithmosh.store.cart.CartEmptyException;
import com.codewithmosh.store.cart.CartNotFoundException;
import com.codewithmosh.store.cart.CartRepository;
import com.codewithmosh.store.order.OrderRepository;
import com.codewithmosh.store.auth.AuthService;
import com.codewithmosh.store.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CheckoutService {

    private final CartRepository cartRepository;
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final PaymentGateway paymentGateway;




    @Transactional
    public CheckoutResponse checkout(CheckoutRequest request) {
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        if(cart == null){
            throw new CartNotFoundException();
        }
        if(cart.isEmpty()){
           throw new CartEmptyException();
        }
        var order = Order.formCart(cart,authService.getcurrentUser());

        orderRepository.save(order);
        try{
            var session = paymentGateway.createCheckoutSession(order);
            cartService.clearCart(cart.getId());
            return new CheckoutResponse(order.getId(),session.getCheckoutUrl());
        }catch (PaymentException ex){
             orderRepository.delete(order);
            throw ex;

        }
    }

    public void handleWebHookEvent(WebHookRequest request){
        paymentGateway
                .parseWebhookRequest(request)
                .ifPresent(paymentResult ->{
                    var order = orderRepository.findById(paymentResult.getOrderId())
                            .orElseThrow(() -> new RuntimeException("Order not found"));

                    order.setStatus(paymentResult.getPaymentStatus());
                    orderRepository.save(order);
                } );

    }
}
