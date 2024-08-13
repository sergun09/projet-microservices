package fr.orleans.serviceauthentification.service;

import fr.orleans.serviceauthentification.dtos.UtilisateurDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqService {
        @Value("${spring.rabbitmq.exchange}")
        private String exchange;
        @Value("${spring.rabbitmq.routingkey}")
        private String routingkey;
        private final RabbitTemplate rabbitTemplate;

        @Autowired
        public RabbitMqService(RabbitTemplate rabbitTemplate) {
            this.rabbitTemplate = rabbitTemplate;
        }
        public void envoyer(UtilisateurDTO utilisateurDTO){
            rabbitTemplate.convertAndSend(exchange,routingkey, utilisateurDTO);
        }
}
