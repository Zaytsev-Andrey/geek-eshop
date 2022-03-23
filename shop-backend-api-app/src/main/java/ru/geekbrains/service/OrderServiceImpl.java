package ru.geekbrains.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.geekbrains.dto.AllCartDto;
import ru.geekbrains.dto.OrderDetailDto;
import ru.geekbrains.exception.OrderDetailNotFoundException;
import ru.geekbrains.exception.OrderNotFoundException;
import ru.geekbrains.exception.UserNotFoundException;
import ru.geekbrains.persist.*;
import ru.geekbrains.repository.OrderDetailRepository;
import ru.geekbrains.repository.OrderRepository;
import ru.geekbrains.repository.UserRepository;
import ru.geekbrains.service.dto.OrderMessage;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private OrderRepository orderRepository;

    private UserRepository userRepository;

    private OrderDetailRepository orderDetailRepository;
    
    private ProductService productService;
    
    private CartService cartService;

    private RabbitTemplate rabbitTemplate;

    private SimpMessagingTemplate webSocketTemplate;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            UserRepository userRepository,
                            OrderDetailRepository orderDetailRepository,
                            ProductService productService,
                            CartService cartService,
                            RabbitTemplate rabbitTemplate,
                            SimpMessagingTemplate webSocketTemplate) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.productService = productService;
        this.cartService = cartService;
        this.rabbitTemplate = rabbitTemplate;
        this.webSocketTemplate = webSocketTemplate;
    }

    @Override
    public List<OrderDetailDto> getOrderDetails(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        
        List<OrderDetailDto> result =  order.getOrderDetails().stream()
                .map(detail -> OrderDetailDto.fromOrderDetail(detail))
                .collect(Collectors.toList());

        return result;
    }

    @Transactional
    @Override
    public AllCartDto save(String email) {
    	if (!cartService.isEmpti()) {
    		List<CartItem> cartItems = cartService.getCartItems();
    		Map<UUID, Product> products = cartService.getCartProducts(cartItems);
    		
    		Set<OrderDetail> orderDetails = cartItems.stream()
    				.map(cartItem -> new OrderDetail(
    							products.get(cartItem.getId()),
    							cartItem.getQty(),
    							products.get(cartItem.getId()).getCost(),
    							cartItem.getGiftWrap()
					))
    				.collect(Collectors.toSet());
    		
    		
    		Order order = new Order(
	    					orderDetails.stream()
	    							.map(detail -> detail.getCost().multiply(new BigDecimal(detail.getQty())))
	    							.reduce(BigDecimal.ZERO, BigDecimal::add),
		                    OrderStatus.CREATED,
		                    userRepository.findByEmail(email)
		                            .orElseThrow(() -> new UserNotFoundException(email))
                    );
    		
    		orderDetails.forEach(detail -> order.addDetail(detail));
    		
    		orderRepository.save(order);
    		
    		cartService.clear();
    		
    		rabbitTemplate.convertAndSend("order.exchange", "new_order",
                    new OrderMessage(order.getId().toString(), order.getStatus().getName()));
    		
    	}
    	
        return cartService.getCartDto();
    }

    private Order changeOrderStatus(UUID orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        order.setStatus(OrderStatus.valueOf(newStatus));

        return orderRepository.save(order);
    }

    @Override
    public void removeOrder(UUID id) {
        orderRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Order editOrderDetail(OrderDetailDto orderDetailDto) {
    	OrderDetail orderDetail = orderDetailRepository.findById(UUID.fromString(orderDetailDto.getId()))
                .orElseThrow(() -> new OrderDetailNotFoundException(UUID.fromString(orderDetailDto.getId())));

        orderDetail.setQty(orderDetailDto.getQty());
        orderDetail.setCost(new BigDecimal(orderDetailDto.getProductDto().getCost()) 
                .multiply(new BigDecimal(orderDetailDto.getQty())));
        orderDetail.setGiftWrap(orderDetailDto.getGiftWrap());

        Order order = orderDetail.getOrder();
        updateOrderTotal(order);

        return order;
    }

    private void updateOrderTotal(Order order) {
        BigDecimal total = order.getOrderDetails().stream()
                .map(OrderDetail::getCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setPrice(total);
        order.setStatus(OrderStatus.MODIFIED);

//        orderRepository.save(order);
    }

    @Transactional
    @Override
    public Order removeOrderDetail(UUID id) {

        Order order = orderDetailRepository.findById(id)
                .orElseThrow(() -> new OrderDetailNotFoundException(id))
                .getOrder();

        order.getOrderDetails().removeIf(detail -> detail.getId().equals(id));
        orderDetailRepository.deleteById(id);

        updateOrderTotal(order);

        return order;
    }

    @RabbitListener(queues = "processed.order.queue")
    public void receive(OrderMessage orderMessage) {
        logger.info("Order with id '{}' state change to '{}'", orderMessage.getId(), orderMessage.getStatus());

        Order order = changeOrderStatus(UUID.fromString(orderMessage.getId()), orderMessage.getStatus());
        orderMessage.setStatus(order.getStatus().getName());

        webSocketTemplate.convertAndSend("/order_out/order", orderMessage);
    }
}
