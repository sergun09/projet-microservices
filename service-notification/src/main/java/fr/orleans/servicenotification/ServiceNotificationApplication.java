package fr.orleans.servicenotification;

import fr.orleans.servicenotification.dao.NotificationDAO;
import fr.orleans.servicenotification.dtos.UtilisateurDTO;
import fr.orleans.servicenotification.entities.Notification;
import fr.orleans.servicenotification.entities.TypeNotification;
import fr.orleans.servicenotification.service.FacadeNotification;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@SpringBootApplication
@EnableJpaRepositories
public class ServiceNotificationApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(ServiceNotificationApplication.class, args);
    }
}
