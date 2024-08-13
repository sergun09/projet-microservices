package fr.orleans.servicepaiement.services;

import fr.orleans.servicepaiement.dtos.TransactionDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqEnvoie {
    @Value("${spring.rabbitmq.exchange-paiement-nouveau}")
    private String exchangePaiementNouveau;
    @Value("${spring.rabbitmq.routingkey-paiement-nouveau}")
    private String routingKeyPaiementNouveau;

    private final RabbitTemplate rabbitTemplate;
   @Autowired
    public RabbitMqEnvoie(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    public void envoyer(TransactionDTO transactionDTO){
        rabbitTemplate.convertAndSend(exchangePaiementNouveau,routingKeyPaiementNouveau, transactionDTO);
    }

}
