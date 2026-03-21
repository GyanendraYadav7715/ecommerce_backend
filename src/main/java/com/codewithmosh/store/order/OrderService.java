package com.codewithmosh.store.order;

import com.codewithmosh.store.auth.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderService {

    private  final AuthService authService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public List<OrderDto> getAllOrders(){
        var user=authService.getcurrentUser();
        var order = orderRepository.getAllByCustomer(user);
        return order.stream().map(orderMapper::toDto).toList();
    }
    public  OrderDto getOrder(Long orderId){
     var order = orderRepository.getOrderWithItmes(orderId).orElse(null);
     if(order == null){
         throw new OrderNotFoundException();
     }
     var user = authService.getcurrentUser();
     if(!order.isPlacedBy(user)){
        throw  new AccessDeniedException("You do not have access to this order");
     }
     return orderMapper.toDto(order);
    }
}
