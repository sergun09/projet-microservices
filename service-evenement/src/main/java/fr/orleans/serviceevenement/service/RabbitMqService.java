package fr.orleans.serviceevenement.service;


import fr.orleans.serviceevenement.dtos.EvenementCreationDTO;
import fr.orleans.serviceevenement.dtos.EvenementResultatDTO;
import fr.orleans.serviceevenement.dtos.EvenementSportifDTO;
import fr.orleans.serviceevenement.dtos.EvenementSupprimerDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqService {
        @Value("${spring.rabbitmq.exchange-evenement}")
        private String exchange;
        @Value("${spring.rabbitmq.routingkey-evenement}")
        private String routingkey;

        @Value("${spring.rabbitmq.exchange-evenement2}")
        private String exchange2;
        @Value("${spring.rabbitmq.routingkey-evenement2}")
        private String routingkey2;

        @Value("${spring.rabbitmq.exchange-evenement3}")
        private String exchange3;
        @Value("${spring.rabbitmq.routingkey-evenement3}")
        private String routingkey3;

        private final RabbitTemplate rabbitTemplate;

        @Autowired
        public RabbitMqService(RabbitTemplate rabbitTemplate) {
                this.rabbitTemplate = rabbitTemplate;
        }

        public void envoyerEvenementCreation(EvenementCreationDTO evenementCreationDTO){
                rabbitTemplate.convertAndSend(exchange,routingkey, evenementCreationDTO);
        }

        public void envoyerEvenementResultat(EvenementResultatDTO evenementResultatDTO){
                rabbitTemplate.convertAndSend(exchange2,routingkey2, evenementResultatDTO);
        }

        public void envoyerEvenementSuppression(EvenementSupprimerDTO evenementSupprimerDTO){
                rabbitTemplate.convertAndSend(exchange3,routingkey3, evenementSupprimerDTO);
        }
}
