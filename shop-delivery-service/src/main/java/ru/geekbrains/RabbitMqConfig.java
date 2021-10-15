package ru.geekbrains;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    Queue newOrderQueue() {
        return new Queue("new.order.queue", false);
    }

    @Bean
    Queue processedOrderQueue() {
        return new Queue("processed.order.queue", false);
    }

    @Bean
    DirectExchange orderExchange() {
        return new DirectExchange("order.exchange");
    }

    @Bean
    Binding newOrderBinding(@Qualifier("newOrderQueue") Queue newOrderQueue,
                            DirectExchange directExchange) {
        return BindingBuilder
                .bind(newOrderQueue)
                .to(directExchange)
                .with("new_order");         // routing key
    }

    @Bean
    Binding processedOrderBinding(@Qualifier("processedOrderQueue") Queue processedOrderQueue,
                                  DirectExchange directExchange) {
        return BindingBuilder
                .bind(processedOrderQueue)
                .to(directExchange)
                .with("processed_order");   // routing key
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitMqReceiver rabbitMqReceiver(RabbitTemplate rabbitTemplate) {
        return new RabbitMqReceiver(rabbitTemplate);
    }
}
