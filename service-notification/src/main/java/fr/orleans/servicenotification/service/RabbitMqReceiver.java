package fr.orleans.servicenotification.service;

import fr.orleans.servicenotification.dtos.PaiementAnnuleDTO;
import fr.orleans.servicenotification.dtos.PaiementDTO;
import fr.orleans.servicenotification.dtos.TransactionDTO;
import fr.orleans.servicenotification.dtos.UtilisateurDTO;
import fr.orleans.servicenotification.entities.TypeNotification;
import fr.orleans.servicenotification.entities.TypeTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqReceiver implements RabbitListenerConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMqReceiver.class);
    private final FacadeNotification facadeNotification;

    public RabbitMqReceiver(FacadeNotification facadeNotification) {
        this.facadeNotification = facadeNotification;
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue-inscription}")
    public void receivedInscription(UtilisateurDTO utilisateurDTO) {
        logger.info("Consumer Inscription : User details received is : " + utilisateurDTO);
        long idU = utilisateurDTO.idUtilisateur();
        String description = "Bonjour "+utilisateurDTO.prenom()+" "+utilisateurDTO.nom()+", bienvenue sur bet-event";
        TypeNotification type = TypeNotification.INSCRIPTION;

        facadeNotification.addNotification(idU, description, type);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue-paiement-nouveau}")
    public void receivedPaiementNouveau(TransactionDTO transactionDTO) {
        logger.info("Consumer PaiementNouveau : Transaction details received is : " + transactionDTO);
        long idU = transactionDTO.idUtilisateur();
        String description = "Notification";
        TypeNotification type = TypeNotification.GENERIQUE;
        if (transactionDTO.typeTransaction().equals(TypeTransaction.COMPTE_PARIEUR_PARI.name())) {
            description = "Vous avez payé un pari pour un montant de "+transactionDTO.montant()+" € le "+transactionDTO.date().toString();
            type = TypeNotification.PAIEMENT_COMPTE_VERS_PARI;
        }
        else if (transactionDTO.typeTransaction().equals(TypeTransaction.CB_COMPTE_PARIEUR.name())) {
            description = "Vous avez crédité votre compte bet-event de "+transactionDTO.montant()+" € le "+transactionDTO.date().toString()+". Le montant a été débité de votre CB";
            type = TypeNotification.PAIEMENT_CB_VERS_COMPTE;
        }
        else if (transactionDTO.typeTransaction().equals(TypeTransaction.COMPTE_PARIEUR_CB.name())) {
            description = "Vous avez débité votre compte bet-event de "+transactionDTO.montant()+" € le "+transactionDTO.date().toString()+". Le montant a été crédité sur votre CB";
            type = TypeNotification.PAIEMENT_COMPTE_VERS_CB;
        }

        facadeNotification.addNotification(idU, description, type);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue-pari-gagne}")
    public void receivedPariGagne(PaiementDTO paiementDTO) {
        logger.info("Consumer PariGagne : Transaction details received is : " + paiementDTO);
        long idU = paiementDTO.idUtilisateur();
        String description = "Vous avez gagné le pari numéro "+paiementDTO.idPari()+" pour un montant de "+paiementDTO.gain()+" €.";
        TypeNotification type = TypeNotification.PARI_GAGNE;

        facadeNotification.addNotification(idU, description, type);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue-pari-annule}")
    public void receivedPariAnnule(PaiementAnnuleDTO paiementAnnuleDTO) {
        logger.info("Consumer PariGagne : Transaction details received is : " + paiementAnnuleDTO);
        long idU = paiementAnnuleDTO.idUtilisateur();
        String description = "Le pari numéro "+paiementAnnuleDTO.idPari()+" a été annulé et vous avez été remboursé de "+paiementAnnuleDTO.montant()+" €.";
        TypeNotification type = TypeNotification.PARI_ANNULE;

        facadeNotification.addNotification(idU, description, type);
    }
}
