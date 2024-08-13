package fr.orleans.serviceevenement.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${spring.rabbitmq.host}")
    String host;
    @Value("${spring.rabbitmq.username}")
    String username;
    @Value("${spring.rabbitmq.password}")
    String password;

    @Value("${spring.rabbitmq.queue-evenement}")
    String queue;
    @Value("${spring.rabbitmq.exchange-evenement}")
    private String exchange;
    @Value("${spring.rabbitmq.routingkey-evenement}")
    private String routingKey;

    @Value("${spring.rabbitmq.queue-evenement2}")
    String queue2;
    @Value("${spring.rabbitmq.exchange-evenement2}")
    private String exchange2;
    @Value("${spring.rabbitmq.routingkey-evenement2}")
    private String routingKey2;


    @Value("${spring.rabbitmq.queue-evenement3}")
    String queue3;
    @Value("${spring.rabbitmq.exchange-evenement3}")
    private String exchange3;
    @Value("${spring.rabbitmq.routingkey-evenement3}")
    private String routingKey3;


    @Bean
    CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(host);
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);
        return cachingConnectionFactory;
    }
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Queue myQueue() {
        return new Queue(queue,true);
    }

    @Bean
    public Queue myQueue2() { return new Queue(queue2,true); }

    @Bean
    public Queue myQueue3() { return new Queue(queue3,true); }


    @Bean
    Exchange myExchange() {
        return ExchangeBuilder.directExchange(exchange).durable(true).build();
    }

    @Bean
    Exchange myExchange2() { return ExchangeBuilder.directExchange(exchange2).durable(true).build();}

    @Bean
    Exchange myExchange3() { return ExchangeBuilder.directExchange(exchange3).durable(true).build(); }

    @Bean("binding1")
    Binding binding() {
        return BindingBuilder
                .bind(myQueue())
                .to(myExchange())
                .with(routingKey)
                .noargs();
    }

    @Bean("binding2")
    Binding binding2() {
        return BindingBuilder
                .bind(myQueue2())
                .to(myExchange2())
                .with(routingKey2)
                .noargs();
    }


    @Bean("binding3")
    Binding binding3() {
        return BindingBuilder
                .bind(myQueue3())
                .to(myExchange3())
                .with(routingKey3)
                .noargs();
    }
}
