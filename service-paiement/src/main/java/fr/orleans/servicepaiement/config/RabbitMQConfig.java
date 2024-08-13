package fr.orleans.servicepaiement.config;

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
    @Value("${spring.rabbitmq.queue-pari-gagne}")
    private String queuePariGagne;
    @Value("${spring.rabbitmq.exchange-pari-gagne}")
    private String exchangePariGagne;
    @Value("${spring.rabbitmq.routingkey-pari-gagne}")
    private String routingKeyPariGagne;

    @Value("${spring.rabbitmq.queue-pari-annule}")
    private String queuePariAnnule;
    @Value("${spring.rabbitmq.exchange-pari-annule}")
    private String exchangePariAnnule;
    @Value("${spring.rabbitmq.routingkey-pari-annule}")
    private String routingKeyPariAnnule;

    @Value("${spring.rabbitmq.queue-inscription}")
    private String queueInscription;
    @Value("${spring.rabbitmq.exchange-inscription}")
    private String exchangeInscription;
    @Value("${spring.rabbitmq.routingkey-inscription}")
    private String routingKeyInscription;

    @Value("${spring.rabbitmq.queue-paiement-nouveau}")
    private String queuePaiementNouveau;
    @Value("${spring.rabbitmq.exchange-paiement-nouveau}")
    private String exchangePaiementNouveau;
    @Value("${spring.rabbitmq.routingkey-paiement-nouveau}")
    private String routingKeyPaiementNouveau;

    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;
    @Value("${spring.rabbitmq.host}")
    private String host;

    @Bean
    Queue queuePariGagne() {
        return new Queue(queuePariGagne, true);
    }

    @Bean
    Exchange exchangePariGagne() {
        return ExchangeBuilder.directExchange(exchangePariGagne).durable(true).build();
    }

    @Bean("bindingPariGagne")
    Binding bindingPariGagne() {
        return BindingBuilder
                .bind(queuePariGagne())
                .to(exchangePariGagne())
                .with(routingKeyPariGagne)
                .noargs();
    }

    @Bean
    Queue queuePariAnnule() {
        return new Queue(queuePariAnnule, true);
    }

    @Bean
    Exchange exchangePariAnnule() {
        return ExchangeBuilder.directExchange(exchangePariAnnule).durable(true).build();
    }

    @Bean("bindingPariAnnule")
    Binding bindingPariAnnule() {
        return BindingBuilder
                .bind(queuePariAnnule())
                .to(exchangePariAnnule())
                .with(routingKeyPariAnnule)
                .noargs();
    }

    @Bean
    Queue queueInscription() {
        return new Queue(queueInscription, true);
    }

    @Bean
    Exchange exchangeInscription() {
        return ExchangeBuilder.directExchange(exchangeInscription).durable(true).build();
    }

    @Bean("bindingInscription")
    Binding bindingInscription() {
        return BindingBuilder
                .bind(queueInscription())
                .to(exchangeInscription())
                .with(routingKeyInscription)
                .noargs();
    }

    @Bean
    Queue queuePaiementNouveau() {
        return new Queue(queuePaiementNouveau, true);
    }

    @Bean
    Exchange exchangePaiementNouveau() {
        return ExchangeBuilder.directExchange(exchangePaiementNouveau).durable(true).build();
    }

    @Bean("bindingPaiementNouveau")
    Binding bindingPaiementNouveau() {
        return BindingBuilder
                .bind(queuePaiementNouveau())
                .to(exchangePaiementNouveau())
                .with(routingKeyPaiementNouveau)
                .noargs();
    }

    @Bean
    public ConnectionFactory connectionFactory() {
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
}