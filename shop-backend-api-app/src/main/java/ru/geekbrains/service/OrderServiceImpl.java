package ru.geekbrains.service;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.controller.dto.OrderDetailDto;
import ru.geekbrains.controller.dto.ProductDto;
import ru.geekbrains.persist.model.*;
import ru.geekbrains.persist.repository.OrderDetailRepository;
import ru.geekbrains.persist.repository.OrderRepository;
import ru.geekbrains.persist.repository.UserRepository;
import ru.geekbrains.service.dto.LineItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;

    private UserRepository userRepository;

    private OrderDetailRepository orderDetailRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            UserRepository userRepository, OrderDetailRepository orderDetailRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    @Override
    public List<OrderDetailDto> findOrderDetails(Long id) throws NotFoundException {
        Order order = orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found"));

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
    public void save(List<LineItem> lineItems, BigDecimal total, String email) throws NotFoundException {
        Order order = new Order(total,
                OrderStatus.CREATED,
                userRepository.findByEmail(email)
                        .orElseThrow(() -> new NotFoundException("User not found")));

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
    }

    @Override
    public void removeOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Order editOrderDetail(OrderDetailDto orderDetailDto) throws NotFoundException {
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailDto.getId())
                .orElseThrow(() -> new NotFoundException("OrderDetail not found"));

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
    public Order removeOrderDetail(Long id) throws NotFoundException {

        Order order = orderDetailRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("OrderDetail not found"))
                .getOrder();

        order.getOrderDetails().removeIf(detail -> detail.getId() == id);
        orderDetailRepository.deleteById(id);

        updateOrderTotal(order);

        return order;
    }
}
