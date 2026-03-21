package com.codewithmosh.store.payments;

import com.codewithmosh.store.order.Order;

import java.util.Optional;

public interface PaymentGateway {

    CheckoutSession createCheckoutSession(Order order);
    Optional<PaymentResult> parseWebhookRequest(WebHookRequest request);

}
