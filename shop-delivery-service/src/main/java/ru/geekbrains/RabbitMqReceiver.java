package ru.geekbrains;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import ru.geekbrains.service.dto.OrderMessage;
import ru.geekbrains.service.dto.OrderStatus;

@RabbitListener(queues = "new.order.queue")
public class RabbitMqReceiver {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMqReceiver.class);

    private final AmqpTemplate rabbitTemplate;

    public RabbitMqReceiver(AmqpTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitHandler
    public void receive(OrderMessage orderMessage) {
        logger.info("New order with id '{}'", orderMessage.getId());

        new Thread(() -> {
            for (OrderStatus status : OrderStatus.values()) {
                try {
                    Thread.sleep(10000);
                    orderMessage.setStatus(status.name());
                    logger.info("Changing status for order '{}' to '{}'", orderMessage.getId(), orderMessage.getStatus());
                    rabbitTemplate.convertAndSend("order.exchange", "processed_order", orderMessage);
                } catch (InterruptedException e) {
                    logger.error("Interrupted", e);
                    break;
                }
            }
        }).start();
    }

}
