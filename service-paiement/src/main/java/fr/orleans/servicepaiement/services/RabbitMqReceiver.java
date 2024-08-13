package fr.orleans.servicepaiement.services;

import fr.orleans.servicepaiement.dtos.PaiementAnnuleDTO;
import fr.orleans.servicepaiement.dtos.PaiementDTO;
import fr.orleans.servicepaiement.dtos.UtilisateurDTO;
import fr.orleans.servicepaiement.services.exceptions.CompteInexistantException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqReceiver implements RabbitListenerConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMqReceiver.class);
    private final PaiementService paiementService;

    public RabbitMqReceiver(PaiementService paiementService) {
        this.paiementService = paiementService;
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue-inscription}")
    public void receivedInscription(UtilisateurDTO utilisateurDTO) {
        logger.info("Consumer 1 User Details Received is..." + utilisateurDTO);
        long idUtilisateur = utilisateurDTO.idUtilisateur();
        paiementService.creeCompte(idUtilisateur);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue-pari-gagne}")
    public void receivedPariGain(PaiementDTO paiementDTO) throws CompteInexistantException {
        logger.info("Consumer 1 User Details Received is..." + paiementDTO);
        long idUtilisateur = paiementDTO.idUtilisateur();
        long idPari = paiementDTO.idPari();
        double gain = paiementDTO.gain();
        paiementService.creerTransactionCrediterCompteGagnant(idPari,idUtilisateur,gain);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue-pari-annule}")
    public void receivedPariAnnule(PaiementAnnuleDTO paiementAnnuleDTO) throws CompteInexistantException {
        logger.info("Consumer 1 User Details Received is..." + paiementAnnuleDTO);
        long idUtilisateur = paiementAnnuleDTO.idUtilisateur();
        long idPari = paiementAnnuleDTO.idPari();
        double montant = paiementAnnuleDTO.montant();
        paiementService.creerTransactionAnnulerPari(idPari,idUtilisateur,montant);
    }

}