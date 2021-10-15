package ru.geekbrains.service;

import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.controller.dto.OrderDetailDto;
import ru.geekbrains.controller.dto.OrderDto;
import ru.geekbrains.controller.dto.ProductDto;
import ru.geekbrains.exception.OrderDetailNotFoundException;
import ru.geekbrains.exception.OrderNotFoundException;
import ru.geekbrains.exception.UserNotFoundException;
import ru.geekbrains.persist.model.*;
import ru.geekbrains.persist.repository.OrderDetailRepository;
import ru.geekbrains.persist.repository.OrderRepository;
import ru.geekbrains.persist.repository.UserRepository;
import ru.geekbrains.service.dto.LineItem;
import ru.geekbrains.service.dto.OrderMessage;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private OrderRepository orderRepository;

    private UserRepository userRepository;

    private OrderDetailRepository orderDetailRepository;

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            UserRepository userRepository,
                            OrderDetailRepository orderDetailRepository,
                            RabbitTemplate rabbitTemplate) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public List<OrderDetailDto> getOrderDetails(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        List<OrderDetailDto> result =  order.getOrderDetails().stream()
                .map(detail -> new OrderDetailDto(
                        detail.getId(),
                        new ProductDto(
                                detail.getProduct().getId(),
                                detail.getProduct().getTitle(),
                                detail.getProduct().getCost(),
                                detail.getProduct().getDescription()
                        ),
                        detail.getCount(),
                        detail.getCost(),
                        detail.getGiftWrap()
                )).collect(Collectors.toList());

        return result;
    }

    @Transactional
    @Override
    public void save(List<LineItem> lineItems, BigDecimal total, String email) {
        Order order = new Order(total,
                OrderStatus.CREATED,
                userRepository.findByEmail(email)
                        .orElseThrow(() -> new UserNotFoundException(email)));

        lineItems.forEach(lineItem -> {
            ProductDto productDto = lineItem.getProductDto();
            Product product = new Product(
                    productDto.getId(),
                    productDto.getTitle(),
                    productDto.getCost(),
                    productDto.getDescription(),
                    new Category(productDto.getCategoryDto().getId(), productDto.getCategoryDto().getTitle()),
                    new Brand(productDto.getBrandDto().getId(), productDto.getBrandDto().getTitle())
            );
            OrderDetail orderDetail = new OrderDetail(product,
                    lineItem.getQty(),
                    lineItem.getItemTotal(),
                    lineItem.getGiftWrap());

            order.addDetail(orderDetail);
        });

        orderRepository.save(order);

        rabbitTemplate.convertAndSend("order.exchange", "new_order",
                new OrderMessage(order.getId(), order.getStatus().getName()));
    }

    @Override
    public void changeOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        order.setStatus(OrderStatus.valueOf(newStatus));

        orderRepository.save(order);
    }

    @Override
    public void removeOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Order editOrderDetail(OrderDetailDto orderDetailDto) {
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailDto.getId())
                .orElseThrow(() -> new OrderDetailNotFoundException(orderDetailDto.getId()));

        orderDetail.setCount(orderDetailDto.getQty());
        orderDetail.setCost(orderDetailDto.getProductDto().getCost()
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
    public Order removeOrderDetail(Long id) {

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

        changeOrderStatus(orderMessage.getId(), orderMessage.getStatus());
    }
}
